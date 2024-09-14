package com.example.toolsfordriver.data.model.service.impl

import android.net.Uri
import android.util.Log
import com.example.toolsfordriver.data.model.service.StorageService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.File
import javax.inject.Inject

class StorageServiceImpl @Inject constructor(
    private val auth: FirebaseAuth,
    storage: FirebaseStorage
) : StorageService {
    private val userId = if (auth.currentUser != null) {
        "/" + auth.currentUser!!.uid
    } else ""

    private val storageRef = storage.reference

    override suspend fun saveImage(fileUri: Uri, isAvatar: Boolean): String {
        val file = fileUri.path?.let { File(it) }
        var cloudUri = ""

        val storageImageRef = storageRef.child(
            "$IMAGE_COLLECTION$userId/"
                    + (if (isAvatar) "avatar/" else "")
                    + file?.name
        )

        storageImageRef.run {
            if (isAvatar) { this.parent?.listAll()?.await()?.items?.forEach { it.delete() } }

            this.putFile(fileUri).await()

            this.downloadUrl.addOnCompleteListener { task ->
                cloudUri = task.result.toString()
            }.await()

            if (file != null) {
                if (file.exists()) {
                    file.delete()
                    Log.e("FILE", "Local file deleted successfully")
                } else Log.e("FILE", "Local file don't found")
            }

        }

        return cloudUri
    }

    override suspend fun getFile(fileUri: Uri) {
        auth.currentUser?.let { user -> storageRef.child(user.uid).getFile(fileUri) }
    }

    override suspend fun deleteFile(fileUri: Uri): Boolean {
        var deletedSuccessfully = false
        fileUri.path?.let {
            val fileName = File(it).name
            auth.currentUser?.let {
                storageRef.child("$IMAGE_COLLECTION$userId/")
                    .child(fileName)
                    .delete()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("tag", "referenceDelete: success")
                            deletedSuccessfully = true
                        } else {
                            Log.d("tag", "referenceDelete: failed");
                        }
                    }.await()
            }
        }

        return deletedSuccessfully
    }

    companion object { private const val IMAGE_COLLECTION = "images" }
}