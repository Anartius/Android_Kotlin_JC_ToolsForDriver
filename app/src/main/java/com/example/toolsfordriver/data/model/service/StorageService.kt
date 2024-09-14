package com.example.toolsfordriver.data.model.service

import android.net.Uri

interface StorageService {
    suspend fun saveImage(fileUri: Uri, isAvatar: Boolean): String
    suspend fun getFile(fileUri: Uri)
    suspend fun deleteFile(fileUri: Uri): Boolean
}