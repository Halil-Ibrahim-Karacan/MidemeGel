package com.example.midemegel.screens.product.productitem.productitemcomponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.midemegel.R

/**
 * Ürün kartı üzerinde ürünün puanını (rating) temsil eden küçük bileşen.
 * İki katmanlı yıldız ikonu ile gölge efekti verilmiş bir görünüm sunar.
 */
@Composable
fun ProductCardRating(
    rating: Int,
    textColor: Color,
    iconSize: Dp,
    modifier: Modifier = Modifier
) {
    // --- Tasarım Bileşenleri ---
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            // Yıldız İkonu - Gölge Katmanı (Siyah, hafif kaydırılmış)
            Image(
                painter = painterResource(id = R.drawable.star_icon),
                contentDescription = null,
                colorFilter = ColorFilter.tint(Color.Black),
                modifier = Modifier.size(iconSize).offset(x = 2.dp, y = 2.dp)
            )
            // Yıldız İkonu - Ana Katman (Sarı)
            Image(
                painter = painterResource(id = R.drawable.star_icon),
                contentDescription = null,
                colorFilter = ColorFilter.tint(Color.Yellow),
                modifier = Modifier.size(iconSize)
            )
        }
        // Puan Değeri Metni
        Text(
            text = "$rating",
            color = textColor
        )
    }
}
