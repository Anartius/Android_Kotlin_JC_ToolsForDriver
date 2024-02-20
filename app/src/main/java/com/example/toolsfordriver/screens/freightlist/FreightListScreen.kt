package com.example.toolsfordriver.screens.freightlist

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.toolsfordriver.R
import com.example.toolsfordriver.components.DeleteItemPopup
import com.example.toolsfordriver.components.FABContent
import com.example.toolsfordriver.components.TFDAppBar
import com.example.toolsfordriver.data.FreightDBModel
import com.example.toolsfordriver.navigation.TFDScreens
import com.example.toolsfordriver.utils.dateAsString

@Composable
fun FreightListScreen(
    navController: NavController,
    viewModel: FreightListScreenViewModel
) {
    val freightList = viewModel.freightList.collectAsState().value
    val showDeletePopup = remember { mutableStateOf(false) }
    val freightToDelete = remember { mutableStateOf<FreightDBModel?>(null) }

    Scaffold(
        topBar = {
            TFDAppBar(
                title = "Freights",
                navIcon = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                navIconDescription = "Back",
                onNavIconClicked = {
                    navController.navigate(TFDScreens.HomeScreen.name)
                }
            )
        },
        floatingActionButton = {
            FABContent(fabDescription = "add freight") {
                navController.navigate(TFDScreens.FreightScreen.name + "/new")
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
                        navController = navController,
                        showDeletePopup = showDeletePopup
                    )
                }
            }
        }

        if (showDeletePopup.value) {
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
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FreightRow(
    freight: FreightDBModel,
    freightToDelete: MutableState<FreightDBModel?>,
    navController: NavController,
    showDeletePopup: MutableState<Boolean>
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 6.dp)
            .combinedClickable(
                enabled = true,
                onClick = {
                    navController.navigate(TFDScreens.FreightScreen.name + "/${freight.id}")
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
                    .padding(vertical = 10.dp, horizontal = 30.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val startDate = dateAsString(freight.loads.keys.first())
                val endDate = dateAsString(freight.unloads.keys.last())

                Column {
                    Text(text = startDate)
                    Text(
                        text = freight.loads[freight.loads.keys.first()]?.replace("#", ", ") ?: "",
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Arrow Forward",
                    tint = Color.Gray,
                )
                Column {
                    Text(text = endDate)
                    Text(
                        text = freight.unloads[freight.unloads.keys.last()]
                            ?.replace("#", ", ") ?: "",
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}