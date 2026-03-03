package com.example.midemegel.components.common

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import com.example.midemegel.helpers.utils.getResponsiveFontSize
import com.example.midemegel.helpers.utils.getScreenWidth
import com.example.midemegel.helpers.utils.getTagFontSize
import com.example.midemegel.helpers.utils.getThemeTextColor

/**
 * Sayfaların üst kısmında yer alan, ikon ve başlık metnini içeren genel başlık tasarımı.
 * Ayrıca toplam ürün sayısını da alt kısımda gösterir.
 */
@Composable
fun HeaderDesign(darkTheme: Boolean, productCount: Int, headerIcon: Int, headerText: String){
    // --- Mantıksal Değişkenler ve Hesaplamalar ---
    // Mevcut temaya göre metin rengini alır
    val themeTextColor = getThemeTextColor(darkTheme)
    // Ekran genişliğine göre dinamik boyutlandırma yapar
    val screenWidth = getScreenWidth()

    // Tasarım öğeleri için oranlanmış boyutlar
    val headerHeight = screenWidth * 0.13f
    val iconSize = screenWidth * 0.09f
    val spacerWidth = screenWidth * 0.04f
    val titleFontSize = getResponsiveFontSize(0.08f)
    val countFontSize = getTagFontSize()

    // --- Tasarım Bileşenleri ---
    Row(
        modifier = Modifier
            .wrapContentWidth()
            .height(headerHeight),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Başlık İkonu (örn: Menü ikonu, Favori ikonu)
        Icon(
            painter = painterResource(id = headerIcon),
            contentDescription = headerText,
            tint = themeTextColor,
            modifier = Modifier.size(iconSize)
        )

        Spacer(modifier = Modifier.size(spacerWidth))

        // Sayfanın başlık metni
        Text(
            text = headerText,
            color = themeTextColor,
            fontSize = titleFontSize,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily.Serif,
        )
    }

    // Listelenen toplam ürün miktarını belirten bilgi metni
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = "Listelenen Toplam Ürün Sayısı: ${productCount}",
        color = themeTextColor,
        textAlign = TextAlign.Center,
        fontSize = countFontSize
    )
}
