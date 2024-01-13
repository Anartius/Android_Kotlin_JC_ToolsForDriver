package com.example.toolsfordriver.screens.home

import android.app.TimePickerDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerLayoutType
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.toolsfordriver.R
import com.example.toolsfordriver.components.AppButton
import com.example.toolsfordriver.components.TFDAppBar
import com.example.toolsfordriver.navigation.TFDScreens
import com.example.toolsfordriver.screens.list.ListScreen
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    var showDatePickerDialog by remember { mutableStateOf(false) }
    var showTimePickerDialog by remember { mutableStateOf(false) }
    val dateState = rememberDatePickerState()
    val timeState = rememberTimePickerState()

    Scaffold(
        topBar = {
            TFDAppBar(
                title = "Tools For Driver",
                actionIcon = Icons.Filled.Logout,
                actionIconDescription = "Log Out",
                onActionIconClicked = {
                    FirebaseAuth.getInstance().signOut().run {
                        navController.navigate(TFDScreens.AuthScreen.name)
                    }
                }
            )
        }
    ) { paddingValue ->
        Surface(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValue)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AppButton(buttonText = "Show Trips") {
                    navController.navigate(TFDScreens.ListScreen.name + "/Trip")
                }

                Button(
                    onClick = {
                        showDatePickerDialog = !showDatePickerDialog
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 50.dp, vertical = 0.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.DarkGray,
                        contentColor = colorResource(id = R.color.light_blue)
                    )
                ) {
                    Text(text = "Select Date")
                }
                if (showDatePickerDialog) {
                    DatePickerDialog(
                        onDismissRequest = { showDatePickerDialog = false },
                        confirmButton = {
                            TextButton(onClick = { showDatePickerDialog = false }) {
                                Text(text = "Ok")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDatePickerDialog = false }) {
                                Text(text = "Cancel")
                            }
                        },
                        colors = DatePickerDefaults.colors(
                            containerColor = Color.Red,
                            titleContentColor = Color.Yellow,
                            headlineContentColor = Color.Green,
                            weekdayContentColor = Color.Blue
                        )
                    ) {
                        DatePicker(state = dateState)
                    }
                }

                Button(
                    onClick = {
                        showTimePickerDialog = !showTimePickerDialog
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 50.dp, vertical = 5.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.DarkGray,
                        contentColor = colorResource(id = R.color.light_blue)
                    )
                ) {
                    Text(text = "Select Time")
                }
                if (showTimePickerDialog) {
                    TimePicker(state = timeState)
                }
            }
        }
    }
}
