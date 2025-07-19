package com.example.toolsfordriver.ui.screens.freight.content

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.toolsfordriver.R
import com.example.toolsfordriver.common.getNameStrRes
import com.example.toolsfordriver.data.model.Freight
import com.example.toolsfordriver.data.model.FreightDateCategory
import com.example.toolsfordriver.ui.common.ActionIcon
import com.example.toolsfordriver.ui.common.SwipeableItemWithActions
import com.example.toolsfordriver.ui.common.TFDAppBar
import com.example.toolsfordriver.ui.common.buttons.FAB
import com.example.toolsfordriver.ui.common.dialogs.ActionConfirmDialog
import com.example.toolsfordriver.ui.common.text.CategoryHeader
import com.example.toolsfordriver.ui.screens.freight.FreightViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.YearMonth
import java.time.ZoneId

@Composable
fun FreightListContent(onNavIconClicked: () -> Unit) {

    val viewModel: FreightViewModel = hiltViewModel()
    val users = viewModel.users.collectAsStateWithLifecycle(initialValue = emptyList()).value
    val freightList = viewModel.freights.collectAsStateWithLifecycle(emptyList()).value

    if (users.isNotEmpty()) {
        val context = LocalContext.current
        val snackbarHostState = remember { SnackbarHostState() }

        val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
        val swipedItemId = uiState.swipedItemId
        val showDeleteConfDialog = uiState.showDeleteItemConfDialog

        BackHandler(enabled = true) { onNavIconClicked() }

        LaunchedEffect(viewModel.snackbarMessages) {
            viewModel.snackbarMessages.collect { snackbarMessage ->
                val job = launch {
                    snackbarHostState.showSnackbar(
                        message = snackbarMessage.asString(context),
                        duration = SnackbarDuration.Indefinite
                    )
                }
                delay(5000)
                job.cancel()
            }
        }

        Scaffold(
            topBar = {
                TFDAppBar(
                    title = stringResource(id = R.string.freights),
                    navIcon = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    onNavIconClicked = onNavIconClicked
                )
            },
            floatingActionButton = {
                FAB(fabDescription = stringResource(id = R.string.add_freight)) {
                    viewModel.updateCurrentFreight(
                        Freight(userId = viewModel.userId)
                    )
                    viewModel.setCurrentFreightAsNew(true)
                    viewModel.showFreightContent(true)
                }
            }
        ) { paddingValue ->
            LaunchedEffect(swipedItemId) { }

            if (freightList.isNotEmpty()) {
                val groupedFreightMap = freightList.groupBy {
                    YearMonth.from(it.firstLoadTime?.toInstant()?.atZone(ZoneId.systemDefault()))
                }.toSortedMap()

                val categoryList = groupedFreightMap.map {
                    val month = it.key.month.getNameStrRes()?.let { stringResource(it) } ?: ""

                    FreightDateCategory(
                        name = month + " " + it.key.year,
                        yearMonth = it.key,
                        items = it.value.asReversed()
                    )
                }.asReversed()

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValue)
                ) {
                    categoryList.forEach { category ->
                        stickyHeader {
                            CategoryHeader(text = category.name)
                        }

                        items(items = category.items) { item ->
                            val isRevealed = swipedItemId == item.id

                            SwipeableItemWithActions(
                                isRevealed = isRevealed,
                                actions = {
                                    ActionIcon(
                                        icon = Icons.Outlined.Delete,
                                        iconDescription = stringResource(R.string.delete) +
                                                stringResource(R.string.freight),
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .padding(start = 30.dp),
                                        tint = Color.Red
                                    ) {
                                        viewModel.addFreightToDelete(freight = item)
                                        viewModel.showDeleteItemConfDialog(true)
                                    }
                                },
                                onExpanded = { viewModel.updateSwipedItemId(item.id) },
                                onCollapsed = { viewModel.updateSwipedItemId("") },
                                onSwipeDetected = { viewModel.updateSwipedItemId("") }
                            ) {
                                FreightRow(freight = item) {
                                    viewModel.updateCurrentFreight(item)
                                    viewModel.updateCurrentFreightBeforeChange(item)
                                    viewModel.setCurrentFreightAsNew(false)
                                    viewModel.showFreightContent(true)
                                }
                            }
                        }
                    }
                }

                if (showDeleteConfDialog) {
                    ActionConfirmDialog(
                        title = stringResource(R.string.freight_delete),
                        message = stringResource(R.string.ask_to_freight_delete),
                        onConfirm = {
                            viewModel.deleteFreight(
                                context.getString(
                                    R.string.freight_deleted_successfully)
                            )
                            viewModel.updateSwipedItemId("")
                        },
                        onDismiss = {
                            viewModel.showDeleteItemConfDialog(false)
                            viewModel.updateSwipedItemId("")
                        }
                    )
                }
            }
        }
    } else Text(stringResource(R.string.user_not_found))
}