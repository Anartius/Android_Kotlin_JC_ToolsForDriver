package com.example.toolsfordriver.data.model.service.impl

import com.example.toolsfordriver.R
import com.example.toolsfordriver.common.UiText
import com.example.toolsfordriver.data.model.User
import com.example.toolsfordriver.data.model.service.AccountService
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AccountServiceImpl @Inject constructor(private val auth: FirebaseAuth) : AccountService {
    private val snackbarChannel = Channel<UiText>()

    override val hasUser: Boolean
        get() = auth.currentUser != null

    override val currentUser: Flow<User>
        get() = callbackFlow {
            val listener = FirebaseAuth.AuthStateListener { auth ->
                this.trySend(auth.currentUser?.let { User(it.uid) } ?: User())
            }
            auth.addAuthStateListener(listener)
            awaitClose { auth.removeAuthStateListener(listener) }
        }

    override val currentUserId: String
        get() = auth.currentUser?.uid.orEmpty()

    override suspend fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).await()
    }

    override suspend fun authenticate(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun sendRecoveryEmail(email: String) {
        auth.useAppLanguage()
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    snackbarChannel.trySend(UiText.StringResource(R.string.recovery_email_sent))
                } else {
                    snackbarChannel.trySend(
                        UiText.DynamicString("${task.exception?.message}")
                    )
                }
            }.await()
    }

    override suspend fun resetPassword(oobCode: String, password: String, onSuccess: () -> Unit) {
        auth.verifyPasswordResetCode(oobCode)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    auth.confirmPasswordReset(oobCode, password)
                        .addOnCompleteListener {
                            if (task.isSuccessful) {
                                onSuccess()
                            } else {
                                snackbarChannel.trySend(
                                    UiText.DynamicString("${task.exception?.message}")
                                )
                            }
                        }
                } else {
                    snackbarChannel.trySend(UiText.DynamicString("${task.exception?.message}"))
                }
            }.await()
    }

    override suspend fun signOut() {
        auth.signOut()
    }

    override suspend fun deleteAccount() {
        auth.currentUser!!.delete().await()
    }

    override suspend fun setLanguageCode(languageCode: String) {
        auth.setLanguageCode(languageCode)
    }
}