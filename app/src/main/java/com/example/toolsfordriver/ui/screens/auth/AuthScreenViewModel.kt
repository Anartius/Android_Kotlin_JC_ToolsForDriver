package com.example.toolsfordriver.ui.screens.auth

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.toolsfordriver.R
import com.example.toolsfordriver.data.model.AppUser
import com.example.toolsfordriver.ui.AuthUiState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthScreenViewModel: ViewModel() {
    private val auth = Firebase.auth

    private val _authUiState = MutableStateFlow(AuthUiState())
    val authUiState = _authUiState.asStateFlow()

    fun updateEmail(email: String) {
        _authUiState.value = _authUiState.value.copy(
            email = email
        )
    }
    fun updatePassword(password: String) {
        _authUiState.value = _authUiState.value.copy(
            password = password
        )
    }

    fun invertIsNewAccountValue() {
        _authUiState.value = _authUiState.value.copy(
            isNewAccount = !_authUiState.value.isNewAccount
        )
    }

    fun invertPasswordVisibilityValue() {
        _authUiState.value = _authUiState.value.copy(
            passwordVisibility = !_authUiState.value.passwordVisibility
        )
    }

    private fun updateLoading(value: Boolean) {
        _authUiState.value = _authUiState.value.copy(uploading = value)
    }

    fun signUser(
        context: Context,
        toHomeScreen: () -> Unit
    ) = viewModelScope.launch {

        val email = _authUiState.value.email
        val password = _authUiState.value.password

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.e(
                        "FB.auth",
                        "signInWithEmailAndPassword: success ${task.result}"
                    )
                    toHomeScreen()
                } else {
                    Log.e("FB.auth", "signInWithEmailAndPassword: ${task.exception}")
                    Toast.makeText(
                        context,
                        context.getString(R.string.authentication_failed),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    fun createNewUser(
        context: Context,
        toHomeScreen: () -> Unit
    ) {

        val email = _authUiState.value.email
        val password = _authUiState.value.password

        if (!_authUiState.value.uploading) {
            updateLoading(true)

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val displayName = task.result?.user?.email?.split("@")?.first()
                        createUser(displayName)
                        toHomeScreen()
                    } else {
                        Log.e("FB.auth", "createNewUser: ${task.exception?.message}")
                        Toast.makeText(
                            context,
                            task.exception?.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    updateLoading(false)
                }
        }
    }

    private fun createUser(displayName: String?) {
        val userId = auth.currentUser?.uid
        val user = AppUser(
            userId = userId.toString(),
            displayName = displayName.toString(),
            avatarUri = ""
        ).toMap()

        FirebaseFirestore.getInstance().collection("users").add(user)
    }
}

