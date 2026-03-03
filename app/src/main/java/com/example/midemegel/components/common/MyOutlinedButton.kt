package com.example.midemegel.components.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

/**
 * Uygulama genelinde kullanılan, kenarlıklı (outlined) özel buton tasarımı.
 * Standart Material3 OutlinedButton bileşenini projenin görsel stiline uygun hale getirir.
 */
@Composable
fun MyOutlinedButton(
    onClick: () -> Unit,
    text: String,
    fontSize: TextUnit,
    modifier: Modifier = Modifier,
    containerColor: Color = Color.Black,
    contentColor: Color = Color.White,
    borderColor: Color = Color.Black,
    fontWeight: FontWeight = FontWeight.Normal
) {
    // --- Tasarım Bileşenleri ---
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        border = BorderStroke(2.dp, borderColor),
        shape = RoundedCornerShape(50.dp) // Tam yuvarlak kenarlar
    ) {
        // Buton içindeki metin
        Text(text = text, fontSize = fontSize, fontWeight = fontWeight)
    }
}
