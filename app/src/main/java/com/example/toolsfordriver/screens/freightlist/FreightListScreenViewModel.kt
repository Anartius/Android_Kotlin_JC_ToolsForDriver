package com.example.toolsfordriver.screens.freightlist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.toolsfordriver.data.FreightDBModel
import com.example.toolsfordriver.repository.Repository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FreightListScreenViewModel @Inject constructor(
    private val repository: Repository
): ViewModel() {
    private val _freightList = MutableStateFlow<List<FreightDBModel>>(emptyList())
    val freightList = _freightList.asStateFlow()
    private val userId = FirebaseAuth.getInstance().currentUser!!.uid

    init {
        viewModelScope.launch {
            repository
                .getAllFreightsByUserId(userId).distinctUntilChanged().collect { listOfFreights ->
                    if (listOfFreights.isEmpty()) {
                    Log.d("List Of Freights", ": Empty list")
                    _freightList.value = emptyList()
                } else _freightList.value = listOfFreights
            }
        }
    }

    fun deleteFreight(freight: FreightDBModel) {
        viewModelScope.launch { repository.deleteFreight(freight) }
    }
}