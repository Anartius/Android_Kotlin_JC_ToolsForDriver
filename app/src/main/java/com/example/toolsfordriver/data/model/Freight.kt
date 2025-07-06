package com.example.toolsfordriver.data.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Freight(
    @DocumentId val id: String = "",
    @ServerTimestamp val createdAt: Date = Date(),
    val userId: String = "",
    val loads: Map<String, String> = emptyMap(),
    val firstLoadTime: Date? = null,
    val unloads: Map<String, String> = emptyMap(),
    val lastUnloadTime: Date? = null,
    val distance: Int? = null,
    val imagesUriList: List<String> = emptyList(),
    val note: String? = null
)