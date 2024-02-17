package com.example.toolsfordriver.screens.auth

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.toolsfordriver.model.AppUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class AuthScreenViewModel: ViewModel() {
    private val auth = Firebase.auth

    private val _load = MutableLiveData(false)
    val load: LiveData<Boolean> = _load

    fun signUserWithEmailAndPassword(
        email: String,
        password: String,
        context: Context,
        toHomeScreen: () -> Unit
    ) = viewModelScope.launch {
        try {
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
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("FB.auth", "signInWithEmailAndPassword: ${exception.message}")
                    Toast.makeText(
                        context,
                        "Authentication failed. Wrong email or password",
                        Toast.LENGTH_LONG
                    ).show()
                }
        } catch (e: Exception) {
            Log.e("FB.auth", "signInWithEmailAndPassword: ${e.message}")
        }
    }

    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        toHomeScreen: () -> Unit
    ) {
        if (_load.value == false) {
            _load.value = true
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val displayName = task.result?.user?.email?.split("@")?.first()
                        createUser(displayName)

                        toHomeScreen()
                    } else {
                        Log.e(
                            "FB.auth",
                            "signInWithEmailAndPassword: success ${task.result}"
                        )
                    }
                    
                    _load.value = false
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

