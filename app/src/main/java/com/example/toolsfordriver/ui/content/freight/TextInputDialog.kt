package com.example.toolsfordriver.ui.content.freight

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.toolsfordriver.R
import com.example.toolsfordriver.ui.TFDViewModel
import com.example.toolsfordriver.ui.components.DialogButtons
import com.example.toolsfordriver.ui.components.DialogTitle
import com.example.toolsfordriver.ui.components.InputField

@Composable
fun TextInputDialog (
    viewModel: TFDViewModel,
    showDialog: MutableState<Boolean>
) {
    if (showDialog.value) {

        val freight = viewModel.uiState.collectAsStateWithLifecycle().value.currentFreight!!
        val scrollState = rememberScrollState()
        val trailingIconVisibility = mutableStateOf(false)
        val text = rememberSaveable { mutableStateOf(freight.notes ?: "") }

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
                        title = stringResource(id = R.string.note)
                    )

                    InputField(
                        modifier = Modifier
                            .height(330.dp)
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
                                text = stringResource(id = R.string.add_note) + "...",
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
                        viewModel.updateCurrentFreight(freight.copy(notes = text.value))
                    }
                }
            }
        }
    }
}