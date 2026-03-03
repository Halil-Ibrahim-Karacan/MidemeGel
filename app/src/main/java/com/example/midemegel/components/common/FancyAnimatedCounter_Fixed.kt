package com.example.midemegel.components.common

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.midemegel.helpers.utils.getResponsiveFontSize
import com.example.midemegel.helpers.utils.getScreenWidth
import kotlinx.coroutines.launch

/**
 * Ürün miktarını artırmak veya azaltmak için kullanılan animasyonlu bir sayaç bileşeni.
 * Sayı değişimlerinde yukarı/aşağı kayma animasyonu ve buton tıklamalarında zıplama efekti sunar.
 */
@Composable
fun FancyAnimatedCounter_Fixed(
    backgroundColor: Color,
    color: Color,
    buttonTextColor: Color,
    Count: Int,
    removeProduct: () -> Unit,
    addProduct: () -> Unit
) {
    // --- Mantıksal Değişkenler ve Animasyon Durumları ---
    
    // Eski sayıyı animasyon sırasında referans almak için tutar
    var oldCount by remember { mutableStateOf(Count) }

    // Buton tıklama animasyonları (ölçekleme)
    val plusScale = remember { Animatable(1f) }
    val minusScale = remember { Animatable(1f) }
    // Sayı değişimi geçiş animasyonu
    val transition = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    // Ekran boyutuna dayalı dinamik ölçüler
    val screenWidth = getScreenWidth()
    val counterSize = screenWidth * 0.05f 
    val fontSize = getResponsiveFontSize(0.036f)
    val spacing = screenWidth * 0.05f

    // Sayı değiştiğinde geçiş animasyonunu tetikle
    LaunchedEffect(Count) {
        transition.snapTo(0f)
        transition.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
        )
    }

    // Her render sonrası mevcut sayıyı 'eski sayı' olarak güncelle
    SideEffect { oldCount = Count }

    // --- Tasarım Bileşenleri ---
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacing),
        modifier = Modifier
            .wrapContentSize()
            .background(backgroundColor, RoundedCornerShape(50.dp))
            .padding(5.dp),
    ) {
        // Eksiltme (-) Butonu
        Box(
            modifier = Modifier
                .size(counterSize)
                .graphicsLayer(
                    scaleX = minusScale.value,
                    scaleY = minusScale.value,
                    shadowElevation = 12f,
                    shape = CircleShape,
                    clip = true
                )
                .background(color)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    if (Count > 1) {
                        removeProduct()
                        // Tıklama animasyonu
                        scope.launch {
                            minusScale.animateTo(0.85f, tween(120, easing = LinearOutSlowInEasing))
                            minusScale.animateTo(1f, spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow))
                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                "-", fontSize = fontSize, color = buttonTextColor,
                textAlign = TextAlign.Center, lineHeight = fontSize
            )
        }

        // Animasyonlu Sayı Göstergesi
        Box(
            modifier = Modifier.wrapContentSize(),
            contentAlignment = Alignment.Center
        ) {
            val direction = if (Count > oldCount) 1 else -1 // Artış veya azalış yönü
            
            // Eski Sayı (Kaybolarak uzaklaşır)
            Text(
                text = oldCount.toString(),
                fontSize = fontSize,
                fontWeight = FontWeight.Bold,
                color = Color.Black.copy(alpha = 0.6f),
                modifier = Modifier.graphicsLayer {
                    translationY = -direction * (1 - transition.value) * 20f
                    alpha = 1 - transition.value
                }
            )

            // Yeni Sayı (Gelerek belirir)
            Text(
                text = Count.toString(),
                fontSize = fontSize,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.graphicsLayer {
                    translationY = direction * (1 - transition.value) * 20f
                    alpha = transition.value
                }
            )
        }

        // Artırma (+) Butonu
        Box(
            modifier = Modifier
                .size(counterSize)
                .graphicsLayer(
                    scaleX = plusScale.value,
                    scaleY = plusScale.value,
                    shadowElevation = 12f,
                    shape = CircleShape,
                    clip = true
                )
                .background(color)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    addProduct()
                    // Tıklama animasyonu
                    scope.launch {
                        plusScale.animateTo(1.25f, tween(120, easing = LinearOutSlowInEasing))
                        plusScale.animateTo(1f, spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow))
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                "+", fontSize = fontSize, color = buttonTextColor,
                textAlign = TextAlign.Center, lineHeight = fontSize
            )
        }
    }
}
