package com.example.midemegel.components.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import com.example.midemegel.components.common.CustomTextField
import com.example.midemegel.helpers.enums.CategoryAnimationEnum
import com.example.midemegel.helpers.asPainter
import com.example.midemegel.data.model.CategoryModel
import com.example.midemegel.helpers.utils.getButtonBackgroundColor
import com.example.midemegel.helpers.utils.getButtonFontSize
import com.example.midemegel.helpers.utils.getButtonTextColor
import com.example.midemegel.helpers.utils.getDialogBackgroundColor
import com.example.midemegel.helpers.utils.getProductImageSize
import com.example.midemegel.helpers.utils.getTitleFontSize
import java.io.File
import java.io.FileOutputStream

/**
 * Yeni kategori eklemek veya mevcut bir kategoriyi düzenlemek için kullanılan diyalog penceresi.
 * Kategori ismi, görseli ve uygulanacak animasyon tipinin seçilmesini sağlar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryEditDialog(
    darkTheme: Boolean,
    itemTextColor: Color,
    category: CategoryModel?, // Düzenlenecek kategori (Yeni ise null)
    listCategories: List<CategoryModel>,
    onDismiss: () -> Unit,
    onSave: (CategoryModel) -> Unit
) {
    // --- Mantıksal Değişkenler ve Form Durumları ---
    
    // Kullanıcı girdilerini tutan state'ler
    var name by remember { mutableStateOf(category?.name ?: "") }
    var animation by remember { mutableStateOf(category?.animation ?: CategoryAnimationEnum.Animasyon_Yok.animationName) }

    // animationSpeed: 1 (Yavaş), 2 (Normal), 3 (Hızlı)
    var animationSpeed by remember { mutableStateOf(category?.animationSpeed ?: 2) } 
    var imagePath by remember { mutableStateOf(category?.image ?: "") }
    
    // Tema renkleri ve yazı boyutları
    val buttonBackgroundColor = getButtonBackgroundColor(darkTheme)
    val buttonTextColor = getButtonTextColor(darkTheme)
    val dialogBackgroundColor = getDialogBackgroundColor(darkTheme)

    val titleFontSize = getTitleFontSize()
    val buttonFontSize = getButtonFontSize()
    val productImageSize = getProductImageSize()

    // Animasyonun seçili olup olmadığını kontrol eder
    val isAnimationSelected = animation != CategoryAnimationEnum.Animasyon_Yok.animationName

    // İsim zaten var mı kontrolü
    val isDuplicate = remember(name) {
        listCategories.any {
            it.name.equals(name.trim(), ignoreCase = true) && it.id != category?.id
        }
    }


    // Formun geçerliliğini kontrol eder (Zorunlu alan kontrolü)
    val isFormValid = remember(name, imagePath) {
        name.isNotBlank() && !isDuplicate && imagePath.isNotBlank()
    }

    val context = LocalContext.current
    
    // Resim seçme işlemi için sistem arayüzünü başlatır ve seçilen resmi dahili depolamaya kopyalar
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                val fileName = "category_${System.currentTimeMillis()}.jpg"
                val file = File(context.filesDir, fileName)
                context.contentResolver.openInputStream(it)?.use { input ->
                    FileOutputStream(file).use { output ->
                        input.copyTo(output)
                    }
                }
                imagePath = file.absolutePath
            }
        }
    )

    // --- Tasarım Bileşenleri ---
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = dialogBackgroundColor
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Başlık metni
                Text(text = if (category == null) "Yeni Kategori" else "Kategoriyi Düzenle", fontSize = titleFontSize, color = itemTextColor)

                Spacer(modifier = Modifier.height(16.dp))

                // Kategori Görseli Seçim Alanı
                Image(
                    painter = imagePath.asPainter(),
                    contentDescription = null,
                    modifier = Modifier
                        .size(productImageSize)
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (!darkTheme) Color(0xFFE5E5E5) else Color(0xFF827AC0))
                        .clickable { launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Kategori İsmi Giriş Alanı
                CustomTextField(
                    value = name,
                    onValueChange = { newText ->
                        if (newText.length <= 50) name = newText
                    },
                    label = "Kategori Adı*",
                    darkTheme = darkTheme,
                    isError = name.isBlank(),
                    placeholder = "Örn: En Sevilenler",
                    maxLength = 50
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Animasyon Tipi Seçim Menüsü
                CustomTextField(
                    value = if (isAnimationSelected) animation else "Animasyon Seçiniz!",
                    onValueChange = {},
                    label = "Animasyon",
                    darkTheme = darkTheme,
                    isError = false,//!isAnimationSelected,
                    items = CategoryAnimationEnum.entries.map { it.animationName to it.animationName },
                    onItemSelected = { selectedAnim ->
                        animation = selectedAnim as String
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Animasyon Hızı Seçim Menüsü
                CustomTextField(
                    value = if (isAnimationSelected) {
                        when (animationSpeed) {
                            3 -> "Yavaş"
                            1 -> "Hızlı"
                            else -> "Normal"
                        }
                    } else "Animasyon Seçiniz!",
                    onValueChange = {},
                    label = "Animasyon Hızı",
                    darkTheme = darkTheme,
                    isError = false,//isAnimationSelected && animationSpeed == 0,
                    enabled = isAnimationSelected,
                    items = listOf("Yavaş" to 3, "Normal" to 2, "Hızlı" to 1),
                    onItemSelected = { selectedValue ->
                        animationSpeed = selectedValue as Int
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Diyalog Butonları (İptal ve Kaydet)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(colors = ButtonDefaults.buttonColors(containerColor = buttonBackgroundColor), onClick = onDismiss) { 
                        Text(text = "İptal", color = buttonTextColor, fontSize = buttonFontSize) 
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = buttonBackgroundColor), 
                        onClick = {
                            onSave(category?.copy(name = name, animation = animation, image = imagePath, animationSpeed = animationSpeed)
                                ?: CategoryModel(name = name, animation = animation, image = imagePath, animationSpeed = animationSpeed))
                        }, 
                        enabled = isFormValid
                    ) {
                        Text(text = "Kaydet", color = buttonTextColor, fontSize = buttonFontSize)
                    }
                }
            }
        }
    }
}
