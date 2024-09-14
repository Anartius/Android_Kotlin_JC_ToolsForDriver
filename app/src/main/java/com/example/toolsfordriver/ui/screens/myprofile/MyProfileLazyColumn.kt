package com.example.toolsfordriver.ui.screens.myprofile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.toolsfordriver.R
import com.example.toolsfordriver.ui.common.DigitalInputWithTitle
import com.example.toolsfordriver.ui.common.TextInputWithTitle

@Composable
fun MyProfileLazyColumn(
    viewModel: MyProfileViewModel
) {
    val currentUser = viewModel.uiState.collectAsStateWithLifecycle().value.user

    val firstName = currentUser?.firstName ?: ""
    val lastName = currentUser?.lastName ?: ""
    val paymentPerDay = currentUser?.paymentPerDay
    val paymentPerHour = currentUser?.paymentPerHour

    val firstNameState = remember { mutableStateOf(firstName) }
    val lastNameState = remember { mutableStateOf(lastName) }
    val paymentPerDayState = remember { mutableStateOf(paymentPerDay.toString()) }
    val paymentPerHourState = remember { mutableStateOf(paymentPerHour.toString()) }

    if (currentUser != null) {
        LaunchedEffect(key1 = firstNameState.value) {
            viewModel.updateCurrentUser(
                currentUser.copy(firstName = firstNameState.value)
            )
        }

        LaunchedEffect(key1 = lastNameState.value) {
            viewModel.updateCurrentUser(
                currentUser.copy(lastName = lastNameState.value)
            )
        }

        LaunchedEffect(key1 = paymentPerDayState.value) {
            viewModel.updateCurrentUser(
                currentUser.copy(
                    paymentPerDay = if (paymentPerDayState.value.isNotEmpty()) {
                        paymentPerDayState.value.toDouble()
                    } else 0.0
                )
            )
        }

        LaunchedEffect(key1 = paymentPerHourState.value) {
            viewModel.updateCurrentUser(
                currentUser.copy(
                    paymentPerHour = if (paymentPerHourState.value.isNotEmpty()) {
                        paymentPerHourState.value.toDouble()
                    } else 0.0
                )
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp, bottom = 20.dp)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    UserImage(
                        currentUser = currentUser,
                        viewModel = viewModel
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
                        thickness = 1.dp,
                        color = colorResource(id = R.color.light_blue)
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(4.dp))

                TextInputWithTitle(
                    title = stringResource(R.string.first_name),
                    valueState = firstNameState,
                    placeHolder = stringResource(R.string.add_first_name),
                    onFocusChanged = { viewModel.updateUser(currentUser) }
                ) { }
            }

            item {
                Spacer(modifier = Modifier.height(4.dp))

                TextInputWithTitle(
                    title = stringResource(R.string.last_name),
                    valueState = lastNameState,
                    placeHolder = stringResource(R.string.add_last_name),
                    onFocusChanged = { viewModel.updateUser(currentUser) }
                ) { }
            }

            item {
                Spacer(modifier = Modifier.height(4.dp))

                DigitalInputWithTitle(
                    title = stringResource(R.string.payment_per_day),
                    valueState = paymentPerDayState,
                    placeholder = "0" + stringResource(id = R.string.pln),
                    suffix = stringResource(id = R.string.pln),
                    onFocusChanged = { viewModel.updateUser(currentUser) }
                ) { }
            }

            item {
                Spacer(modifier = Modifier.height(4.dp))

                DigitalInputWithTitle(
                    title = stringResource(R.string.payment_per_hour),
                    valueState = paymentPerHourState,
                    placeholder = "0" + stringResource(id = R.string.pln),
                    suffix = stringResource(id = R.string.pln),
                    onFocusChanged = { viewModel.updateUser(currentUser) }
                ) { }
            }
        }
    }
}