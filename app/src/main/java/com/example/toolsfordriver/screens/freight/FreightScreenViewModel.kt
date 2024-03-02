package com.example.toolsfordriver.screens.freight

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.toolsfordriver.data.FreightDBModel
import com.example.toolsfordriver.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class FreightScreenViewModel @Inject constructor(
    private val repository: Repository,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val id = checkNotNull(savedStateHandle.get<String>("freightId"))
    private val _freightList = MutableStateFlow<List<FreightDBModel>>(emptyList())
    val freightList = _freightList.asStateFlow()

    init {
        viewModelScope.launch {
            _freightList.value = listOf(repository.getFreightById(id))
        }
    }

    fun addFreight(freight: FreightDBModel) {
        viewModelScope.launch { repository.addFreight(freight) }
    }

    fun updateFreight(freight: FreightDBModel) {
        viewModelScope.launch { repository.updateFreight(freight) }
    }


    fun saveImageToInternalStorage(context: Context, uri: Uri): Uri {
        val fileName = String.format(Locale.GERMANY, "%d.jpg", System.currentTimeMillis())

        val inputStream = context.contentResolver.openInputStream(uri)
        val outputFile = context.filesDir.resolve(fileName)

        inputStream?.use {
            it.copyTo(outputFile.outputStream())
        }

        return outputFile.toUri()
    }
}