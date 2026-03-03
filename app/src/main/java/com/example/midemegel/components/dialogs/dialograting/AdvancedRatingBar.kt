package com.example.midemegel.components.dialograting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.midemegel.*
import com.example.midemegel.helpers.utils.getResponsiveFontSize
import com.example.midemegel.helpers.utils.getScreenWidth
import kotlin.math.ceil

/**
 * Kullanıcının sürükleme veya tıklama ile puan (1-5 arası) vermesini sağlayan gelişmiş puanlama çubuğu.
 * Seçilen puana göre ilgili emojiyi ve metin etiketini dinamik olarak gösterir.
 */
@Composable
fun AdvancedRatingBar(
    rating: Float,
    onRatingChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    starCount: Int = 5,
    starSize: Dp = 48.dp
) {
    // --- Mantıksal Değişkenler ve Durumlar ---
    
    // Mevcut seçili puan durumunu takip eder
    var selectedRating by remember { mutableFloatStateOf(rating) }
    
    // Puanlara karşılık gelen görsel ve metinsel geri bildirimler
    val emojis = listOf("🤮", "😕", "😐", "😋", "🤤")
    val labels = listOf("İğrenç", "Yenilebilir", "Fena Değil", "Lezzetli", "Muazzam")

    // Ekran boyutuna dayalı dinamik yazı boyutları
    val screenWidth = getScreenWidth()
    val emojiFontSize = getResponsiveFontSize(0.12f)
    val labelFontSize = getResponsiveFontSize(0.06f)

    // --- Tasarım Bileşenleri ---
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Puanın anlamını belirten emoji
        Text(text = emojis[selectedRating.toInt() - 1], fontSize = emojiFontSize)
        Spacer(modifier = Modifier.size(screenWidth * 0.024f))
        
        // Puanın anlamını belirten metin etiketi
        Text(text = labels[selectedRating.toInt() - 1], fontSize = labelFontSize)
        Spacer(modifier = Modifier.size(screenWidth * 0.024f))

        // Etkileşimli Yıldız Çubuğu
        Row(
            modifier = modifier
                .wrapContentWidth()
                .pointerInput(Unit) {
                    // Sürükleme (Drag) hareketlerini algılar ve puanı anlık günceller
                    detectDragGestures(
                        onDragStart = { offset ->
                            val starWidth = starSize.toPx()
                            val clickedStar = (offset.x / starWidth).coerceIn(0f, starCount.toFloat())
                            selectedRating = ceil(clickedStar).coerceAtLeast(1f)
                            onRatingChange(selectedRating)
                        },
                        onDrag = { change, _ ->
                            change.consume()
                            val starWidth = starSize.toPx()
                            val clickedStar = (change.position.x / starWidth).coerceIn(0f, starCount.toFloat())
                            val newRating = ceil(clickedStar).coerceAtLeast(1f)
                            if (newRating != selectedRating) {
                                selectedRating = newRating
                                onRatingChange(selectedRating)
                            }
                        }
                    )
                }
        ) {
            // Yıldızları oluştur
            for (i in 1..starCount) {
                val starPosition = i.toFloat()

                Box(
                    modifier = Modifier
                        .size(starSize)
                        .clickable {
                            // Tıklama ile puan seçimi
                            selectedRating = if (selectedRating > starPosition) i.toFloat() else starPosition
                            onRatingChange(selectedRating)
                        }
                ) {
                    // Boş Yıldız İkonu (Alt Katman)
                    Icon(
                        painter = painterResource(R.drawable.star_border_icon),
                        contentDescription = "Boş Yıldız",
                        modifier = Modifier.size(starSize),
                        tint = Color.Yellow
                    )

                    // Dolu Yıldız İkonu (Üst Katman - Puan değerine göre görünür)
                    if (i <= selectedRating) {
                        Icon(
                            painter = painterResource(R.drawable.star_icon),
                            contentDescription = "Dolu Yıldız",
                            modifier = Modifier.size(starSize),
                            tint = Color.Yellow
                        )
                    }
                }
            }
        }
    }
}
