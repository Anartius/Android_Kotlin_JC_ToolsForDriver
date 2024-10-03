package com.example.toolsfordriver.ui.screens.myprofile

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.toolsfordriver.R
import com.example.toolsfordriver.common.LocaleManager
import com.example.toolsfordriver.common.UiText
import com.example.toolsfordriver.common.saveBitmapToInternalStorage
import com.example.toolsfordriver.common.saveImageToInternalStorage
import com.example.toolsfordriver.data.model.User
import com.example.toolsfordriver.data.model.service.AccountService
import com.example.toolsfordriver.data.model.service.FirestoreService
import com.example.toolsfordriver.data.model.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(
    private val accountService: AccountService,
    private val firestoreService: FirestoreService,
    private val storageService: StorageService
) : ViewModel() {

    private val snackbarChannel = Channel<UiText>()
    val snackbarMessages = snackbarChannel.receiveAsFlow()
    private val _uiState = MutableStateFlow(MyProfileUiState())
    val uiState = _uiState.asStateFlow()
    val userId = accountService.currentUserId
    val users = firestoreService.users

    fun updateUser(user: User) = launchCatching { firestoreService.updateUser(user) }

    fun updateLocale(locale: Locale, context: Context) {
        LocaleManager.saveLocale(context, locale)
        val updatedLocale = LocaleManager.getSavedLocale(context)
        LocaleManager.setLocale(context, updatedLocale)
        (context as Activity).recreate()
    }

    fun saveAvatarToCloud(
        uri: Uri? = null,
        bitmap: Bitmap? = null,
        user: User,
        context: Context
    ) = launchCatching {

        _uiState.value = _uiState.value.copy(
            temporaryImageUri = if (uri != null) {
                saveImageToInternalStorage(uri, context)
            } else if (bitmap != null) {
                saveBitmapToInternalStorage(bitmap, context)
            } else null
        )

        if (_uiState.value.temporaryImageUri == null) {
            snackbarChannel.send(
                UiText.DynamicString(context.getString(R.string.image_was_not_saved))
            )
            return@launchCatching
        } else {
            val downloadUri = storageService.saveImage(
                fileUri = _uiState.value.temporaryImageUri!!,
                isAvatar = true
            )
            updateUser(user.copy(avatarUri = downloadUri))
            _uiState.value = _uiState.value.copy(temporaryImageUri = null)
        }
    }

    fun updateCurrentUser (user: User) {
        _uiState.value = _uiState.value.copy(user = user)
    }

    fun showCamera(value: Boolean) {
        _uiState.value = _uiState.value.copy(showCamera = value)
    }

    fun showAvatarImage(value: Boolean) {
        _uiState.value = _uiState.value.copy(showZoomableImageDialog = value)
    }

    fun showSelectLocaleDialog(value: Boolean) {
        _uiState.value = _uiState.value.copy(showSelectLocaleDialog = value)
    }

    fun showSignOutDialog(value: Boolean) {
        _uiState.value = _uiState.value.copy(showSignOutDialog = value)
    }

    fun onSignOutClick(onSignOutClicked: () -> Unit) = launchCatching {
        accountService.signOut()
        onSignOutClicked()
    }

    private fun launchCatching(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(
            CoroutineExceptionHandler{ _, throwable ->
                viewModelScope.launch {
                    Log.e("Firestore", throwable.message.toString())
                    snackbarChannel.send(UiText.DynamicString(throwable.message.toString()))
                }
            },
            block = block
        )
    }
}

