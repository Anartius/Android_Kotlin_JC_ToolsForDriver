package com.example.toolsfordriver.ui

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val isNewAccount: Boolean = false,
    val passwordVisibility: Boolean = false,
    val uploading: Boolean = false
)
