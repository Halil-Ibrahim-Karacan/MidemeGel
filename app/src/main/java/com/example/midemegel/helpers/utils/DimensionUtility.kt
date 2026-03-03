package com.example.midemegel.helpers.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Mevcut ekranın genişliğini dp cinsinden döndürür.
 */
@Composable
fun getScreenWidth(): Dp = LocalConfiguration.current.screenWidthDp.dp

/**
 * Ekran genişliğine göre duyarlı (responsive) bir yazı tipi boyutu hesaplar.
 * @param ratio Ekran genişliğine uygulanacak oran.
 */
@Composable
fun getResponsiveFontSize(ratio: Float): TextUnit = (getScreenWidth().value * ratio).sp

/**
 * Başlıklar için duyarlı yazı tipi boyutu döndürür.
 */
@Composable
fun getTitleFontSize() = getResponsiveFontSize(0.05f)

/**
 * Gövde metinleri için duyarlı yazı tipi boyutu döndürür.
 */
@Composable
fun getBodyFontSize() = getResponsiveFontSize(0.038f)

/**
 * Buton metinleri için duyarlı yazı tipi boyutu döndürür.
 */
@Composable
fun getButtonFontSize() = getResponsiveFontSize(0.038f)

/**
 * Etiketler (tag) için duyarlı yazı tipi boyutu döndürür.
 */
@Composable
fun getTagFontSize() = getResponsiveFontSize(0.035f)

/**
 * Ürün resimleri için ekran genişliğine göre orantılı boyut döndürür.
 */
@Composable
fun getProductImageSize() = getScreenWidth() * 0.5f

/**
 * Üst bardaki ikonlar için ekran genişliğine göre orantılı boyut döndürür.
 */
@Composable
fun getTopIconSize() = getScreenWidth() * 0.10f
