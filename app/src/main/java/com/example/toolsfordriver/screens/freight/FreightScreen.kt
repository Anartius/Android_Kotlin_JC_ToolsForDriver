package com.example.toolsfordriver.screens.freight

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
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
    val isCreateFreight = remember(freightId) { mutableStateOf(freightId == "new" ) }

    val freightList = if (!isCreateFreight.value) {
        viewModel.freightList.collectAsState().value
    } else emptyList()

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
        }
    ) { paddingValue ->

        Surface(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValue)
        ) {

            val position = remember { mutableStateOf(0) }
            val scrollState = rememberScrollState()

            LaunchedEffect(scrollState.maxValue) {
                scrollState.scrollTo(
                    scrollState.maxValue
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {

                if (freightList.isEmpty() && !isCreateFreight.value) {
                    CircularProgressIndicator()
                } else {
                    FreightScreenContent(
                        freightList = freightList,
                        isCreateFreight = isCreateFreight,
                        navController = navController,
                        viewModel = viewModel,
                        position = position
                    )
                }
            }
        }
    }
}

@Composable
fun FreightScreenContent(
    freightList: List<FreightDBModel>,
    isCreateFreight: MutableState<Boolean>,
    navController: NavController,
    viewModel: FreightScreenViewModel,
    position: MutableState<Int>
) {
    val context = LocalContext.current

    val showDialog = rememberSaveable { mutableStateOf(false) }
    val showNoteDialog = rememberSaveable { mutableStateOf(false) }
    val showDateTimeDialog = rememberSaveable { mutableStateOf(false) }
    val isLoadDialog = rememberSaveable { mutableStateOf(true) }

    val freight = if (freightList.isNotEmpty()) {
        freightList.first()
    } else FreightDBModel(userId = FirebaseAuth.getInstance().currentUser!!.uid)

    val loadLocationState = rememberSaveable {
        mutableStateOf(
            if (freightList.isNotEmpty()) {
                freight.loads[freight.loads.keys.first()].toString()
            } else ""
        )
    }
    val loadLocation = remember(loadLocationState.value) {
        mutableStateOf(loadLocationState.value)
    }

    val unloadLocationState = rememberSaveable { mutableStateOf(
        if (freightList.isNotEmpty()) {
            freight.unloads[freight.unloads.keys.last()].toString()
        } else ""
    ) }
    val unloadLocation = remember(unloadLocationState.value) {
        mutableStateOf(unloadLocationState.value)
    }

    val loadDateTimeState = remember { mutableStateOf(
        if (freight.loads.isNotEmpty()) {
            LocalDateTime(
                LocalDate.parse(dateAsString(freight.loads.keys.first())),
                LocalTime.parse(timeAsString(freight.loads.keys.first()))
            )
        } else null
    )}
    val loadDateTime = remember(loadDateTimeState.value) {
        mutableStateOf(loadDateTimeState.value)
    }

    val unloadDateTimeState = remember { mutableStateOf(
        if (freight.unloads.isNotEmpty()) {
            LocalDateTime(
                LocalDate.parse(dateAsString(freight.unloads.keys.last())),
                LocalTime.parse(timeAsString(freight.unloads.keys.last()))
            )
        } else null
    )}
    val unloadDateTime = remember(unloadDateTimeState.value) {
        mutableStateOf(unloadDateTimeState.value)
    }

    val distanceState = rememberSaveable {
        mutableStateOf(
            if (freightList.isNotEmpty()) freight.distance.toString() else ""
        )
    }
    val distance = remember(distanceState.value) { mutableStateOf(distanceState.value) }

    val noteState = rememberSaveable {
        mutableStateOf(
            if (freightList.isNotEmpty() && freight.notes != null) freight.notes else ""
        )
    }
    val note = remember(noteState.value) { mutableStateOf(noteState.value) }

    LoadsUnloadsContent(
        isLoadsContent = true,
        location = loadLocation,
        dateTime = loadDateTime,
        isLoadDialog = isLoadDialog,
        showDialog = showDialog
    )

    LoadsUnloadsContent(
        isLoadsContent = false,
        isLoadDialog = isLoadDialog,
        showDialog = showDialog,
        location = unloadLocation,
        dateTime = unloadDateTime
    )

    DistanceContent(distanceState = distanceState,
        position = position)

    TitleRow(
        modifier = Modifier.padding(bottom = 12.dp),
        title = "Pictures:",
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
                "Unload cannot be before load or in the same time.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    LocationDateTimeDialog(
        showDialog = showDialog,
        showDateTimeDialog = showDateTimeDialog,
        dateTime = if (isLoadDialog.value) loadDateTimeState else unloadDateTimeState,
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
        modifier = Modifier.padding(vertical = 12.dp),
        title = if (isLoadsContent) "Loads:" else "Unloads:",
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
fun DistanceContent(
    distanceState: MutableState<String>,
    position: MutableState<Int>
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var distancePosition by remember { mutableStateOf(0f) }

    TitleRow(
        title = "Distance:",
        modifier = Modifier.padding(
            start = 0.dp, top = 12.dp, end = 0.dp, bottom = 0.dp
        )
    )

    Row(
        modifier = Modifier
            .height(70.dp)
            .padding(vertical = 0.dp, horizontal = 10.dp)
            .onGloballyPositioned { distancePosition = it.positionInRoot().y }
            .pointerInput(distancePosition) {
                position.value = distancePosition.toInt()
            },
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
        title = "Note:",
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
            text = noteState.value.ifEmpty { "Add note..." },
            color = if (noteState.value.isEmpty()) Color.Gray else Color.LightGray
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
    Row(modifier = modifier
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


