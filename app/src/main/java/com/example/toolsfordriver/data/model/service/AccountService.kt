package com.example.toolsfordriver.data.model.service

import com.example.toolsfordriver.data.model.User
import kotlinx.coroutines.flow.Flow

interface AccountService {
    val hasUser: Boolean
    val currentUser: Flow<User>
    val currentUserId: String

    suspend fun createAccount(email: String, password: String)
    suspend fun authenticate(email: String, password: String)
    suspend fun signOut()
    suspend fun deleteAccount()
}