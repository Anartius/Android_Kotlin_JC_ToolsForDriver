package com.example.toolsfordriver.data.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Trip(
    @DocumentId val id: String = "",
    @ServerTimestamp val createdAt: Date = Date(),
    val userId: String = "",
    val startTime: Date? = null,
    val endTime: Date? = null,
    val hourlyPayment: Boolean = false
)