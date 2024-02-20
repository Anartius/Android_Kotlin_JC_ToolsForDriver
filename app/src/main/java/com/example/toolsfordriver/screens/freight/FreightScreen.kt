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
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

@Composable
fun FreightScreen(
    navController: NavController,
    viewModel: FreightScreenViewModel,
    freightId: String
) {
    val isCreateFreight = remember(freightId) { mutableStateOf(freightId == "new") }
    val context = LocalContext.current

    val freightState = rememberSaveable {
        mutableStateOf(FreightDBModel(userId = FirebaseAuth.getInstance().currentUser!!.uid))
    }

    val loadLocationState = rememberSaveable { mutableStateOf("") }
    val loadLocation = remember(loadLocationState.value) {
        mutableStateOf(loadLocationState.value)
    }

    val unloadLocationState = rememberSaveable { mutableStateOf("") }
    val unloadLocation = remember(unloadLocationState.value) {
        mutableStateOf(unloadLocationState.value)
    }

    val loadDateTime = remember { mutableStateOf<LocalDateTime?>(null) }
    val unloadDateTime = remember { mutableStateOf<LocalDateTime?>(null) }

    val distanceState = rememberSaveable { mutableStateOf("") }
    val noteState = rememberSaveable { mutableStateOf("") }

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
                freight = freightState.value,
                loadLocation = loadLocation,
                loadDateTime = loadDateTime,
                unloadLocation = unloadLocation,
                unloadDateTime = unloadDateTime,
                distanceState = distanceState,
                noteState = noteState,
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
                    .fillMaxSize()
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
                        freightState = freightState,
                        loadLocationState = loadLocationState,
                        loadDateTime = loadDateTime,
                        unloadLocationState = unloadLocationState,
                        unloadDateTime = unloadDateTime,
                        distanceState = distanceState,
                        noteState = noteState,
                        context = context
                    )
                }
            }
        }
    }
}

@Composable
fun FreightScreenContent(
    freightList: List<FreightDBModel>,
    freightState: MutableState<FreightDBModel>,
    loadLocationState: MutableState<String>,
    loadDateTime: MutableState<LocalDateTime?>,
    unloadLocationState: MutableState<String>,
    unloadDateTime: MutableState<LocalDateTime?>,
    distanceState: MutableState<String>,
    noteState: MutableState<String>,
    context: Context
) {

    val showDialog = rememberSaveable { mutableStateOf(false) }
    val showNoteDialog = rememberSaveable { mutableStateOf(false) }
    val showDateTimeDialog = rememberSaveable { mutableStateOf(false) }
    val isLoadDialog = rememberSaveable { mutableStateOf(true) }

    if (freightList.isNotEmpty()) freightState.value = freightList.first()
    val freight = freightState.value

    if (freightList.isNotEmpty()) {
        loadLocationState.value = freight.loads[freight.loads.keys.first()].toString()
        unloadLocationState.value = freight.unloads[freight.unloads.keys.last()].toString()

        loadDateTime.value = LocalDateTime(
            LocalDate.parse(dateAsString(freight.loads.keys.first())),
            LocalTime.parse(timeAsString(freight.loads.keys.first()))
        )

        unloadDateTime.value = LocalDateTime(
            LocalDate.parse(dateAsString(freight.unloads.keys.last())),
            LocalTime.parse(timeAsString(freight.unloads.keys.last()))
        )

        distanceState.value = freight.distance.toString()
        if (freight.notes != null) noteState.value = freight.notes
    }

    LoadsUnloadsContent(
        isLoadsContent = true,
        location = loadLocationState,
        dateTime = loadDateTime,
        isLoadDialog = isLoadDialog,
        showDialog = showDialog
    )

    LoadsUnloadsContent(
        isLoadsContent = false,
        isLoadDialog = isLoadDialog,
        showDialog = showDialog,
        location = unloadLocationState,
        dateTime = unloadDateTime
    )

    DistanceContent(distanceState = distanceState)

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
        noteState = noteState,
        showNoteDialog = showNoteDialog
    )

    LocationDateTimeDialog(
        showDialog = showDialog,
        showDateTimeDialog = showDateTimeDialog,
        dateTime = if (isLoadDialog.value) loadDateTime else unloadDateTime,
        location = if (isLoadDialog.value) loadLocationState else unloadLocationState,
        context = context
    )

    TextInputDialog(
        showDialog = showNoteDialog,
        textValue = noteState
    )
}

@Composable
fun LoadsUnloadsContent(
    isLoadsContent: Boolean,
    isLoadDialog: MutableState<Boolean>,
    showDialog: MutableState<Boolean>,
    location: MutableState<String>,
    dateTime: MutableState<LocalDateTime?>
) {
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

    TextRow(
        valueDescription = location.value.replace("#", ", "),
        value = "${dateTime.value?.date ?: ""} ${dateTime.value?.time ?: ""}",
        clickable = true
    ) {
        isLoadDialog.value = isLoadsContent
        showDialog.value = true
    }
}

@Composable
fun DistanceContent(distanceState: MutableState<String>) {
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

        DigitInputField(
            textValue = distanceState,
            placeholder = "0 km",
            suffix = " km",
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ) {
            focusManager.clearFocus()
            keyboardController?.hide()
        }
    }
}

@Composable
fun NoteContent(
    noteState: MutableState<String>,
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
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showNoteDialog.value = true },
            text = noteState.value.ifEmpty { "Add note" },
            color = if (noteState.value.isEmpty()) Color.Gray else Color.LightGray,
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
    loadLocation: MutableState<String>,
    loadDateTime: MutableState<LocalDateTime?>,
    unloadLocation: MutableState<String>,
    unloadDateTime: MutableState<LocalDateTime?>,
    distanceState: MutableState<String>,
    noteState: MutableState<String>,
    freight: FreightDBModel,
    navController: NavController,
    viewModel: FreightScreenViewModel,
    context: Context
) {

    val distance = remember(distanceState.value) { mutableStateOf(distanceState.value) }
    val note = remember(noteState.value) { mutableStateOf(noteState.value) }

    AppButton(
        buttonText = if (isCreateFreight.value) "Add Freight" else "Update Freight",
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 50.dp, vertical = 15.dp),
        enabled = loadLocation.value.isNotEmpty() &&
                unloadLocation.value.isNotEmpty() &&
                distance.value.isNotEmpty() && distance.value != "0"
    ) {
        if (loadDateTime.value!! < unloadDateTime.value!!) {

            val freightUpdated = freight.copy(
                loads = mapOf(
                    loadDateTime.value!!.toInstant(
                        TimeZone.currentSystemDefault()
                    ).toEpochMilliseconds() to loadLocation.value
                ),
                unloads = mapOf(
                    unloadDateTime.value!!.toInstant(
                        TimeZone.currentSystemDefault()
                    ).toEpochMilliseconds() to unloadLocation.value
                ),
                distance = distance.value.toInt(),
                notes = note.value
            )

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


