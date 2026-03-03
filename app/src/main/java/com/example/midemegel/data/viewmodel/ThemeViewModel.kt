package com.example.midemegel.data.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * Uygulamanın tema (koyu/açık mod) durumunu yöneten ViewModel.
 */
@HiltViewModel
class ThemeViewModel @Inject constructor() : ViewModel(){
    // Seçili tema durumunu tutan akış (true: koyu tema, false: açık tema)
    private val _selectedTheme = MutableStateFlow<Boolean>(false)
    val selectedTheme: StateFlow<Boolean> = _selectedTheme.asStateFlow()

    /**
     * Uygulamanın temasını günceller.
     * @param darkTheme Yeni tema durumu.
     */
    fun setDarkTheme(darkTheme: Boolean){
        _selectedTheme.value = darkTheme
    }

}