package com.example.toolsfordriver.ui

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.toolsfordriver.common.LocaleManager
import com.example.toolsfordriver.ui.theme.ToolsForDriverTheme
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseFirestore.getInstance().clearPersistence()
        if (!hasRequiredPermissions()) {
            ActivityCompat.requestPermissions(
                this,
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU){
                    arrayOf(
                        android.Manifest.permission.CAMERA,
                        android.Manifest.permission.POST_NOTIFICATIONS
                    )
                } else arrayOf(android.Manifest.permission.CAMERA),
                0
            )

        }

        setContent {
            ToolsForDriverTheme {
                val windowSize = calculateWindowSizeClass(activity = this)
                TFDApp(windowSize = windowSize.widthSizeClass)
            }
        }
    }

    private fun hasRequiredPermissions(): Boolean {
        var permissionsGranted = ContextCompat.checkSelfPermission(
            applicationContext, android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            permissionsGranted = permissionsGranted &&
                    ContextCompat.checkSelfPermission(
                        applicationContext, android.Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
        }
        return permissionsGranted
    }

    override fun attachBaseContext(newBase: Context) {
        val savedLocale = LocaleManager.getSavedLocale(newBase)

        val context = LocaleManager.setLocale(newBase, savedLocale)
        super.attachBaseContext(context)
    }
}