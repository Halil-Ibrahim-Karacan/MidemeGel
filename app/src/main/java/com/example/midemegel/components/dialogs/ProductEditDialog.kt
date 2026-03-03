package com.example.midemegel.components.dialogs

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.midemegel.components.common.CustomTextField
import com.example.midemegel.helpers.utils.getBodyFontSize
import com.example.midemegel.helpers.utils.getButtonBackgroundColor
import com.example.midemegel.helpers.utils.getButtonFontSize
import com.example.midemegel.helpers.utils.getButtonTextColor
import com.example.midemegel.helpers.utils.getScreenWidth
import com.example.midemegel.helpers.utils.getTagFontSize
import com.example.midemegel.helpers.utils.getTextFieldBackgroundColor
import com.example.midemegel.helpers.utils.getThemeTextColor
import com.example.midemegel.helpers.utils.getTitleFontSize

/**
 * Ürün içeriklerini (malzemelerini) liste olarak düzenlemeye olanak tanıyan diyalog penceresi.
 * Kullanıcı yeni içerik ekleyebilir veya mevcut içerikleri silebilir.
 */
@Composable
fun ProductEditDialog(
    darkTheme: Boolean, 
    list: MutableState<MutableList<String>>, 
    scrollState: ScrollState, 
    onDismiss: () -> Unit, 
    onSave: () -> Unit
){
    // --- Mantıksal Değişkenler ve Durum Yönetimi ---
    
    // Yeni eklenen içerik metni için geçici durum
    var txt by remember { mutableStateOf("") }
    
    // Temaya göre renkler
    val themeTextColor = getThemeTextColor(darkTheme)
    val textfieldBackgroundColor = getTextFieldBackgroundColor(darkTheme)
    val buttonBackgroundColor = getButtonBackgroundColor(darkTheme)
    val buttonTextColor = getButtonTextColor(darkTheme)

    // Ekran ve yazı boyutları
    val screenWidth = getScreenWidth()
    val titleFontSize = getTitleFontSize()
    val labelFontSize = getBodyFontSize()
    val buttonFontSize = getButtonFontSize()
    val tagFontSize = getTagFontSize()

    // --- Tasarım Bileşenleri ---
    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = RoundedCornerShape(16.dp), color = textfieldBackgroundColor) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Başlık
                Text(text = "Ürün İçeriği *", fontSize = titleFontSize, color = themeTextColor)

                // İçerik Ekleme Satırı (Metin Alanı + Ekle Butonu)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CustomTextField(
                        value = txt,
                        onValueChange = { newText -> if (newText.length <= 50) txt = newText },
                        label = "İçerik",
                        darkTheme = darkTheme,
                        modifier = Modifier.weight(1f),
                        isError = txt.isBlank(),
                        singleLine = true,
                        placeholder = "Örn: Süt Tozu",
                        maxLength = 50,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                if (txt.isNotBlank()) {
                                    list.value = list.value.toMutableList().apply { add(txt) }
                                    txt = ""
                                }
                            }
                        )
                    )
                    // Ekleme İkonu (+)
                    Icon(
                        imageVector = Icons.Default.Add, 
                        contentDescription = "Ekle",
                        tint = themeTextColor,
                        modifier = Modifier
                            .size(screenWidth * 0.136f)
                            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {
                                if (txt.isNotBlank()) {
                                    list.value = list.value.toMutableList().apply { add(txt) }
                                    txt = ""
                                }
                            }
                    )
                }

                Text(text = "İçerikler:", fontSize = labelFontSize, color = themeTextColor)

                // Eklenen İçeriklerin Listelendiği Alan (FlowRow: Otomatik alt satıra geçer)
                @OptIn(ExperimentalLayoutApi::class)
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier
                        .border(2.dp, themeTextColor, RoundedCornerShape(15.dp))
                        .padding(all = 5.dp)
                        .verticalScroll(state = scrollState)
                        .fillMaxWidth()
                        .heightIn(min = 150.dp)
                ) {
                    list.value.forEachIndexed { index, tag ->
                        // Her bir içerik etiketi (Tag)
                        Row(
                            modifier = Modifier
                                .background(buttonBackgroundColor, RoundedCornerShape(25.dp))
                                .padding(vertical = 7.dp, horizontal = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(text = tag, color = buttonTextColor, modifier = Modifier.weight(1f, fill = false), fontSize = tagFontSize)
                            // İçeriği Silme İkonu (X)
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Sil",
                                tint = buttonTextColor,
                                modifier = Modifier
                                    .clickable {
                                        list.value = list.value.toMutableList().apply { removeAt(index) }
                                    }
                                    .size(screenWidth * 0.045f)
                                    .padding(2.dp)
                            )
                        }
                    }
                }


                Text(text = "*SADECE HARF VE RAKAM KULLANINIZ!", fontSize = labelFontSize, color = MaterialTheme.colorScheme.error)

                // Diyalog Aksiyon Butonları
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(colors = ButtonDefaults.buttonColors(containerColor = buttonBackgroundColor), onClick = onDismiss) { 
                        Text(text = "İptal", color = buttonTextColor, fontSize = buttonFontSize) 
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(colors = ButtonDefaults.buttonColors(containerColor = buttonBackgroundColor), onClick = onSave, enabled = list.value.isNotEmpty()) {
                        Text(text = "Tamam", color = buttonTextColor, fontSize = buttonFontSize)
                    }
                }
            }
        }
    }
}
