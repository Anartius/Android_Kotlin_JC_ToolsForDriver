package com.example.toolsfordriver.ui.content.freight

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.toolsfordriver.R
import com.example.toolsfordriver.ui.TFDViewModel
import com.example.toolsfordriver.ui.components.TFDAppBar

@Composable
fun FreightContent(
    viewModel: TFDViewModel,
    onNavIconClicked: () -> Unit
) {
    val context = LocalContext.current
    val imagesToDelete = rememberSaveable { mutableStateOf<MutableList<Uri>>(mutableListOf()) }


    Scaffold(
        topBar = {
            TFDAppBar(
                title = stringResource(id = R.string.freight),
                navIcon = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                onNavIconClicked = onNavIconClicked
            )
        },
        floatingActionButton = {
            FreightContentFAB(
                imagesToDelete = imagesToDelete,
                onFABClicked = onNavIconClicked,
                viewModel = viewModel,
                context = context
            )
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValue ->

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp, bottom = 100.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                FreightContentLazyColumn(
                    imagesToDelete = imagesToDelete,
                    viewModel = viewModel
                )
            }
        }
    }
}