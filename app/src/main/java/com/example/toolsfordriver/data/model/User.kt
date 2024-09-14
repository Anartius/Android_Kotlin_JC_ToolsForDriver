package com.example.toolsfordriver.data.model

import com.google.firebase.firestore.DocumentId

data class User(
    @DocumentId val id: String = "",
    val userId: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val avatarUri: String = "",
    val paymentPerDay: Double = 400.0,
    val paymentPerHour: Double = 26.0
)
