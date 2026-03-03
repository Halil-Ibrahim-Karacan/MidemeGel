package com.example.midemegel.screens.coffeehouse.coffeehousecomponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.midemegel.R

/**
 * Kahvehane ekranında kullanılan interaktif bardak bileşeni.
 * Seçili olma durumuna göre dolum animasyonu ve damlama efekti gösterir.
 */
@Composable
fun CoffeeCup(
    resId: Int,
    maskResId: Int,
    isSelected: Boolean,
    currentColor: Color,
    fillProgress: Float, // Bardak doluluk oranı (0.0 - 1.0)
    dropsAlpha: Float, // Damlama efekti şeffaflığı
    cupBaseSizeDp: Dp,
    animatedCupSize: Dp,
    standardCupSize: Dp,
    extraHeight: Dp,
    onClick: () -> Unit
) {
    // --- Tasarım Bileşenleri ---
    Box(
        modifier = Modifier
            .width(cupBaseSizeDp)
            .height(cupBaseSizeDp + extraHeight)
            // Seçili olmayan bardakları hafif şeffaf yapar
            .alpha(if (isSelected) 1f else 0.5f)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onClick()
            },
        contentAlignment = Alignment.BottomCenter
    ) {
        // Eğer bardak seçiliyse, üstten gelen dolum damlalarını göster
        if (isSelected) {
            Image(
                painter = painterResource(id = R.drawable.filling_drops_icon),
                contentDescription = null,
                modifier = Modifier
                    .width(10.dp)
                    .height(45.dp)
                    .alpha(dropsAlpha)
                    .background(color = currentColor)
                    .align(Alignment.TopCenter)
            )
        }

        // Bardak gövdesi
        Box(
            modifier = Modifier
                .size(if (isSelected) animatedCupSize else standardCupSize)
        ) {
            // Maske katmanı: İçeceğin bardak sınırları içinde dolmasını sağlar
            Image(
                painter = painterResource(id = maskResId),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
                    .drawWithContent {
                        drawContent()
                        if (isSelected) {
                            // İçecek rengini bardağın içine "maskeleyerek" çizer
                            val maxFillRatio = 0.85f // Bardağın taşmaması için maksimum doluluk
                            val currentHeight = size.height * fillProgress * maxFillRatio
                            drawRect(
                                color = currentColor,
                                topLeft = Offset(0f, size.height - currentHeight),
                                size = Size(size.width, currentHeight),
                                blendMode = BlendMode.SrcAtop // Sadece bardağın olduğu yerleri boya
                            )
                        }
                    }
            )

            // Bardağın dış hatları ve detaylarını içeren ana görsel
            Image(
                painter = painterResource(id = resId),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
