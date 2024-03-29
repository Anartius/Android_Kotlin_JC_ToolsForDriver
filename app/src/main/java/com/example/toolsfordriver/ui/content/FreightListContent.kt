package com.example.toolsfordriver.ui.content

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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.toolsfordriver.R
import com.example.toolsfordriver.data.FreightDBModel
import com.example.toolsfordriver.ui.TFDViewModel
import com.example.toolsfordriver.ui.components.DeleteItemPopup
import com.example.toolsfordriver.ui.components.FABContent
import com.example.toolsfordriver.ui.components.TFDAppBar
import com.example.toolsfordriver.utils.dateAsString
import com.google.firebase.auth.FirebaseAuth

@Composable
fun FreightListContent(
    viewModel: TFDViewModel,
    onNavIconClicked: () -> Unit
) {
    val freightList = viewModel.uiState.collectAsStateWithLifecycle().value.freights.value

    val showDeletePopup = remember { mutableStateOf(false) }
    val freightToDelete = remember { mutableStateOf<FreightDBModel?>(null) }

    BackHandler(enabled = true) { onNavIconClicked.invoke() }

    Scaffold(
        topBar = {
            TFDAppBar(
                title = stringResource(id = R.string.freights),
                navIcon = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                onNavIconClicked = onNavIconClicked
            )
        },
        floatingActionButton = {
            FABContent(fabDescription = stringResource(id = R.string.add_freight)) {
                viewModel.updateCurrentFreight(
                    FreightDBModel(userId = FirebaseAuth.getInstance().currentUser!!.uid)
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
                        freightToDelete = freightToDelete,
                        viewModel = viewModel,
                        showDeletePopup = showDeletePopup
                    )
                }
            }
        }

        DeleteItemPopup(
            showDeletePopup = showDeletePopup,
            itemName = "freight"
        ) {
            freightToDelete.value?.let {
                viewModel.deleteFreight(it)
            }
            freightToDelete.value = null
            showDeletePopup.value = false
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FreightRow(
    freight: FreightDBModel,
    viewModel: TFDViewModel,
    freightToDelete: MutableState<FreightDBModel?>,
    showDeletePopup: MutableState<Boolean>
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
                    freightToDelete.value = freight
                    showDeletePopup.value = true
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
                val startDate = dateAsString(freight.loads.keys.minOf { it })
                val endDate = dateAsString(freight.unloads.keys.maxOf { it })

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
                    tint = Color.Gray,
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