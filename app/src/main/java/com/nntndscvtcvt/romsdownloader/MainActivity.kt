package com.nntndscvtcvt.romsdownloader

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.nntndscvtcvt.romsdownloader.presentation.navigation.Navigator
import com.nntndscvtcvt.romsdownloader.presentation.theme.RomsDownloaderTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RomsDownloaderTheme {
                Navigator()
            }
        }
    }
}
