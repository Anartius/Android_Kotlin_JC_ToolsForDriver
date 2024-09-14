package com.example.toolsfordriver.ui

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.toolsfordriver.ui.theme.ToolsForDriverTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!hasRequiredPermissions()) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.CAMERA),
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
        return ContextCompat.checkSelfPermission(
            applicationContext, android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }
}