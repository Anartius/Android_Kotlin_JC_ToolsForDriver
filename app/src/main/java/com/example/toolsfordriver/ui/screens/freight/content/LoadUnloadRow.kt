package com.example.toolsfordriver.ui.screens.freight.content

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.toolsfordriver.common.dateAsString
import com.example.toolsfordriver.common.dateAsStringIso
import com.example.toolsfordriver.common.timeAsString
import com.example.toolsfordriver.ui.common.TextRow
import com.example.toolsfordriver.ui.screens.freight.FreightViewModel
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime


@Composable
fun LoadUnloadRow(
    isLoad: Boolean,
    isLoadContent: MutableState<Boolean>,
    time: Long,
    isLoadDialog: MutableState<Boolean>,
    showTimeLocationDialog: MutableState<Boolean>,
    selectedItemLocation: MutableState<String>,
    selectedItemDateTime: MutableState<LocalDateTime?>,
    deleteItemKey: MutableState<Long?>,
    viewModel: FreightViewModel,
    onLongClick: () -> Unit
) {
    val freight = viewModel.uiState.collectAsStateWithLifecycle().value.currentFreight!!

    val location = remember(freight) {
        mutableStateOf(
            if (isLoad) {
                freight.loads[time.toString()].toString()
            } else {
                freight.unloads[time.toString()].toString()
            }
        )
    }

    val dateTime = remember(time) {
        mutableStateOf(
            LocalDateTime(
                LocalDate.parse(dateAsStringIso(time)),
                LocalTime.parse(timeAsString(time))
            )
        )
    }

    TextRow(
        valueDescription = location.value.replace("#", ", "),
        value = "${dateAsString(time)}  ${dateTime.value.time}",
        firstTextColor = MaterialTheme.colorScheme.onBackground,
        clickable = true,
        onLongClick = {
            isLoadContent.value = isLoad
            onLongClick()
            deleteItemKey.value = time
        }
    ) {
        selectedItemLocation.value = location.value
        selectedItemDateTime.value = dateTime.value
        isLoadDialog.value = isLoad
        showTimeLocationDialog.value = true
    }
}