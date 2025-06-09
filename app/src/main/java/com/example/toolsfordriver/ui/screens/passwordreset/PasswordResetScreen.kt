package com.example.toolsfordriver.ui.screens.passwordreset

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.toolsfordriver.R
import com.example.toolsfordriver.ui.common.buttons.AppButton
import com.example.toolsfordriver.ui.common.textfields.PasswordInput
import com.example.toolsfordriver.ui.common.TFDAppBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PasswordResetScreen(oobCode: String, onNavigateToAuthScreen: () -> Unit) {
    val viewModel: PasswordResetScreenViewModel = hiltViewModel()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val passwordState = rememberSaveable { mutableStateOf("") }

    LaunchedEffect(viewModel.snackbarMessages) {
        viewModel.snackbarMessages.collect { snackbarMessage ->
            val job = launch {
                snackbarHostState.showSnackbar(
                    message = snackbarMessage.asString(context),
                    duration = SnackbarDuration.Indefinite
                )
            }
            delay(7000)
            job.cancel()
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    containerColor = Color.DarkGray,
                    contentColor = Color.White,
                    snackbarData = data
                )
            }
        },
        topBar = {
            TFDAppBar(
                title = stringResource(R.string.password_reset),
                navIcon = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                onNavIconClicked = { onNavigateToAuthScreen() }
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(150.dp))

                PasswordInput(
                    modifier = Modifier.padding(vertical = 30.dp),
                    passwordState = passwordState
                )

                AppButton(buttonText = stringResource(R.string.confirm)) {
                    viewModel.resetPassword(oobCode, passwordState.value) {
                        onNavigateToAuthScreen()
                    }
                }
            }
        }
    }
}