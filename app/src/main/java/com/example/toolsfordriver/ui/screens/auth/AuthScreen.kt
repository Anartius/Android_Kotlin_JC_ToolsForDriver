package com.example.toolsfordriver.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.toolsfordriver.R
import com.example.toolsfordriver.ui.screens.auth.content.TextRowButton
import com.example.toolsfordriver.ui.screens.auth.content.UserForm
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AuthScreen(onAuthSuccess: () -> Unit) {
    val viewModel: AuthScreenViewModel = hiltViewModel()
    val uiState = viewModel.authUiState.collectAsStateWithLifecycle().value
    val isNewAccount = uiState.isNewAccount
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

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
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    containerColor = Color.DarkGray,
                    contentColor = Color.White,
                    snackbarData = data
                )
            }
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier.fillMaxSize()
            .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                UserForm(viewModel = viewModel) { onAuthSuccess() }

                TextRowButton(
                    text = stringResource(
                        id = if (isNewAccount) R.string.have_account else R.string.new_account
                    ) + "?",
                    actionText = stringResource(id = if (isNewAccount) R.string.login else R.string.sign_up),
                    modifier = Modifier.padding(top = 30.dp)
                ) { viewModel.invertIsNewAccountValue() }

                TextRowButton(
                    text = stringResource(R.string.forgot_password_ask),
                    actionText = stringResource(R.string.reset)
                ) {
                    val email = uiState.email
                    viewModel.resetPassword(email = email)
                }
            }
        }
    }
}