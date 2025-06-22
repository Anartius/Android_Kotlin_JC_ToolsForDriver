package com.example.toolsfordriver.ui.screens.freight.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.toolsfordriver.R
import com.example.toolsfordriver.ui.common.DigitInputField
import com.example.toolsfordriver.ui.common.text.TitleRowWithIcon
import com.example.toolsfordriver.ui.screens.freight.FreightViewModel

@Composable
fun DistanceContent(viewModel: FreightViewModel) {

    val freight = viewModel.uiState.collectAsStateWithLifecycle().value.currentFreight!!
    val keyboardController = LocalSoftwareKeyboardController.current

    TitleRowWithIcon(
        modifier = Modifier.padding(start = 24.dp, top = 12.dp, end = 24.dp),
        text = stringResource(id = R.string.distance)
    )

    Row(
        modifier = Modifier
            .height(70.dp)
            .padding(vertical = 0.dp, horizontal = 10.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {

        val focusManager = LocalFocusManager.current
        val distance = freight.distance

        val distanceState = remember(distance) {
            mutableStateOf(
                if (distance != null && distance > 0) distance.toString() else ""
            )
        }

        DigitInputField(
            modifier = Modifier,
            textValue = distanceState,
            placeholder = "0" + stringResource(id = R.string.km),
            suffix = stringResource(id = R.string.km),
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ) {
            viewModel.updateCurrentFreight(
                freight.copy(
                    distance = if (distanceState.value.isNotEmpty()) {
                        distanceState.value.toInt()
                    } else null
                )
            )

            focusManager.clearFocus()
            keyboardController?.hide()
        }
    }
}
