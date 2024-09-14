package com.example.toolsfordriver.ui.screens.home

import androidx.lifecycle.ViewModel
import com.example.toolsfordriver.data.model.User
import com.example.toolsfordriver.data.model.service.FirestoreService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(storageService: FirestoreService) : ViewModel() {
    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState = _homeUiState.asStateFlow()
    val users = storageService.users

    fun updateCurrentUser (user: User) {
        _homeUiState.value = _homeUiState.value.copy(user = user)
    }
}