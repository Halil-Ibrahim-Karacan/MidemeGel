package com.example.midemegel.screens.editactivities.edit.editcomponents

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.example.midemegel.R
import kotlinx.coroutines.launch

/**
 * Düzenleme ekranlarında (Ürün veya Kategori düzenleme) kullanılan üst başlık çubuğu.
 * Geri butonu ve "Düzenle" başlığını içerir.
 */
@Composable
fun EditHeader(
    themeTextColor: Color,
    headerHeight: Dp,
    iconSize: Dp,
    titleFontSize: TextUnit,
    onBackClick: () -> Unit
) {
    // --- Mantıksal Değişkenler ve Animasyonlar ---
    
    val scope = rememberCoroutineScope()
    // Geri butonunun tıklanma animasyonu için ölçekleme değeri
    val backIconScale = remember { Animatable(1f) }

    // --- Tasarım Bileşenleri ---
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(headerHeight)
            .padding(top = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        // Geri Butonu (Ok İkonu)
        Image(
            painter = painterResource(id = R.drawable.arror_wight_icon),
            contentDescription = "Geri",
            colorFilter = ColorFilter.tint(themeTextColor),
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 16.dp)
                .size(iconSize)
                .rotate(180f) // Oku geri yönüne çevirir
                .graphicsLayer(
                    scaleX = backIconScale.value,
                    scaleY = backIconScale.value
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {
                        // Tıklama animasyonu (küçülüp geri sıçrama)
                        scope.launch {
                            backIconScale.animateTo(0.85f, tween(120, easing = LinearOutSlowInEasing))
                            backIconScale.animateTo(1f, spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow))
                            onBackClick()
                        }
                    }
                )
        )

        // Sayfa Başlığı (İkon + "Düzenle" Metni)
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.edit_icon),
                contentDescription = "Düzenle İkonu",
                tint = themeTextColor,
                modifier = Modifier.size(iconSize)
            )

            Spacer(modifier = Modifier.size(12.dp))

            Text(
                text = "Düzenle",
                color = themeTextColor,
                fontSize = titleFontSize,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily.Serif,
            )
        }
    }
}
