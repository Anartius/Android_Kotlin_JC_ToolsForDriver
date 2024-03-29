package com.example.toolsfordriver.ui.content.freight

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.toolsfordriver.ui.TFDViewModel
import com.example.toolsfordriver.ui.components.TextRow
import com.example.toolsfordriver.utils.dateAsString
import com.example.toolsfordriver.utils.dateAsStringIso
import com.example.toolsfordriver.utils.timeAsString
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
    showDeletePopup: MutableState<Boolean>,
    deleteItemKey: MutableState<Long?>,
    viewModel: TFDViewModel
) {
    val freight = viewModel.uiState.collectAsStateWithLifecycle().value.currentFreight!!

    val location = remember(freight) {
        mutableStateOf(
            if (isLoad) {
                freight.loads[time].toString()
            } else {
                freight.unloads[time].toString()
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
        clickable = true,
        onLongClick = {
            isLoadContent.value = isLoad
            showDeletePopup.value = true
            deleteItemKey.value = time
        }
    ) {
        selectedItemLocation.value = location.value
        selectedItemDateTime.value = dateTime.value
        isLoadDialog.value = isLoad
        showTimeLocationDialog.value = true
    }
}