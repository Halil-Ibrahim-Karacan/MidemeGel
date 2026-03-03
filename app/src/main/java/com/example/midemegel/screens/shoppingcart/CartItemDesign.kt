package com.example.midemegel.screens.shoppingcart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.example.midemegel.components.common.FancyAnimatedCounter_Fixed
import com.example.midemegel.helpers.asPainter
import com.example.midemegel.helpers.toComposeColor
import com.example.midemegel.helpers.utils.getContrastTextColor
import com.example.midemegel.data.model.BagProductModel

/**
 * Alışveriş sepetindeki her bir ürün satırının tasarımını tanımlayan Composable.
 * Ürün görseli, ismi, fiyatı, miktar kontrolü ve silme butonunu içerir.
 */
@Composable
fun CartItemDesign(
    // --- Parametreler ---
    product: BagProductModel, // Sepetteki ürün verisi
    themeTextColor: Color, // Tema metin rengi
    productImageSize: Dp, // Görsel boyutu
    productNameFontSize: TextUnit, // İsim yazı boyutu
    priceFontSize: TextUnit, // Fiyat yazı boyutu
    deleteIconSize: Dp, // Silme ikonu boyutu
    onProductClick: () -> Unit, // Ürüne tıklandığında (detaya gitmek için)
    onDeleteClick: () -> Unit, // Silme butonuna tıklandığında
    onIncreaseCount: () -> Unit, // Miktarı artır
    onDecreaseCount: () -> Unit // Miktarı azalt
) {
    // --- Mantıksal Değişkenler ve Stil Tanımlamaları ---
    
    // Ürünün kendi rengi ve üzerine gelecek metin için kontrast renk
    val currentColor = product.color.toComposeColor()
    val contrastTextColor = getContrastTextColor(product)

    // --- Tasarım Bileşenleri ---
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = currentColor, shape = RoundedCornerShape(10.dp))
            .border(width = 2.dp, color = themeTextColor, RoundedCornerShape(10.dp))
            .clickable { onProductClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // --- Ürün Görseli ---
        // Radyal bir gradyan arka plan üzerine ürün resmini yerleştirir.
        Image(
            painter = product.image.asPainter(),
            contentDescription = null,
            modifier = Modifier
                .size(productImageSize)
                .padding(5.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color.White, Color.Transparent)
                    )
                )
        )

        // --- Ürün Bilgileri ve Kontroller ---
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            // Ürün İsmi
            Text(
                text = product.name ?: "",
                color = contrastTextColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                fontSize = productNameFontSize,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            
            // Fiyat ve Miktar Sayacı Satırı
            Row(
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(text = "${product.price}₺", color = contrastTextColor, fontSize = priceFontSize)
                
                // Animasyonlu miktar artırma/azaltma bileşeni
                FancyAnimatedCounter_Fixed(
                    backgroundColor = Color.White,
                    color = currentColor,
                    buttonTextColor = contrastTextColor,
                    Count = product.count,
                    removeProduct = { onDecreaseCount() },
                    addProduct = { onIncreaseCount() }
                )
            }
        }

        // --- Silme Butonu ---
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Sil",
            tint = contrastTextColor,
            modifier = Modifier
                .padding(5.dp)
                .size(deleteIconSize)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { onDeleteClick() }
                )
        )
    }
}
