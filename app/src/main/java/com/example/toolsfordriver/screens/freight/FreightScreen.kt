package com.example.toolsfordriver.screens.freight

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.AddCircleOutline
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(25.dp),
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
                        viewModel = viewModel
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
    viewModel: FreightScreenViewModel
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val context = LocalContext.current

    val showDialog = rememberSaveable { mutableStateOf(false) }
    val showDateTimeDialog = rememberSaveable { mutableStateOf(false) }
    val isLoadingDialog = rememberSaveable { mutableStateOf(true) }
    val showDistanceInputField = rememberSaveable { mutableStateOf(false) }

    val freight = if (freightList.isNotEmpty()) {
        freightList.first()
    } else FreightDBModel(userId = FirebaseAuth.getInstance().currentUser!!.uid)

    val loadingLocationState = rememberSaveable {
        mutableStateOf(
            if (freightList.isNotEmpty()) {
                freight.loadings[freight.loadings.keys.first()].toString()
            } else ""
        )
    }
    val loadingLocation = remember(loadingLocationState.value) {
        mutableStateOf(loadingLocationState.value)
    }

    val unloadingLocationState = rememberSaveable { mutableStateOf(
        if (freightList.isNotEmpty()) {
            freight.unloading[freight.unloading.keys.last()].toString()
        } else ""
    ) }
    val unloadingLocation = remember(unloadingLocationState.value) {
        mutableStateOf(unloadingLocationState.value)
    }

    val loadingDateTimeState = remember { mutableStateOf(
        if (freight.loadings.isNotEmpty()) {
            LocalDateTime(
                LocalDate.parse(dateAsString(freight.loadings.keys.first())),
                LocalTime.parse(timeAsString(freight.loadings.keys.first()))
            )
        } else null
    )}
    val loadingDateTime = remember(loadingDateTimeState.value) {
        mutableStateOf(loadingDateTimeState.value)
    }

    val unloadingDateTimeState = remember { mutableStateOf(
        if (freight.unloading.isNotEmpty()) {
            LocalDateTime(
                LocalDate.parse(dateAsString(freight.unloading.keys.last())),
                LocalTime.parse(timeAsString(freight.unloading.keys.last()))
            )
        } else null
    )}
    val unloadingDateTime = remember(unloadingDateTimeState.value) {
        mutableStateOf(unloadingDateTimeState.value)
    }

    val distanceState = rememberSaveable {
        mutableStateOf(
            if (freightList.isNotEmpty()) freight.distance.toString() else ""
        )
    }
    val distance = remember(distanceState.value) { mutableStateOf(distanceState.value) }


    TitleRow(
        title = "Loadings:",
        icon = Icons.Filled.AddCircleOutline,
        iconDescription = "add place and time"
    ) {
        isLoadingDialog.value = true
        showDialog.value = true
    }

    TextRow(
        description = loadingLocation.value.replace("#", ", "),
        value = "${loadingDateTime.value?.date ?: ""} ${loadingDateTime.value?.time ?: ""}",
        clickable = true
    ) {
        isLoadingDialog.value = true
        showDialog.value = true
    }

    TitleRow(
        title = "Unloading:",
        icon = Icons.Filled.AddCircleOutline,
        iconDescription = "add place and time"
    ) {
        isLoadingDialog.value = false
        showDialog.value = true
    }

    TextRow(
        description = unloadingLocation.value.replace("#", ", "),
        value = "${unloadingDateTime.value?.date ?: ""} ${unloadingDateTime.value?.time ?: ""}",
        clickable = true
    ) {
        isLoadingDialog.value = false
        showDialog.value = true
    }

    TitleRow(
        title = "Distance:",
        icon = if (distance.value.isEmpty()) {
            Icons.Filled.AddCircleOutline
        } else Icons.Filled.Edit,
        iconDescription = "add distance",
        modifier = Modifier.padding(
            start = 5.dp, top = 12.dp, end = 5.dp, bottom = 0.dp
        )
    ) {
        showDistanceInputField.value = true
    }

    LaunchedEffect(showDistanceInputField.value) {
        if (showDistanceInputField.value){
            focusRequester.requestFocus()
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(vertical = 0.dp, horizontal = 5.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showDistanceInputField.value) {
            DigitInputField(
                textValue = distanceState,
                modifier = Modifier.fillMaxWidth(),
                focusRequester = focusRequester,
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ) {
                keyboardController?.hide()
                showDistanceInputField.value = false
            }
        } else {
            Text(text = if (distance.value.isNotEmpty()) "${distance.value}km." else "")
        }
    }


    TitleRow(
        title = "Pictures:",
        icon = Icons.Filled.AddCircleOutline,
        iconDescription = "add picture",
        modifier = Modifier.padding(
            start = 5.dp, top = 0.dp, end = 5.dp, bottom = 12.dp
        )
    ) {}

    TitleRow(
        title = "Note:",
        icon = Icons.Filled.AddCircleOutline,
        iconDescription = "add note"
    ) {}

    Spacer(modifier = Modifier.height(40.dp))

    AppButton(
        buttonText = if (isCreateFreight.value) "Add Freight" else "Update Freight",
        enabled = loadingLocation.value.isNotEmpty() &&
                unloadingLocation.value.isNotEmpty() &&
                distance.value.isNotEmpty()
    ) {
        if (loadingDateTime.value!! < unloadingDateTime.value!!) {

            val freightUpdated = freight.copy(
                loadings = mapOf(
                    loadingDateTime.value!!.toInstant(
                        TimeZone.currentSystemDefault()
                    ).toEpochMilliseconds() to loadingLocation.value
                ),
                unloading = mapOf(
                    unloadingDateTime.value!!.toInstant(
                        TimeZone.currentSystemDefault()
                    ).toEpochMilliseconds() to unloadingLocation.value
                ),
                distance = distance.value.toInt()
            )

            if (isCreateFreight.value) {
                viewModel.addFreight(freightUpdated)
            } else viewModel.updateFreight(freightUpdated)
            navController.popBackStack()
        } else {
            Toast.makeText(
                context,
                "Unloading cannot be before loading.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    LocationDateTimeDialog(
        showDialog = showDialog,
        showDateTimeDialog = showDateTimeDialog,
        dateTime = if (isLoadingDialog.value) loadingDateTimeState else unloadingDateTimeState,
        location = if (isLoadingDialog.value) loadingLocationState else unloadingLocationState,
        context = context
    )
}

@Composable
fun TitleRow(
    title: String,
    icon: ImageVector,
    iconDescription: String,
    modifier: Modifier = Modifier.padding(horizontal = 5.dp, vertical = 12.dp),
    onIconClick: () -> Unit = {}
) {
    Row(modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = colorResource(id = R.color.light_blue)
        )
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


