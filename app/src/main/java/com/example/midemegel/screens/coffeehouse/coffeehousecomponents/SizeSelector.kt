package com.example.midemegel.screens.coffeehouse.coffeehousecomponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.example.midemegel.helpers.enums.SizeEnum

/**
 * Kahvehane ekranında içecek boyutunun (Küçük, Orta, Büyük) seçilmesini sağlayan kontrol bileşeni.
 * Her bir boyut seçeneği, bardağın ikonunu ve boyut ismini barındırır.
 */
@Composable
fun SizeSelector(
    selectedSize: SizeEnum,
    onSizeSelected: (SizeEnum) -> Unit,
    currentColor: Color,
    itemBackgroundColorUnselected: Color,
    contrastTextColor: Color,
    itemTextColorUnselected: Color,
    maskResId: Int, // Bardak türüne (çay/kahve) göre değişen ikon
    iconSize: Dp,
    fontSize: TextUnit
) {
    // --- Tasarım Bileşenleri ---
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Tüm boyut seçeneklerini döngü ile listele
        SizeEnum.entries.forEach { size ->
            val isSelected = size == selectedSize
            // Seçili olma durumuna göre renkleri belirle
            val backgroundColor = if (isSelected) currentColor else itemBackgroundColorUnselected
            val textColor = if (isSelected) contrastTextColor else itemTextColorUnselected

            Row(
                modifier = Modifier
                    .wrapContentSize()
                    .background(
                        color = backgroundColor,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onSizeSelected(size) }
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // Boyutu temsil eden minik bardak ikonu
                Image(
                    painter = painterResource(id = maskResId),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(textColor),
                    modifier = Modifier
                        .size(iconSize)
                        .padding(end = 2.dp)
                )
                // Boyut ismi (Küçük, Orta, Büyük)
                Text(
                    text = size.sizeName,
                    color = textColor,
                    fontSize = fontSize,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}
