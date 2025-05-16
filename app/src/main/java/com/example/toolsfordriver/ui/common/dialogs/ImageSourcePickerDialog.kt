package com.example.toolsfordriver.ui.common.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.toolsfordriver.R

@Composable
fun ImageSourcePickerDialog(
    showDialog: MutableState<Boolean>,
    showCamera: () -> Unit = {},
    showGallery: () -> Unit = {}
) {
    if (showDialog.value) {
        Dialog(onDismissRequest = {}) {
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .padding(4.dp)
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
                    DialogTitle(title = stringResource(R.string.choose_source))

                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painterResource(id = R.drawable.ic_outline_photo_camera_24),
                            contentDescription = stringResource(R.string.camera),
                            modifier = Modifier
                                .size(64.dp)
                                .clickable { showCamera() },
                            tint = colorResource(id = R.color.light_blue)
                        )
                        Icon(
                            painterResource(id = R.drawable.ic_outline_photo_library_24),
                            contentDescription = stringResource(R.string.gallery),
                            modifier = Modifier
                                .size(64.dp)
                                .clickable { showGallery() },
                            tint = colorResource(id = R.color.light_blue)
                        )
                    }

                    DialogButtons(
                        showConfirmButton = false,
                        onDismiss = { showDialog.value = false }
                    )
                }
            }
        }
    }
}