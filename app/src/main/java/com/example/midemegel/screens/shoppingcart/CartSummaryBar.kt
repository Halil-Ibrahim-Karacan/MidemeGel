package com.example.midemegel.screens.shoppingcart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import com.example.midemegel.components.common.MyOutlinedButton

@Composable
fun CartSummaryBar(
    totalAmount: Double,
    themeBackgroundColor: Color,
    themeTextColor: Color,
    totalAmountFontSize: TextUnit,
    buttonFontSize: TextUnit,
    paddingValues: PaddingValues,
    screenWidth: Dp,
    onCheckoutClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(themeBackgroundColor)
            .padding(
                start = screenWidth * 0.04f,
                end = screenWidth * 0.04f,
                bottom = paddingValues.calculateBottomPadding(),
                top = screenWidth * 0.04f
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            text = "Toplam Tutar: ${"%.2f".format(totalAmount)}₺",
            color = themeTextColor,
            fontSize = totalAmountFontSize,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        MyOutlinedButton(
            onClick = onCheckoutClick,
            text = "Alışverişi Tamamla",
            fontSize = buttonFontSize,
            modifier = Modifier.padding(screenWidth * 0.025f),
            containerColor = themeTextColor.copy(0.1f),
            contentColor = themeTextColor,
            borderColor = themeTextColor,
            fontWeight = FontWeight.Bold
        )
    }
}