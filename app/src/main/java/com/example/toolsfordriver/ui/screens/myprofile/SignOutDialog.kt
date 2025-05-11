package com.example.toolsfordriver.ui.screens.myprofile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.toolsfordriver.R
import com.example.toolsfordriver.ui.common.dialogs.DialogButtons
import com.example.toolsfordriver.ui.common.dialogs.DialogTitle

@Composable
fun SignOutDialog(onConfirm: () -> Unit) {
    val viewModel: MyProfileViewModel = hiltViewModel()

    Dialog(onDismissRequest = { viewModel.showSignOutDialog(false) }) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            colors = CardDefaults.cardColors(
                containerColor = colorResource(id = R.color.dark_gray).copy(alpha = 0.8f)
            ),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                DialogTitle(
                    modifier = Modifier.padding(top = 10.dp),
                    title = stringResource(id = R.string.sign_out)
                )

                Text(
                    text = stringResource(R.string.ask_for_log_out_ensure),
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    textAlign = TextAlign.Center
                )

                DialogButtons(onDismiss = { viewModel.showSignOutDialog(false) }) {
                    onConfirm()
                }
            }
        }
    }
}