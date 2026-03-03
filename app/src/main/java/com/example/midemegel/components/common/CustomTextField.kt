package com.example.midemegel.components.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.example.midemegel.helpers.utils.getBodyFontSize
import com.example.midemegel.helpers.utils.getResponsiveFontSize
import com.example.midemegel.helpers.utils.getScreenWidth
import com.example.midemegel.helpers.utils.getTextFieldBackgroundColor
import com.example.midemegel.helpers.utils.getThemeTextColor

/**
 * Uygulama genelinde kullanılan, özelleştirilmiş giriş alanı (TextField).
 * Hem standart metin girişi hem de seçim menüsü (Dropdown) olarak çalışabilir.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    darkTheme: Boolean,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    placeholder: String? = null,
    supportingText: String? = null,
    maxLength: Int? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    items: List<Pair<String, Any>>? = null, // Dropdown menü öğeleri
    onItemSelected: (Any) -> Unit = {},
    enabled: Boolean = true,
    singleLine: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    // --- Mantıksal Değişkenler ve Stil Tanımlamaları ---
    
    val themeColor = getThemeTextColor(darkTheme)
    val textfieldBackgroundColor = getTextFieldBackgroundColor(darkTheme)
    val screenWidth = getScreenWidth()
    val textFieldFontSize = getBodyFontSize()

    // --- Tasarım Bileşenleri ---

    // Eğer 'items' listesi verilmişse bir Dropdown (Açılır Menü) oluştur
    if (items != null) {
        var expanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { if(enabled) expanded = !expanded }
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = {},
                readOnly = true, // Kullanıcı doğrudan metin yazamaz, sadece seçebilir
                label = { Text(text = label, color = themeColor, fontSize = textFieldFontSize) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = modifier
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable, enabled)
                    .fillMaxWidth(),
                isError = isError,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = textfieldBackgroundColor,
                    unfocusedContainerColor = textfieldBackgroundColor,
                    disabledContainerColor = textfieldBackgroundColor,
                    focusedBorderColor = themeColor,
                    unfocusedBorderColor = themeColor,
                    disabledBorderColor = themeColor,
                    cursorColor = themeColor,
                    focusedTextColor = themeColor,
                    unfocusedTextColor = themeColor,
                    disabledTextColor = themeColor,
                    focusedTrailingIconColor = themeColor,
                    unfocusedTrailingIconColor = themeColor,
                    disabledTrailingIconColor = themeColor,
                    disabledLabelColor = themeColor
                ),
                enabled = enabled
            )
            // Açılır menü içeriği
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(textfieldBackgroundColor)
            ) {
                items.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item.first, color = themeColor) },
                        onClick = {
                            onItemSelected(item.second)
                            expanded = false
                        }
                    )
                }
            }
        }
    } else {
        // Standart Metin Giriş Alanı
        OutlinedTextField(
            value = value,
            onValueChange = { newText ->
                // Karakter sınırı kontrolü
                if (maxLength == null || newText.length <= maxLength) {
                    onValueChange(newText)
                }
            },
            label = { Text(text = label, color = themeColor, fontSize = textFieldFontSize) },
            modifier = modifier.fillMaxWidth(),
            isError = isError,
            singleLine = singleLine,
            trailingIcon = trailingIcon ?: {
                // Metin varsa temizleme butonu (X) göster
                AnimatedVisibility(visible = value.isNotEmpty()) {
                    IconButton(
                        onClick = { onValueChange("") },
                        modifier = Modifier.size(screenWidth * 0.058f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Temizle",
                            tint = themeColor
                        )
                    }
                }
            },
            placeholder = {
                if (placeholder != null) {
                    Text(text = placeholder, color = themeColor, fontSize = textFieldFontSize)
                }
            },
            supportingText = {
                // Alt kısımda hata mesajı ve karakter sayacı göster
                Row {
                    if (supportingText != null) {
                        Text(
                            text = supportingText,
                            color = if (isError) MaterialTheme.colorScheme.error else themeColor,
                            textAlign = TextAlign.Left,
                            modifier = Modifier.weight(1f),
                            fontSize = getResponsiveFontSize(0.03f)
                        )
                    }
                    if (maxLength != null) {
                        Text(
                            text = "${value.length}/$maxLength",
                            color = if (value.length >= maxLength) MaterialTheme.colorScheme.error else themeColor,
                            textAlign = TextAlign.Right,
                            modifier = Modifier.wrapContentWidth(),
                            fontSize = getResponsiveFontSize(0.03f)
                        )
                    }
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = textfieldBackgroundColor,
                unfocusedContainerColor = textfieldBackgroundColor,
                focusedBorderColor = themeColor,
                unfocusedBorderColor = themeColor,
                cursorColor = themeColor,
                focusedTextColor = themeColor,
                unfocusedTextColor = themeColor,
                disabledContainerColor = textfieldBackgroundColor,
                disabledBorderColor = themeColor,
                disabledTextColor = themeColor,
                disabledLabelColor = themeColor
            ),
            enabled = enabled,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions
        )
    }
}
