package com.example.midemegel.di

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Hilt bağımlılık enjeksiyonunun (Dependency Injection) başlatıldığı uygulama sınıfı.
 */
@HiltAndroidApp
class HiltApplication: Application() {
}