package com.example.midemegel.components.dialogs

import androidx.compose.foundation.layout.offset
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.midemegel.components.common.MyOutlinedButton
import com.example.midemegel.helpers.utils.getBodyFontSize
import com.example.midemegel.helpers.utils.getButtonFontSize
import com.example.midemegel.helpers.utils.getTitleFontSize

/**
 * Kullanıcıdan silme işlemi için onay alan genel amaçlı diyalog penceresi.
 * Uyarı mesajı gösterir ve 'Sil' veya 'İptal' seçenekleri sunar.
 */
@Composable
fun DeleteDialog(
    informationText: String, 
    textColor: Color, 
    onDismissRequest: () -> Unit, 
    onDelete: () -> Unit
) {
    // --- Mantıksal Değişkenler (Yazı Boyutları) ---
    val titleFontSize = getTitleFontSize()
    val textFontSize = getBodyFontSize()
    val buttonFontSize = getButtonFontSize()

    // --- Tasarım Bileşenleri (Material3 AlertDialog) ---
    AlertDialog(
        onDismissRequest = onDismissRequest, // Dışarı tıklandığında ne olacağı
        containerColor = Color.White,
        title = { 
            // Başlık Kısmı
            Text(text = "UYARI!", color = textColor, fontSize = titleFontSize) 
        },
        text = {
            // İçerik/Bilgi Metni
            Text(text = informationText, color = textColor, fontSize = textFontSize)
        },
        confirmButton = {
            // Onay Butonu (Sil)
            MyOutlinedButton(
                onClick = { onDelete() },
                text = "Sil",
                fontSize = buttonFontSize,
                modifier = Modifier.offset(0.dp, -15.dp) // Görsel hizalama için ofset
            )
        },
        dismissButton = {
            // Vazgeç Butonu (İptal)
            MyOutlinedButton(
                onClick = { onDismissRequest() },
                text = "İptal",
                fontSize = buttonFontSize,
                modifier = Modifier.offset(0.dp, -15.dp)
            )
        }
    )
}
