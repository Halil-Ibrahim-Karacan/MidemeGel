package com.example.midemegel.components.main

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * Ana ekranın en üstünde yer alan başlık çubuğu (Header).
 * Düzenleme moduna geçiş butonu, uygulama ismi ve tema değiştirme anahtarını içerir.
 */
@Composable
fun MainHeader(
    darkTheme: Boolean,
    themeTextColor: Color,
    topRowHeight: Dp,
    iconSize: Dp,
    titleFontSize: TextUnit,
    onEditClick: () -> Unit
) {
    // --- Mantıksal Değişkenler ve Animasyonlar ---
    
    val scope = rememberCoroutineScope()
    // Liste (düzenleme) ikonunun tıklama animasyonu için scale değeri
    val listIconScale = remember { Animatable(1f) }

    // --- Tasarım Bileşenleri ---
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(topRowHeight),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Düzenleme Sayfasına Giriş Butonu
        Icon(
            imageVector = Icons.Default.List,
            contentDescription = "Düzenle",
            tint = themeTextColor,
            modifier = Modifier
                .size(iconSize)
                .background(if (!darkTheme) Color(0xFFE5E5E5) else Color(0xFF827AC0), RoundedCornerShape(10))
                .border(1.dp, themeTextColor, RoundedCornerShape(10))
                .graphicsLayer(
                    scaleX = listIconScale.value,
                    scaleY = listIconScale.value
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {
                        // Tıklama anında ikonun küçülüp büyümesi (zıplama efekti)
                        scope.launch {
                            listIconScale.animateTo(0.85f, tween(120, easing = LinearOutSlowInEasing))
                            listIconScale.animateTo(1f, spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow))
                            onEditClick()
                        }
                    }
                )
        )

        // Uygulama İsmi (Merkezi Başlık)
        Text(
            text = "MidemeGel",
            color = themeTextColor,
            fontSize = titleFontSize,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily.Cursive,
            modifier = Modifier.weight(1f) // Metni merkeze itmek için boşluğu kullanır
        )

        // Tema Değiştirme Anahtarı (Switch)
        ThemeSwitch(darkThemeChanged = { })
    }
}
