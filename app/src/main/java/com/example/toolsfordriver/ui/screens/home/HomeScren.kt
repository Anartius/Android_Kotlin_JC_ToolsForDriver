package com.example.toolsfordriver.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.toolsfordriver.R
import com.example.toolsfordriver.ui.common.buttons.AppButton
import com.example.toolsfordriver.ui.common.TFDAppBar

@Composable
fun HomeScreen(
    onNavigateToTripsScreen: () -> Unit,
    onNavigateToFreightsScreen: () -> Unit,
    onNavigateToMyProfileScreen: () -> Unit
) {
    val viewModel: HomeViewModel = hiltViewModel()
    val context = LocalContext.current
    val users = viewModel.users.collectAsStateWithLifecycle(initialValue = emptyList()).value
    val currentUser = viewModel.homeUiState.collectAsStateWithLifecycle().value.user

    LaunchedEffect(key1 = users) {
        if (users.isNotEmpty()) {
            viewModel.updateCurrentUser(users.first())
        }
    }

    Scaffold(
        topBar = {
            TFDAppBar(
                title = stringResource(R.string.tfd_app),
                avatarAction = {
                    IconButton(
                        onClick = { onNavigateToMyProfileScreen() },
                        modifier = Modifier.padding(horizontal = 4.dp)
                    ) {
                        if (currentUser != null && currentUser.avatarUri.isNotEmpty()) {
                            SubcomposeAsyncImage(
                                model = ImageRequest.Builder(context)
                                    .data(currentUser.avatarUri)
                                    .crossfade(true)
                                    .build(),
                                loading = {
                                    Box(contentAlignment = Alignment.Center) {
                                        CircularProgressIndicator(
                                            color = colorResource(id = R.color.light_blue)
                                        )
                                    }
                                },
                                error = { painterResource(id = R.drawable.ic_account_circle) },
                                contentDescription = stringResource(R.string.user_profile_image),
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(RoundedCornerShape(corner = CornerSize(50))),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(painter = painterResource(
                                id = R.drawable.ic_account_circle),
                                contentDescription = stringResource(
                                    id = R.string.user_profile_image
                                )
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValue ->
        Surface(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValue)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AppButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 0.dp, vertical = 8.dp),
                    buttonText = stringResource(id = R.string.trips)
                ) {
                    onNavigateToTripsScreen()
                }
                AppButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 0.dp, vertical = 8.dp),
                    buttonText = stringResource(id = R.string.freights)
                ) {
                    onNavigateToFreightsScreen()
                }
            }
        }
    }
}