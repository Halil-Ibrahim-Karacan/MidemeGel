package com.example.midemegel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.midemegel.ui.theme.MidemeGelTheme
import com.example.midemegel.data.viewmodel.ThemeViewModel
import com.example.midemegel.helpers.Transitions
import dagger.hilt.android.AndroidEntryPoint

/**
 * Uygulamanın ana giriş noktası olan Activity.
 * Tema yönetimi ve ana ekran geçişlerini koordine eder.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // --- Mantıksal Değişkenler ve Durumlar ---
            val themeViewModel: ThemeViewModel = hiltViewModel()
            val darkTheme by themeViewModel.selectedTheme.collectAsState() // Mevcut tema durumunu takip eder
            
            // --- Tasarım Bileşenleri ---
            MidemeGelTheme(darkTheme = darkTheme) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    content = { innerPadding ->
                        Transitions(
                            innerPadding,
                            darkTheme
                        ) // Ekranlar arası geçişleri yöneten yapı
                    }
                )
            }
        }
    }
}


