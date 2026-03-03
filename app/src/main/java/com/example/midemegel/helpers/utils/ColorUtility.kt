package com.example.midemegel.helpers.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.example.midemegel.R
import com.example.midemegel.helpers.isBright
import com.example.midemegel.helpers.toComposeColor
import com.example.midemegel.data.model.IColorable

/**
 * Mevcut tema durumuna göre arka plan rengini döndürür.
 * @param darkTheme Karanlık tema aktif mi?
 */
@Composable
fun getThemeBackgroundColor(darkTheme: Boolean) = colorResource(
    id = if (!darkTheme) R.color.background_color else R.color.background_color_dark
)

/**
 * Mevcut tema durumuna göre metin rengini döndürür.
 * @param darkTheme Karanlık tema aktif mi?
 */
fun getThemeTextColor(darkTheme: Boolean) = if (!darkTheme) Color.Black else Color.White

/**
 * Mevcut tema durumuna göre zıt metin rengini döndürür.
 * @param darkTheme Karanlık tema aktif mi?
 */
fun getThemeInverseTextColor(darkTheme: Boolean) = if (!darkTheme) Color.White else Color.Black

/**
 * Verilen öğenin rengine göre en iyi okunabilirliği sağlayan metin rengini (siyah/beyaz) döndürür.
 * @param item Renk bilgisi içeren öğe.
 */
@Composable
fun getContrastTextColor(item: IColorable) = if (item.color.toComposeColor().isBright()) Color.Black else Color.White

/**
 * Mevcut tema durumuna göre metin giriş alanı (TextField) arka plan rengini döndürür.
 * @param darkTheme Karanlık tema aktif mi?
 */
fun getTextFieldBackgroundColor(darkTheme: Boolean) = if (!darkTheme) Color(0xFFFFFFFF) else Color(0xFF827AC0)

/**
 * Mevcut tema durumuna göre buton arka plan rengini döndürür.
 * @param darkTheme Karanlık tema aktif mi?
 */
fun getButtonBackgroundColor(darkTheme: Boolean) = if (!darkTheme) Color(0xFF000000) else Color(0xFF1C1556)

/**
 * Mevcut tema durumuna göre buton metin rengini döndürür.
 * @param darkTheme Karanlık tema aktif mi?
 */
fun getButtonTextColor(darkTheme: Boolean) = if (!darkTheme) Color(0xFFE5E5E5) else Color(0xFF827AC0)

/**
 * Mevcut tema durumuna göre diyalog arka plan rengini döndürür.
 * @param darkTheme Karanlık tema aktif mi?
 */
fun getDialogBackgroundColor(darkTheme: Boolean) = if (!darkTheme) Color(0xFFFFFFFF) else Color(0xFF827AC0)
