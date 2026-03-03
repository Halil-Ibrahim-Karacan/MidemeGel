package com.example.midemegel.screens.editactivities.edit.editcomponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.midemegel.R
import com.example.midemegel.helpers.asPainter
import com.example.midemegel.helpers.toComposeColor
import com.example.midemegel.helpers.utils.getContrastTextColor
import com.example.midemegel.data.model.ProductModel
import kotlin.math.roundToInt

/**
 * Düzenleme ekranındaki ürün listesinde her bir ürünü temsil eden satır tasarımı.
 * Ürünün görselini, ismini ve düzenleme/silme butonlarını içerir. 
 * Ayrıca sürükle-bırak (drag-and-drop) desteği için ofset ve sürükleme ikonu barındırır.
 */
@Composable
fun EditProductItem(
    product: ProductModel,
    isDragged: Boolean, // Öğenin şu an sürüklenip sürüklenmediği
    offsetAnim: Float, // Sürükleme sırasındaki dikey konum ofseti
    themeTextColor: Color,
    listItemHeight: Dp,
    listItemIconSize: Dp,
    categoryTextSize: TextUnit,
    iconSize: Dp,
    onDragModifier: Modifier, // Sürükleme işlemini tetikleyen modifier
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    // --- Mantıksal Değişkenler ve Renk Hesaplamaları ---
    
    // Ürünün kendi rengi ve üzerine gelecek metin için kontrast renk
    val currentColor = product.color.toComposeColor()
    val contrastTextColor = getContrastTextColor(product)

    // --- Tasarım Bileşenleri ---
    Row(
        modifier = Modifier
            .fillMaxWidth()
            // Sürükleme ofsetini uygular
            .offset { IntOffset(0, offsetAnim.roundToInt()) }
            .background(
                // Sürüklenirken hafif şeffaflık efekti verir
                color = if (isDragged) currentColor.copy(alpha = 0.8f) else currentColor,
                shape = RoundedCornerShape(10.dp)
            )
            .border(
                width = 2.dp,
                color = themeTextColor,
                shape = RoundedCornerShape(10.dp)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Sürükleme İkonu (Drag Handle)
        Image(
            painter = painterResource(id = R.drawable.drag_drop_icon),
            contentDescription = "Sürükle",
            modifier = onDragModifier
                .wrapContentWidth()
                .height(listItemHeight)
                .padding(5.dp),
            colorFilter = ColorFilter.tint(contrastTextColor)
        )

        // Ürün Görseli (Arka planına hafif bir parlama efekti ile)
        Image(
            painter = product.image.asPainter(),
            contentDescription = null,
            modifier = Modifier
                .size(listItemIconSize)
                .padding(5.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            if (isDragged) Color.White.copy(alpha = 0.8f) else Color.White,
                            Color.Transparent
                        )
                    )
                )
        )

        // Ürün İsmi
        Text(
            text = product.name,
            color = contrastTextColor,
            modifier = Modifier
                .fillMaxWidth()
                .height((categoryTextSize.value * 4).dp)
                .padding(5.dp)
                .weight(1f),
            fontSize = categoryTextSize,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            lineHeight = (categoryTextSize.value * 1.5f).sp
        )

        // Düzenleme Butonu (Kalem İkonu)
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = "Düzenle",
            tint = contrastTextColor,
            modifier = Modifier
                .padding(8.dp)
                .size(iconSize * 0.8f)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onEditClick
                )
        )

        // Silme Butonu (Çöp Kutusu İkonu)
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Sil",
            tint = contrastTextColor,
            modifier = Modifier
                .padding(8.dp)
                .size(iconSize * 0.8f)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onDeleteClick
                )
        )
    }
}
