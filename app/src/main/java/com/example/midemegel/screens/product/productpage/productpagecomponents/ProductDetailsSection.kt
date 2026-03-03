package com.example.midemegel.screens.product.productpage.productpagecomponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.example.midemegel.R
import com.example.midemegel.data.model.ProductModel

/**
 * Ürün detay sayfasındaki metinsel bilgileri barındıran bölüm.
 * Ürün ismi, puanlama, içerik listesi (etiketler) ve ürün açıklamasını görüntüler.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProductDetailsSection(
    productModel: ProductModel,
    currentColor: Color,
    contrastTextColor: Color,
    productNameFontSize: TextUnit,
    ratingTextFontSize: TextUnit,
    starIconSize: Dp,
    ratingCount: Int,
    onRatingClick: () -> Unit
) {
    // --- Tasarım Bileşenleri ---
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- Ürün İsmi ---
        // Gölgeli ve belirgin bir başlık tasarımı sunar.
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopCenter
        ) {
            Text(
                modifier = Modifier.offset(0.dp, 10.dp),
                text = productModel.name,
                color = currentColor,
                fontSize = productNameFontSize,
                style = TextStyle.Default.copy(
                    shadow = Shadow(
                        color = Color.Black,
                        offset = Offset(3f, 3f),
                        blurRadius = 0f
                    )
                )
            )
        }

        // --- Puanlama ve Derecelendirme Butonu ---
        Box(modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Yıldız İkonu (Gölge ve Ana katman)
                Box {
                    Image(
                        painter = painterResource(id = R.drawable.star_icon),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(Color.Black),
                        modifier = Modifier
                            .size(starIconSize)
                            .offset(x = 2.dp, y = 2.dp)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.star_icon),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(Color.Yellow),
                        modifier = Modifier.size(starIconSize)
                    )
                }
                // Puan bilgisi ve değerlendiren kişi sayısı
                Text(
                    text = "(${productModel.ratingSize.toInt()}) Toplam $ratingCount kişi değerlendirdi.",
                    color = Color.Black,
                    fontSize = ratingTextFontSize
                )
            }
            // Derecelendirme Diyaloğunu Açan Buton
            Text(
                text = "Derecelendir",
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onRatingClick() }
                    .align(Alignment.CenterEnd)
                    .padding(horizontal = 5.dp),
                color = currentColor,
                textDecoration = TextDecoration.Underline,
                fontSize = ratingTextFontSize,
                style = TextStyle.Default.copy(
                    shadow = Shadow(
                        color = Color.Black,
                        offset = Offset(3f, 3f),
                        blurRadius = 0f
                    )
                )
            )
        }

        // --- Ürün İçerikleri (Etiketler/Tags) ---
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Kimyasal Formülümüz:",
                color = Color.Black,
                modifier = Modifier.padding(5.dp),
                fontWeight = FontWeight.Bold,
                fontSize = ratingTextFontSize
            )
            // FlowRow ile içerik etiketlerini otomatik alt satıra geçecek şekilde listeler
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.padding(horizontal = 5.dp)
            ) {
                productModel.getIngredientsList().forEach { tag ->
                    Text(
                        text = tag,
                        color = contrastTextColor,
                        fontSize = ratingTextFontSize,
                        modifier = Modifier
                            .background(currentColor, RoundedCornerShape(25.dp))
                            .border(2.dp, Color.Black, RoundedCornerShape(25.dp))
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
            }
        }

        // --- Resmi Ürün Açıklaması ---
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Resmi Açıklama:",
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 10.dp),
                fontWeight = FontWeight.Bold,
                fontSize = ratingTextFontSize
            )
            Text(
                text = productModel.description,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 10.dp),
                fontSize = ratingTextFontSize
            )
        }
    }
}
