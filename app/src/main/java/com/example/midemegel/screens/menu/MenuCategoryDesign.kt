package com.example.midemegel.screens.menu

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.midemegel.helpers.asPainter
import com.example.midemegel.helpers.utils.getBodyFontSize
import com.example.midemegel.helpers.utils.getScreenWidth
import com.example.midemegel.helpers.utils.getThemeTextColor

/**
 * Kategori listesindeki her bir kategori öğesinin tasarımını tanımlayan Composable.
 * Seçili olma durumuna göre ölçeklenme ve şeffaflık efektleri uygular.
 */
@SuppressLint("ContextCastToActivity")
@Composable
fun MenuCategoryDesign(
    currentSelectedIndex: Int,
    index: Int,
    categoryname: String,
    categoryImage: String,
    darkTheme: Boolean,
    onCardClick: () -> Unit,
    onHeightMeasured: (Int) -> Unit
) {
    // --- Mantıksal Değişkenler ve Animasyonlar ---

    val themeTextColor = getThemeTextColor(darkTheme)
    val isSelected = currentSelectedIndex == index // Öğenin seçili olup olmadığını kontrol eder

    // Ekran genişliğine göre dinamik boyutlandırma
    val screenWidth = getScreenWidth()
    val imageSize = screenWidth * 0.12f 
    val fontSize = getBodyFontSize()
    val cardPadding = screenWidth * 0.024f

    // Seçili öğe için zıplama (bounce) animasyonu hazırlığı
    val infiniteTransition = rememberInfiniteTransition(label = "bounce")
    val bounceOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -20f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bounceOffset"
    )

    // Not: verticalOffset şu anki tasarımda aktif olarak kullanılmamaktadır (yoruma alınmıştır).
    val verticalOffset = if (isSelected) bounceOffset else 0f

    // --- Tasarım Bileşenleri ---
    Card(
        modifier = Modifier
            .wrapContentSize()
            .padding(cardPadding)
            // Seçili öğeyi daha belirgin hale getirmek için ölçekle ve öne çıkar
            .scale(if (isSelected) 1.1f else 1f)
            .zIndex(if (isSelected) 2f else 1f)
            .alpha(if (isSelected) 1f else 0.5f)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ){}
            // Bileşenin yüksekliğini ölçüp üst bileşene bildirir (merkezleme hesaplamaları için)
            .onGloballyPositioned { coordinates ->
                onHeightMeasured(coordinates.size.height)
            },
        elevation = CardDefaults.cardElevation(0.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        onClick = onCardClick
    ) {
        // Kategori görseli
        Image(
            modifier = Modifier.size(imageSize).align(Alignment.CenterHorizontally),
            painter = categoryImage.asPainter(),
            contentDescription = null
        )
        
        Spacer(modifier = Modifier.size(screenWidth * 0.024f))
        
        // Kategori ismi
        Text(
            text = categoryname,
            color = themeTextColor,
            modifier = Modifier.wrapContentSize(),
            textAlign = TextAlign.Center,
            fontSize = fontSize
        )
    }
}
