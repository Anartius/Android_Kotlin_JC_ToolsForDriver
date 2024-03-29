package com.example.toolsfordriver.ui.content.freight

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.toolsfordriver.R
import com.example.toolsfordriver.ui.TFDViewModel

@Composable
fun NoteContent(
    viewModel: TFDViewModel,
    showNoteDialog: MutableState<Boolean>
) {
    val freight = viewModel.uiState.collectAsStateWithLifecycle().value.currentFreight!!

    ItemsTitleRow(
        title = stringResource(id = R.string.note),
        modifier = Modifier.padding(
            start = 0.dp, top = 12.dp, end = 0.dp, bottom = 0.dp
        )
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 15.dp, horizontal = 25.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val note = freight.notes

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showNoteDialog.value = true },
            text = if (note.isNullOrEmpty()) stringResource(id = R.string.add_note) else note,
            color = if (note.isNullOrEmpty()) Color.Gray else Color.LightGray,
            maxLines = 5,
            overflow = TextOverflow.Ellipsis
        )
    }
}