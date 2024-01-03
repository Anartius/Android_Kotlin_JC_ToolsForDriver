package com.example.toolsfordriver.model

data class AppUser(
    val id: String? = null,
    val userId: String,
    val displayName: String,
    val avatarUri: String
) {
    fun toMap(): MutableMap<String, Any> = mutableMapOf(
        "userId" to this.userId,
        "display_name" to this.displayName,
        "avatar_uri" to this.avatarUri
    )
}