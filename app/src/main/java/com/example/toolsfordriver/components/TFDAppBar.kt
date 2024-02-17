package com.example.toolsfordriver.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.toolsfordriver.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TFDAppBar(
    title: String,
    navIcon: ImageVector? = null,
    navIconDescription: String = "",
    actionIcon: ImageVector? = null,
    actionIconDescription: String = "",
    onNavIconClicked:() -> Unit = {},
    onActionIconClicked:() -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    color = colorResource(id = R.color.light_blue),
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)
                )
            }
        },
        navigationIcon = {
            if (navIcon != null) {
                IconButton(
                    onClick = { onNavIconClicked.invoke() },
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = colorResource(id = R.color.light_blue)
                    )
                ) {
                    Icon(imageVector = navIcon, contentDescription = navIconDescription)
                }
            }
        },
        actions = {
            if (actionIcon != null) {
                IconButton(onClick = { onActionIconClicked.invoke() }) {
                    Icon(imageVector = actionIcon, contentDescription = actionIconDescription)
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(Color.Transparent)
    )
}