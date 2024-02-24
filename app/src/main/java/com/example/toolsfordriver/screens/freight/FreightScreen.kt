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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
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
import com.example.toolsfordriver.components.LocationDateTimeDialog
import com.example.toolsfordriver.components.TFDAppBar
import com.example.toolsfordriver.components.TextInputDialog
import com.example.toolsfordriver.components.TextRow
import com.example.toolsfordriver.data.FreightDBModel
import com.example.toolsfordriver.navigation.TFDScreens
import com.example.toolsfordriver.utils.dateAsString
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
        }
    ) { paddingValue ->

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
        ) {
            val scrollState = rememberScrollState()

            val freightList = if (!isCreateFreight.value) {
                viewModel.freightList.collectAsState().value
            } else emptyList()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp, bottom = 100.dp)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {

                if (freightList.isEmpty() && !isCreateFreight.value) {
                    CircularProgressIndicator()
                } else {
                    FreightScreenContent(
                        freightList = freightList,
                        freight = freight
                    )
                }
            }
        }
    }
}

@Composable
fun FreightScreenContent(
    freightList: List<FreightDBModel>,
    freight: MutableState<FreightDBModel>
) {

    val showNoteDialog = rememberSaveable { mutableStateOf(false) }

    if (freightList.isNotEmpty()) freight.value = freightList.first()

    LoadsUnloadsContent(
        isLoadsContent = true,
        freight = freight
    )

    LoadsUnloadsContent(
        isLoadsContent = false,
        freight = freight
    )

    DistanceContent(freight = freight)

    TitleRow(
        modifier = Modifier.padding(bottom = 12.dp),
        title = "Pictures",
        icon = Icons.Filled.Add,
        showIcon = true,
        iconDescription = "add picture"
    ) {
        // TODO ADD PICTURES
    }

    NoteContent(
        freight = freight,
        showNoteDialog = showNoteDialog
    )

    TextInputDialog(
        freight = freight,
        showDialog = showNoteDialog
    )
}

@Composable
fun LoadsUnloadsContent(
    isLoadsContent: Boolean,
    freight: MutableState<FreightDBModel>
) {
    val context = LocalContext.current
    val showDialog = rememberSaveable { mutableStateOf(false) }
    val showDateTimeDialog = rememberSaveable { mutableStateOf(false) }
    val showDeletePopup = rememberSaveable { mutableStateOf(false) }
    val isLoadDialog = rememberSaveable { mutableStateOf(true) }

    val selectedItemLocation = remember { mutableStateOf("") }
    val selectedItemDateTime = remember { mutableStateOf<LocalDateTime?>(null) }

    TitleRow(
        modifier = Modifier.padding(bottom = 12.dp),
        title = if (isLoadsContent) "Loads" else "Unloads",
        icon = Icons.Filled.Add,
        showIcon = true,
        iconDescription = "add place and time"
    ) {
        isLoadDialog.value = isLoadsContent
        showDialog.value = true
    }

    val deleteItemKey = rememberSaveable { mutableStateOf<Long?>(null) }

    val keyList = if (isLoadsContent) {
        freight.value.loads.keys.toList().sorted()
    } else freight.value.unloads.keys.toList().sorted()

    if (keyList.isNotEmpty()) {
        keyList.forEach { time ->

            val location = remember(freight.value) {
                mutableStateOf(
                    if (isLoadsContent) {
                        freight.value.loads[time].toString()
                    } else {
                        freight.value.unloads[time].toString()
                    }
                )
            }

            val dateTime = remember(time) {
                mutableStateOf(
                    LocalDateTime(
                        LocalDate.parse(dateAsString(time)),
                        LocalTime.parse(timeAsString(time))
                    )
                )
            }

            TextRow(
                valueDescription = location.value.replace("#", ", "),
                value = "${dateTime.value.date} ${dateTime.value.time}",
                clickable = true,
                onLongClick = {
                    showDeletePopup.value = true
                    deleteItemKey.value = time
                }
            ) {
                selectedItemLocation.value = location.value
                selectedItemDateTime.value = dateTime.value
                isLoadDialog.value = isLoadsContent
                showDialog.value = true
            }
        }
    }

    LocationDateTimeDialog(
        isLoadDialog = isLoadDialog,
        showDialog = showDialog,
        showDateTimeDialog = showDateTimeDialog,
        location = selectedItemLocation,
        dateTime = selectedItemDateTime,
        freight = freight,
        context = context
    )

    DeleteItemPopup(
        showDeletePopup = showDeletePopup,
        itemName = if (isLoadsContent) "load" else "unload"
    ) {
        deleteItemKey.value?.let {
            val itemMap = (if (isLoadsContent) {
                freight.value.loads
            } else freight.value.unloads).toMutableMap()

            itemMap.remove(deleteItemKey.value)

            freight.value = freight.value.copy(
                loads = if (isLoadsContent) {
                    itemMap.toMap()
                } else freight.value.loads,
                unloads = if (!isLoadsContent) {
                    itemMap.toMap()
                } else freight.value.unloads
            )
        }
        deleteItemKey.value = null
        showDeletePopup.value = false
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


