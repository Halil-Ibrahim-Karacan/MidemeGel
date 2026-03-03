package com.example.midemegel.screens.product.productitem.productitemcomponents

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.example.midemegel.R
import kotlinx.coroutines.launch

/**
 * Ürün kartı üzerinde yer alan favori (kalp) ikonu bileşeni.
 * Tıklandığında animasyonlu bir şekilde favori durumunu değiştirir.
 */
@Composable
fun ProductCardFavorite(
    isFavorite: Boolean,
    tintColor: Color,
    iconSize: Dp,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // --- Mantıksal Değişkenler ve Animasyonlar ---
    
    val scope = rememberCoroutineScope()
    // Tıklama anında ikonun küçülüp büyümesi için ölçekleme değeri
    val scale = remember { Animatable(1f) }
    // Mevcut favori durumuna göre uygun ikon kaynağını belirle
    val favoriteIcon = if (isFavorite) R.drawable.favorite_selected_icon else R.drawable.favorite_unselected_icon

    // --- Tasarım Bileşenleri ---
    Image(
        painter = painterResource(id = favoriteIcon),
        contentDescription = "Favori",
        modifier = modifier
            .size(iconSize)
            .graphicsLayer(
                scaleX = scale.value,
                scaleY = scale.value
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                // Tıklama animasyonunu başlat ve callback fonksiyonunu çağır
                scope.launch {
                    scale.animateTo(0.85f, tween(120))
                    scale.animateTo(1f, spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow))
                }
                onFavoriteClick()
            },
        colorFilter = ColorFilter.tint(color = tintColor)
    )
}
