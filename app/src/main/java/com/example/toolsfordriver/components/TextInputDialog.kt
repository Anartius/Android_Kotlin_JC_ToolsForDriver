package com.example.toolsfordriver.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.toolsfordriver.R

@Composable
fun TextInputDialog (
    showDialog: MutableState<Boolean>,
    textValue: MutableState<String>
) {
    if (showDialog.value) {
        val text = rememberSaveable { mutableStateOf(textValue.value.ifEmpty { "" }) }
        val scrollState = rememberScrollState()
        val trailingIconVisibility = mutableStateOf(false)

        Dialog(onDismissRequest = {}) {
            Card(
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    DialogTitle(
                        modifier = Modifier.padding(vertical = 15.dp),
                        title = "Add or edit the Note:"
                    )

                    InputField(
                        modifier = Modifier
                            .height(340.dp)
                            .fillMaxWidth()
                            .verticalScroll(scrollState)
                            .padding(start = 10.dp, top = 5.dp, end = 10.dp, bottom = 0.dp)
                            .border(
                                width = 1.dp,
                                color = colorResource(id = R.color.light_blue),
                                shape = RoundedCornerShape(10.dp)
                            ),
                        textValueState = text,
                        isOutlined = false,
                        trailingIconVisibility = trailingIconVisibility,
                        maxLines = 10,
                        placeholder = {
                            Text(
                                text = "Add note...",
                                color = Color.Gray
                            )
                        },
                        isSingleLine = false,
                        inputTextAlign = TextAlign.Justify,
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences,
                            imeAction = ImeAction.Default
                        )
                    )

                    DialogButtons(
                        showDialog = showDialog
                    ) {
                        showDialog.value = false
                        if (text.value.isNotEmpty()) textValue.value = text.value
                    }
                }
            }
        }
    }
}