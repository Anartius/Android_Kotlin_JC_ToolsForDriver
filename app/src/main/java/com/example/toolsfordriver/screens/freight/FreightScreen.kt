package com.example.toolsfordriver.screens.freight

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.toolsfordriver.R
import com.example.toolsfordriver.components.AppButton
import com.example.toolsfordriver.components.DeleteItemPopup
import com.example.toolsfordriver.components.DigitInputField
import com.example.toolsfordriver.components.TFDAppBar
import com.example.toolsfordriver.components.TextInputDialog
import com.example.toolsfordriver.components.TextRow
import com.example.toolsfordriver.components.TimeLocationDialog
import com.example.toolsfordriver.data.FreightDBModel
import com.example.toolsfordriver.navigation.TFDScreens
import com.example.toolsfordriver.utils.dateAsString
import com.example.toolsfordriver.utils.dateAsStringIso
import com.example.toolsfordriver.utils.timeAsString
import com.google.firebase.auth.FirebaseAuth
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

@Composable
fun FreightScreen(
    navController: NavController,
    viewModel: FreightScreenViewModel,
    freightId: String
) {
    val isCreateFreight = remember(freightId) { mutableStateOf(freightId == "new") }
    val context = LocalContext.current

    val freight = rememberSaveable {
        mutableStateOf(FreightDBModel(userId = FirebaseAuth.getInstance().currentUser!!.uid))
    }

    Scaffold(
        topBar = {
            TFDAppBar(
                title = "Freight",
                navIcon = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                navIconDescription = "Back",
                onNavIconClicked = {
                    navController.navigate(TFDScreens.FreightListScreen.name)
                }
            )
        },
        floatingActionButton = {
            FABContent(
                isCreateFreight = isCreateFreight,
                freight = freight,
                navController = navController,
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
            val freightList = if (!isCreateFreight.value) {
                viewModel.freightList.collectAsState().value
            } else emptyList()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp, bottom = 100.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {

                if (freightList.isEmpty() && !isCreateFreight.value) {
                    CircularProgressIndicator()
                } else {
                    if (freightList.isNotEmpty()) freight.value = freightList.first()

                    FreightScreenContent(freight = freight)
                }
            }
        }
    }
}

@Composable
fun FreightScreenContent(
    freight: MutableState<FreightDBModel>
) {
    val context = LocalContext.current

    val selectedItemLocation = remember { mutableStateOf("") }
    val selectedItemDateTime = remember { mutableStateOf<LocalDateTime?>(null) }

    val showTimeLocationDialog = rememberSaveable { mutableStateOf(false) }
    val showDateTimeDialog = rememberSaveable { mutableStateOf(false) }
    val showNoteDialog = rememberSaveable { mutableStateOf(false) }
    val showDeletePopup = rememberSaveable { mutableStateOf(false) }
    val isLoadDialog = rememberSaveable { mutableStateOf(true) }
    val isLoadContent = rememberSaveable { mutableStateOf(true) }
    val deleteItemKey = rememberSaveable { mutableStateOf<Long?>(null) }

    LazyColumn(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
    ) {
        item {
            TitleRow(
                modifier = Modifier.padding(bottom = 12.dp),
                title = "Loads",
                icon = Icons.Filled.Add,
                showIcon = true,
                iconDescription = "add place and time"
            ) {
                selectedItemDateTime.value = null
                selectedItemLocation.value = ""
                isLoadDialog.value = true
                showTimeLocationDialog.value = true
            }
        }

        items(
            items = freight.value.loads.toList().sortedBy { it.first }
        ) { item ->
            isLoadContent.value = true

            LoadUnloadRow(
                isLoad = true,
                isLoadContent = isLoadContent,
                freight = freight,
                time = item.first,
                isLoadDialog = isLoadDialog,
                showTimeLocationDialog = showTimeLocationDialog,
                selectedItemLocation = selectedItemLocation,
                selectedItemDateTime = selectedItemDateTime,
                showDeletePopup = showDeletePopup,
                deleteItemKey = deleteItemKey
            )
        }

        item {
            TitleRow(
                modifier = Modifier.padding(bottom = 12.dp),
                title = "Unloads",
                icon = Icons.Filled.Add,
                showIcon = true,
                iconDescription = "add place and time"
            ) {
                selectedItemDateTime.value = null
                selectedItemLocation.value = ""
                isLoadDialog.value = false
                showTimeLocationDialog.value = true
            }
        }

        items(
            items = freight.value.unloads.toList().sortedBy { it.first }
        ) { item ->
            isLoadContent.value = false

            LoadUnloadRow(
                isLoad = false,
                isLoadContent = isLoadContent,
                freight = freight,
                time = item.first,
                isLoadDialog = isLoadDialog,
                showTimeLocationDialog = showTimeLocationDialog,
                selectedItemLocation = selectedItemLocation,
                selectedItemDateTime = selectedItemDateTime,
                showDeletePopup = showDeletePopup,
                deleteItemKey = deleteItemKey
            )
        }

        item { DistanceContent(freight = freight) }

        item {
            TitleRow(
                modifier = Modifier.padding(bottom = 12.dp),
                title = "Pictures",
                icon = Icons.Filled.Add,
                showIcon = true,
                iconDescription = "add picture"
            ) {
                // TODO ADD PICTURES
            }
        }

        item {
            NoteContent(
                freight = freight,
                showNoteDialog = showNoteDialog
            )
        }
    }

    TimeLocationDialog(
        isLoadDialog = isLoadDialog,
        showDialog = showTimeLocationDialog,
        showDateTimeDialog = showDateTimeDialog,
        location = selectedItemLocation,
        dateTime = selectedItemDateTime,
        freight = freight,
        context = context
    )

    TextInputDialog(
        freight = freight,
        showDialog = showNoteDialog
    )

    DeleteItemPopup(
        showDeletePopup = showDeletePopup,
        itemName = if (isLoadContent.value) "load" else "unload"
    ) {
        deleteItemKey.value?.let {
            val itemMap = (if (isLoadContent.value) {
                freight.value.loads
            } else freight.value.unloads).toMutableMap()

            itemMap.remove(deleteItemKey.value)

            freight.value = freight.value.copy(
                loads = if (isLoadContent.value) {
                    itemMap.toMap()
                } else freight.value.loads,
                unloads = if (!isLoadContent.value) {
                    itemMap.toMap()
                } else freight.value.unloads
            )
        }
        deleteItemKey.value = null
        showDeletePopup.value = false
    }
}

@Composable
fun LoadUnloadRow(
    isLoad: Boolean,
    isLoadContent: MutableState<Boolean>,
    freight: MutableState<FreightDBModel>,
    time: Long,
    isLoadDialog: MutableState<Boolean>,
    showTimeLocationDialog: MutableState<Boolean>,
    selectedItemLocation: MutableState<String>,
    selectedItemDateTime: MutableState<LocalDateTime?>,
    showDeletePopup: MutableState<Boolean>,
    deleteItemKey: MutableState<Long?>
) {

    val location = remember(freight.value) {
        mutableStateOf(
            if (isLoad) {
                freight.value.loads[time].toString()
            } else {
                freight.value.unloads[time].toString()
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

@Composable
fun DistanceContent(freight: MutableState<FreightDBModel>) {

    val keyboardController = LocalSoftwareKeyboardController.current

    TitleRow(
        title = "Distance",
        modifier = Modifier.padding(
            start = 0.dp, top = 12.dp, end = 0.dp, bottom = 0.dp
        )
    )

    Row(
        modifier = Modifier
            .height(70.dp)
            .padding(vertical = 0.dp, horizontal = 10.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {

        val focusManager = LocalFocusManager.current
        val distance = freight.value.distance

        val distanceState = remember(distance) {
            mutableStateOf(
                if (distance != null && distance > 0) distance.toString() else ""
            )
        }

        DigitInputField(
            textValue = distanceState,
            placeholder = "0 km",
            suffix = " km",
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ) {
            freight.value = freight.value.copy(
                distance = if (distanceState.value.isNotEmpty()) {
                    distanceState.value.toInt()
                } else null
            )

            focusManager.clearFocus()
            keyboardController?.hide()
        }
    }
}

@Composable
fun NoteContent(
    freight: MutableState<FreightDBModel>,
    showNoteDialog: MutableState<Boolean>
) {

    TitleRow(
        title = "Note",
        modifier = Modifier.padding(
            start = 0.dp, top = 12.dp, end = 0.dp, bottom = 0.dp
        )
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 15.dp, horizontal = 25.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val note = freight.value.notes

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showNoteDialog.value = true },
            text = if (note.isNullOrEmpty()) "Add note" else note,
            color = if (note.isNullOrEmpty()) Color.Gray else Color.LightGray,
            maxLines = 5,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun TitleRow(
    modifier: Modifier = Modifier,
    title: String,
    icon: ImageVector = Icons.Filled.Edit,
    showIcon: Boolean = false,
    iconDescription: String = "icon",
    onIconClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = colorResource(id = R.color.light_blue)
        )
        if (showIcon) {
            Icon(
                imageVector = icon,
                contentDescription = iconDescription,
                modifier = modifier
                    .padding(0.dp)
                    .clickable { onIconClick.invoke() },
                tint = colorResource(id = R.color.light_blue)
            )
        }
    }
}


@Composable
fun FABContent(
    isCreateFreight: MutableState<Boolean>,
    freight: MutableState<FreightDBModel>,
    navController: NavController,
    viewModel: FreightScreenViewModel,
    context: Context
) {
    AppButton(
        buttonText = if (isCreateFreight.value) "Add Freight" else "Update Freight",
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 50.dp, vertical = 15.dp),
        enabled = freight.value.loads.isNotEmpty() &&
                freight.value.unloads.isNotEmpty() &&
                freight.value.distance != null && freight.value.distance != 0
    ) {
        val firstLoadTime = freight.value.loads.keys.min()
        val lastLoadTime = freight.value.loads.keys.max()
        val firstUnloadTime = freight.value.unloads.keys.min()
        val lastUnloadTime = freight.value.unloads.keys.max()

        if (firstLoadTime < lastUnloadTime
            && firstLoadTime < firstUnloadTime
            && lastLoadTime < lastUnloadTime
        ) {
            val freightUpdated = freight.value.copy()

            if (isCreateFreight.value) {
                viewModel.addFreight(freightUpdated)
            } else viewModel.updateFreight(freightUpdated)
            navController.popBackStack()
        } else {
            Toast.makeText(
                context,
                "Unload before load or in the same time",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}


