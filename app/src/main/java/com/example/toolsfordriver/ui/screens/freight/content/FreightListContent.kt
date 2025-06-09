package com.example.toolsfordriver.ui.screens.freight.content

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.toolsfordriver.R
import com.example.toolsfordriver.common.dateAsString
import com.example.toolsfordriver.data.model.Freight
import com.example.toolsfordriver.ui.common.DeleteItemPopup
import com.example.toolsfordriver.ui.common.buttons.FAB
import com.example.toolsfordriver.ui.common.TFDAppBar
import com.example.toolsfordriver.ui.screens.freight.FreightViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun FreightListContent(
    viewModel: FreightViewModel,
    onNavIconClicked: () -> Unit
) {
    val freightList = viewModel.freights.collectAsStateWithLifecycle(emptyList()).value
    val showDeletePopup = viewModel.uiState.collectAsStateWithLifecycle().value.showDeletePopup
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    BackHandler(enabled = true) { onNavIconClicked.invoke() }

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
                    Freight(userId = FirebaseAuth.getInstance().currentUser!!.uid)
                )
                viewModel.setCurrentFreightAsNew(true)
                viewModel.showFreightContent(true)
            }
        }
    ) { paddingValue ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
        ) {
            LazyColumn {
                items(items = freightList) { item ->
                    FreightRow(
                        freight = item,
                        viewModel = viewModel
                    )
                }
            }
        }

        if (showDeletePopup) {
            DeleteItemPopup(
                itemName = stringResource(id = R.string.freight),
                onDismiss = { viewModel.showDeletePopup(false) }
            ) { viewModel.deleteFreight() }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FreightRow(
    freight: Freight,
    viewModel: FreightViewModel
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 6.dp)
            .combinedClickable(
                enabled = true,
                onClick = {
                    viewModel.updateCurrentFreight(freight)
                    viewModel.setCurrentFreightAsNew(false)
                    viewModel.showFreightContent(true)
                },
                onLongClick = {
                    viewModel.addFreightToDelete(freight)
                    viewModel.showDeletePopup(true)
                }
            ),
        border = BorderStroke(
            width = 0.5.dp,
            color = colorResource(id = R.color.light_blue).copy(alpha = 0.6f)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 30.dp, vertical = 10.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val startDate = dateAsString(freight.loads.keys.minOf { it }.toLong())
                val endDate = dateAsString(freight.unloads.keys.maxOf { it }.toLong())

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = startDate,
                        maxLines = 1
                    )
                    Text(
                        text = freight.loads[freight.loads.keys.minOf { it }]
                            ?.replace("#", ", ")?.trimEnd() ?: "",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Arrow Forward",
                    modifier = Modifier.weight(0.5f),
                    tint = colorResource(id = R.color.gray),
                )

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = endDate,
                        maxLines = 1
                    )
                    Text(
                        text = freight.unloads[freight.unloads.keys.maxOf { it }]
                            ?.replace("#", ", ")?.trimEnd() ?: "",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}