package com.example.toolsfordriver.ui.screens.freight.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.toolsfordriver.R
import com.example.toolsfordriver.ui.screens.freight.FreightViewModel

@Composable
fun NoteContent(
    viewModel: FreightViewModel,
    showNoteDialog: MutableState<Boolean>
) {
    val freight = viewModel.uiState.collectAsStateWithLifecycle().value.currentFreight!!

    ItemTitleRow(
        title = stringResource(id = R.string.note),
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 24.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val note = freight.note

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showNoteDialog.value = true },
            text = if (note.isNullOrEmpty()) stringResource(id = R.string.add_note) else note,
            color = if (note.isNullOrEmpty()) {
                colorResource(id = R.color.gray)
            } else colorResource(id = R.color.light_gray),
            maxLines = 5,
            overflow = TextOverflow.Ellipsis
        )
    }
}