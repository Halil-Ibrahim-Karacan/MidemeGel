package com.example.midemegel.screens.editactivities.edit.editcomponents

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.example.midemegel.helpers.asPainter
import com.example.midemegel.data.model.CategoryModel
import kotlinx.coroutines.launch

/**
 * Düzenleme ekranında kategoriler arasında seçim yapılmasını sağlayan yatay liste bileşeni.
 * Yanında kategori listesini düzenleme sayfasına gitmeyi sağlayan bir buton barındırır.
 */
@Composable
fun EditCategorySelector(
    categories: List<CategoryModel>,
    selectedCategoryId: Int?,
    onCategorySelected: (Int) -> Unit,
    onEditCategoriesClick: () -> Unit,
    darkTheme: Boolean,
    themeTextColor: Color,
    categoryIconSize: Dp,
    categoryTextSize: TextUnit,
    screenWidth: Dp
) {
    // --- Mantıksal Değişkenler ve Animasyonlar ---
    
    val scope = rememberCoroutineScope()
    // Kategori düzenleme butonunun tıklama animasyonu için scale değeri
    val categoryEditIconScale = remember { Animatable(1f) }

    // --- Tasarım Bileşenleri ---
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Yatay Kategori Listesi
        LazyRow(
            modifier = Modifier
                .weight(1f)
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(categories) { category ->
                val isSelected = selectedCategoryId == category.id
                // Her bir kategori kartı
                Card(
                    modifier = Modifier
                        .wrapContentSize()
                        // Seçili değilse şeffaflığı azaltarak geri plana iter
                        .alpha(if (isSelected) 1f else 0.5f)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            onCategorySelected(category.id)
                        },
                    elevation = CardDefaults.cardElevation(0.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(5.dp)
                    ) {
                        Image(
                            painter = category.image.asPainter(),
                            modifier = Modifier.size(categoryIconSize),
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.size(5.dp))
                        Text(
                            text = category.name,
                            color = themeTextColor,
                            fontSize = categoryTextSize,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        // Kategori Yönetim Sayfasına Geçiş Butonu (Liste İkonu)
        Icon(
            imageVector = Icons.Default.List,
            contentDescription = "Kategorileri Düzenle",
            tint = themeTextColor,
            modifier = Modifier
                .width(screenWidth * 0.136f)
                .height(screenWidth * 0.1f)
                .padding(end = 16.dp)
                .background(if (!darkTheme) Color(0xFFE5E5E5) else Color(0xFF827AC0), RoundedCornerShape(10))
                .border(1.dp, themeTextColor, RoundedCornerShape(10))
                .graphicsLayer(
                    scaleX = categoryEditIconScale.value,
                    scaleY = categoryEditIconScale.value
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {
                        // Tıklama efekti ve aksiyon tetikleme
                        scope.launch {
                            categoryEditIconScale.animateTo(0.85f, tween(120, easing = LinearOutSlowInEasing))
                            categoryEditIconScale.animateTo(1f, spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow))
                            onEditCategoriesClick()
                        }
                    }
                )
        )
    }
}
