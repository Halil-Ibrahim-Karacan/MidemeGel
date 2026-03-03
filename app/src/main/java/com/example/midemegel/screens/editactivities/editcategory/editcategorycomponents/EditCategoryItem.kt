package com.example.midemegel.screens.editactivities.editcategory.editcategorycomponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.example.midemegel.R
import com.example.midemegel.helpers.asPainter
import com.example.midemegel.data.model.CategoryModel
import kotlin.math.roundToInt

/**
 * Kategori düzenleme listesindeki her bir kategori öğesini temsil eden tasarım.
 * Sürükle-bırak desteği, düzenleme ve silme butonlarını içerir.
 */
@Composable
fun EditCategoryItem(
    // --- Mantıksal Değişkenler ve Parametreler ---
    category: CategoryModel, // Gösterilecek kategori verisi
    isDragged: Boolean, // Öğenin şu an sürüklenip sürüklenmediği durumu
    offsetAnim: Float, // Sürükleme sırasındaki dikey kayma miktarı
    itemBackgroundColor: Color, // Öğenin arka plan rengi
    itemTextColor: Color, // Metin ve ikonların rengi
    listItemHeight: Dp, // Satır yüksekliği
    listItemTextSize: TextUnit, // Kategori ismi yazı boyutu
    iconSize: Dp, // Buton ikonlarının boyutu
    onDragModifier: Modifier, // Sürükleme işlemini tetikleyen modifier
    onEditClick: () -> Unit, // Düzenle butonuna tıklama aksiyonu
    onDeleteClick: () -> Unit, // Silme butonuna tıklama aksiyonu
    showDelete: Boolean // Silme butonunun gösterilip gösterilmeyeceği
) {
    // --- Tasarım Bileşenleri ---
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            // Sürükleme sırasındaki dikey konumlamayı ayarlar
            .offset { IntOffset(0, offsetAnim.roundToInt()) }
            .background(
                // Sürüklenirken öğeyi hafif şeffaf yaparak görsel geri bildirim verir
                if (isDragged) itemBackgroundColor.copy(alpha = 0.8f) else itemBackgroundColor,
                RoundedCornerShape(12.dp)
            )
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Sürükleme tutamacı (Drag handle)
        Image(
            painter = painterResource(id = R.drawable.drag_drop_icon),
            contentDescription = "Sürükle",
            modifier = onDragModifier
                .wrapContentWidth()
                .height(listItemHeight)
                .padding(5.dp),
            colorFilter = ColorFilter.tint(itemTextColor)
        )

        // Kategori görseli
        Image(
            painter = category.image.asPainter(),
            contentDescription = null,
            modifier = Modifier
                .size(listItemHeight)
                .padding(5.dp)
        )

        // Kategori ismi
        Text(
            text = category.name,
            color = itemTextColor,
            fontSize = listItemTextSize,
            modifier = Modifier.weight(1f)
        )

        // Düzenleme butonu
        IconButton(onClick = onEditClick) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Düzenle",
                tint = itemTextColor,
                modifier = Modifier.size(iconSize * 0.8f)
            )
        }

        // Silme butonu (Eğer gösterilmesi istenmişse)
        if (showDelete) {
            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Sil",
                    tint = itemTextColor,
                    modifier = Modifier.size(iconSize * 0.8f)
                )
            }
        }
    }
}
