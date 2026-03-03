package com.example.midemegel.screens.coffeehouse.coffeehousecomponents

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.example.midemegel.R
import com.example.midemegel.helpers.asPainter
import com.example.midemegel.data.model.ProductModel
import kotlinx.coroutines.launch

/**
 * Kahvehane ekranında, tepsindeki yatay listede her bir ürünü temsil eden öğe.
 * Ürün görselini, ismini, puanını ve favori durumunu gösterir.
 */
@Composable
fun ProductTrayItem(
    item: ProductModel,
    isSelected: Boolean,
    currentColor: Color,
    itemBackgroundColorUnselected: Color,
    contrastTextColor: Color,
    itemTextColorUnselected: Color,
    fontSize: TextUnit,
    onProductClick: () -> Unit,
    onFavoriteClick: (ProductModel) -> Unit,
    onRatingClick: () -> Unit
) {
    // --- Mantıksal Değişkenler ve Stil Tanımlamaları ---
    
    // Seçili olma durumuna göre renkleri belirle
    val backgroundColor = if (isSelected) currentColor else itemBackgroundColorUnselected
    val textColor = if (isSelected) contrastTextColor else itemTextColorUnselected

    // Favori durumu ve animasyon değişkenleri
    val favoriteIcon = if (item.favorite == 1) R.drawable.favorite_selected_icon else R.drawable.favorite_unselected_icon
    val minusScaleFavorite = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()

    // --- Tasarım Bileşenleri ---
    Row(
        modifier = Modifier
            .wrapContentSize()
            .background(backgroundColor, RoundedCornerShape(10.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onProductClick()
            }
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Puanlama Bölümü (Yıldız ve Değerlendirme Sayısı)
        Column(
            modifier = Modifier.wrapContentSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    onRatingClick() // Tıklandığında puanlama diyaloğunu açar
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box {
                // Yıldız ikonu (gölge efekti için iki katmanlı)
                Image(
                    painter = painterResource(id = R.drawable.star_icon),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(Color.Black),
                    modifier = Modifier.size(15.dp).offset(x = 1.dp, y = 1.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.star_icon),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(Color.Yellow),
                    modifier = Modifier.size(15.dp)
                )
            }
            Text(
                text = "${item.ratingSize.toInt()}\n(${item.ratingCount})",
                color = textColor,
                fontSize = fontSize,
                textAlign = TextAlign.Center,
                modifier = Modifier.wrapContentSize()
            )
        }

        // Ürün Görseli ve İsmi Bölümü
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.wrapContentSize()
        ) {
            Image(
                painter = item.image.asPainter(),
                contentDescription = null,
                modifier = Modifier.size(30.dp)
            )

            Text(
                text = item.name,
                color = textColor,
                fontSize = fontSize,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                textAlign = TextAlign.Center,
                modifier = Modifier.wrapContentSize()
            )
        }

        // Favori Butonu (Animasyonlu)
        Image(
            painter = painterResource(id = favoriteIcon),
            contentDescription = "Favori",
            modifier = Modifier
                .size(20.dp)
                .graphicsLayer(
                    scaleX = minusScaleFavorite.value,
                    scaleY = minusScaleFavorite.value
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    // Tıklama anında zıplama efekti
                    scope.launch {
                        minusScaleFavorite.animateTo(0.85f, tween(120))
                        minusScaleFavorite.animateTo(1f, spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow))
                    }
                    onFavoriteClick(item)
                },
            colorFilter = ColorFilter.tint(color = textColor)
        )
    }
}
