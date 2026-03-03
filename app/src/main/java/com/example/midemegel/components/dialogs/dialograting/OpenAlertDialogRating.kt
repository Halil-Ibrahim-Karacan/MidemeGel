package com.example.midemegel.components.dialogs.dialograting

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.midemegel.components.common.MyOutlinedButton
import com.example.midemegel.components.dialograting.AdvancedRatingBar
import com.example.midemegel.helpers.utils.getButtonFontSize
import com.example.midemegel.helpers.utils.getScreenWidth
import com.example.midemegel.helpers.utils.getTitleFontSize

/**
 * Kullanıcının bir ürüne puan (derecelendirme) vermesini sağlayan diyalog penceresi.
 * 'AdvancedRatingBar' bileşenini kullanarak etkileşimli bir puanlama arayüzü sunar.
 */
@SuppressLint("SuspiciousIndentation")
@Composable
fun OpenAlertDialogRating(
    onDismissRequest: () -> Unit, 
    onRatingSubmit: (Float) -> Unit, 
    rs: Float, // Mevcut puan (rating size)
    rc: Int,   // Toplam puan sayısı (rating count) - Not: Şu anki tasarımda gösterilmiyor ama mantıkta var
    color: Color
){
    // --- Mantıksal Değişkenler ve Durumlar ---
    
    // Kullanıcının seçtiği yeni puan değerini tutar
    var ratingSize by remember { mutableFloatStateOf(rs) }
    
    // Ekran boyutuna dayalı dinamik ölçüler
    val screenWidth = getScreenWidth()
    val starSize = screenWidth * 0.10f
    val titleFontSize = getTitleFontSize()
    val buttonFontSize = getButtonFontSize()

    // --- Tasarım Bileşenleri (Material3 AlertDialog) ---
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { 
            // Başlık metni
            Text(text = "Derecelendir!", fontSize = titleFontSize) 
        },
        text = {
            // Etkileşimli yıldız puanlama çubuğu
            AdvancedRatingBar(
                rating = ratingSize,
                onRatingChange = { newRating -> ratingSize = newRating },
                starSize = starSize
            )
        },
        confirmButton = {
            // Seçilen puanı kaydeden buton
            MyOutlinedButton(
                onClick = { onRatingSubmit(ratingSize) },
                text = "Derecelendir",
                fontSize = buttonFontSize,
                containerColor = color,
                contentColor = Color.White,
                borderColor = Color.Black,
                modifier = Modifier.offset(0.dp, (-15).dp)
            )
        }
    )
}
