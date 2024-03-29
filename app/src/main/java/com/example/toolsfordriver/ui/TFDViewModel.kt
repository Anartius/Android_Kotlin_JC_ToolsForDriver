package com.example.toolsfordriver.ui

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.toolsfordriver.data.FreightDBModel
import com.example.toolsfordriver.data.TripDBModel
import com.example.toolsfordriver.repository.Repository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class TFDViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    private val userId = FirebaseAuth.getInstance().currentUser!!.uid

    private val _tripList = MutableStateFlow<List<TripDBModel>>(emptyList())
    private val _freightList = MutableStateFlow<List<FreightDBModel>>(emptyList())

    private val _uiState = MutableStateFlow(TFDUiState())
    val uiState = _uiState.asStateFlow()
    init {
        initializeUiState()
    }

    private fun initializeUiState() {
        viewModelScope.launch {
            repository.getAllTripsByUserId(userId).distinctUntilChanged().collect { listOfTrips ->
                if (listOfTrips.isEmpty()) {
                    Log.d("List Of Trips", ": Empty list")
                    _tripList.value = emptyList()
                } else {
                    _tripList.value = listOfTrips
                    _uiState.value = _uiState.value.copy(
                        trips = mutableStateOf(_tripList.value)
                    )
                }
            }
        }
        viewModelScope.launch {
            repository
                .getAllFreightsByUserId(userId).distinctUntilChanged().collect { listOfFreights ->
                    if (listOfFreights.isEmpty()) {
                        Log.d("List Of Freights", ": Empty list")
                        _freightList.value = emptyList()
                    } else {
                        _freightList.value = listOfFreights
                        _uiState.value = _uiState.value.copy(
                            freights = mutableStateOf(_freightList.value)
                        )
                    }
                }
        }
    }

    fun updateCurrentTrip(trip: TripDBModel?) {
        _uiState.update { it.copy(currentTrip = trip) }
    }

    fun showTripContent(value: Boolean) {
        _uiState.update { it.copy(showTripContent = value) }
    }

    fun setCurrentTripAsNew(value: Boolean) {
        _uiState.update { it.copy(isNewTrip = value) }
    }

    fun addTrip(trip: TripDBModel) {
        viewModelScope.launch { repository.addTrip(trip) }
    }

    fun updateTrip(trip: TripDBModel) {
        viewModelScope.launch { repository.updateTrip(trip) }
    }
    fun deleteTrip(trip: TripDBModel) {
        viewModelScope.launch { repository.deleteTrip(trip) }
    }

    fun updateCurrentFreight(freight: FreightDBModel?) {
        _uiState.update { it.copy(currentFreight = freight) }
    }

    fun showFreightContent(value: Boolean) {
        _uiState.update { it.copy(showFreightContent = value) }
    }

    fun setCurrentFreightAsNew(value: Boolean) {
        _uiState.update { it.copy(isNewFreight = value) }
    }

    fun addFreight(freight: FreightDBModel) {
        viewModelScope.launch { repository.addFreight(freight) }
    }

    fun updateFreight(freight: FreightDBModel) {
        viewModelScope.launch { repository.updateFreight(freight) }
    }
    fun deleteFreight(freight: FreightDBModel) {
        viewModelScope.launch { repository.deleteFreight(freight) }
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