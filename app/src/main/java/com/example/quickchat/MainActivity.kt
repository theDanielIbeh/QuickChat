package com.example.quickchat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.quickchat.ui.theme.QuickChatTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        var isKeepOnScreen = true
        splashScreen.setKeepOnScreenCondition { isKeepOnScreen }
        enableEdgeToEdge()
        lifecycleScope.launch {
            delay(1000)
            isKeepOnScreen = false
        }
        setContent {
            QuickChatTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    QuickChatApp()
                }
            }
        }
    }
}