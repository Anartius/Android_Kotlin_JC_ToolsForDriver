package com.example.toolsfordriver.ui.screens.myprofile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.toolsfordriver.R
import java.util.Locale

@Composable
fun SelectLocaleDialog(
    localeOptions: Map<Locale, String>,
    locale: Locale,
    onOptionSelected: (Locale) -> Unit
) {
    AlertDialog(
        onDismissRequest = { },
        confirmButton = { },
        title = {
            Text(
                text = stringResource(R.string.select_language),
                color = colorResource(id = R.color.light_blue),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        text = {
            Column(modifier = Modifier.selectableGroup()) {
                localeOptions.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .selectable(
                                selected = (item.key.language == locale.language),
                                onClick = { onOptionSelected(item.key) },
                                role = Role.RadioButton
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (item.key.language == locale.language),
                            onClick = null,
                            modifier = Modifier.padding(end = 8.dp),
                            colors = RadioButtonDefaults.colors(
                                selectedColor = colorResource(id = R.color.light_blue)
                            )
                        )
                        Text(text = item.value)
                    }
                }
            }
        },
        containerColor = colorResource(id = R.color.dark_gray).copy(alpha = 0.8f)
    )
}