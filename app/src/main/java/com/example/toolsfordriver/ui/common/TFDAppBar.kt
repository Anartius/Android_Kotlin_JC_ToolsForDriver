package com.example.toolsfordriver.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import com.example.toolsfordriver.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TFDAppBar(
    title: String,
    navIcon: ImageVector? = null,
    navIconDescription: String = stringResource(id = R.string.to_the_previous_screen),
    modifier: Modifier = Modifier,
    avatarAction: (@Composable () -> Unit)? = null,
    actions: List<Triple<ImageVector, String, () -> Unit>> = emptyList(),
    onNavIconClicked:() -> Unit = {}
) {
    Column(modifier = modifier) {
        CenterAlignedTopAppBar(
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = title,
                        color = colorResource(id = R.color.light_blue),
                        style = MaterialTheme.typography.titleLarge
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
                if (avatarAction != null) {
                    avatarAction()
                }
                actions.forEach { action ->
                    IconButton(onClick = { action.third.invoke() }) {
                        Icon(
                            imageVector = action.first,
                            contentDescription = action.second
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(Color.Transparent)
        )
    }
}