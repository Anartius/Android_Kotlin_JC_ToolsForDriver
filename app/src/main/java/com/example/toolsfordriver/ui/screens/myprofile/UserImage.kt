package com.example.toolsfordriver.ui.screens.myprofile

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Cached
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.toolsfordriver.R
import com.example.toolsfordriver.data.model.User
import com.example.toolsfordriver.ui.common.dialogs.ImageSourcePickerDialog

@Composable
fun UserImage(
    currentUser: User,
    viewModel: MyProfileViewModel
) {
    val context = LocalContext.current

    val showSourcePickerDialog = rememberSaveable {
        mutableStateOf(false)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            viewModel.saveAvatarToCloud(
                uri = uri,
                user = currentUser,
                context = context
            )
            showSourcePickerDialog.value = false
        }
    }

    Box(contentAlignment = Alignment.BottomEnd) {
        Card(
            modifier = Modifier
                .padding(horizontal = 25.dp, vertical = 12.dp)
                .wrapContentSize(),
            shape = RoundedCornerShape(50),
            colors = CardDefaults.cardColors(Color.Transparent)
        ) {
            val photoUri = remember(currentUser.avatarUri) {
                mutableStateOf(currentUser.avatarUri)
            }

            SubcomposeAsyncImage(
                model = ImageRequest.Builder(context)
                    .data(photoUri.value)
                    .crossfade(true)
                    .build(),
                loading = {
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = colorResource(id = R.color.light_blue))
                    }
                },
                error = { painterResource(id = R.drawable.ic_account_circle) },
                contentDescription = stringResource(R.string.user_profile_image),
                onError = { error ->  Log.e("Coil_img", error.result.throwable.toString()) },
                modifier = Modifier
                    .size(150.dp)
                    .clickable(
                        enabled = true,
                        onClick = {
                            if (currentUser.avatarUri.isNotEmpty()) {
                                viewModel.showAvatarImage(true)
                            } else viewModel.showCamera(true)
                        }
                    )
                    .clip(RoundedCornerShape(corner = CornerSize(5.dp))),
                contentScale = ContentScale.Crop
            )
        }

        IconButton(
            modifier = Modifier.padding(end = 4.dp),
            onClick = {
                showSourcePickerDialog.value = true
            }
        ) {

            val hasAvatar by remember(currentUser.avatarUri) {
                mutableStateOf(currentUser.avatarUri.isNotEmpty())
            }

            Icon(
                imageVector = if (hasAvatar) {
                    Icons.Filled.Cached
                } else Icons.Filled.AddAPhoto,
                contentDescription = if (hasAvatar) {
                    stringResource(R.string.update_user_profile_photo)
                } else stringResource(R.string.add_user_profile_photo),
                tint = colorResource(id = R.color.light_blue)
            )
        }
    }

    if (showSourcePickerDialog.value) {
        ImageSourcePickerDialog(
            showDialog = showSourcePickerDialog,
            showCamera = { viewModel.showCamera(true) },
            showGallery = { launcher.launch("image/*") }
        )
    }
}