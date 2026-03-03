package com.example.midemegel.screens.editactivities.editproduct

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.midemegel.components.ColorPickerButton
import com.example.midemegel.components.common.CustomTextField
import com.example.midemegel.components.dialogs.ProductEditDialog
import com.example.midemegel.helpers.enums.CoffeeHouseCategoriesEnum
import com.example.midemegel.helpers.asPainter
import com.example.midemegel.helpers.toComposeColor
import com.example.midemegel.data.model.ProductModel
import com.example.midemegel.ui.theme.MidemeGelTheme
import com.example.midemegel.helpers.utils.getButtonFontSize
import com.example.midemegel.helpers.utils.getButtonTextColor
import com.example.midemegel.helpers.utils.getProductImageSize
import com.example.midemegel.helpers.utils.getResponsiveFontSize
import com.example.midemegel.helpers.utils.getScreenWidth
import com.example.midemegel.helpers.utils.getThemeBackgroundColor
import com.example.midemegel.helpers.utils.getThemeTextColor
import com.example.midemegel.helpers.utils.saveUriToInternalStorage
import com.example.midemegel.data.viewmodel.CategoriesViewModel
import com.example.midemegel.data.viewmodel.ProductsViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

/**
 * Yeni ürün ekleme veya mevcut bir ürünü düzenleme ekranını başlatan Activity.
 */
@AndroidEntryPoint
class EditProductActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Gelen intent üzerinden ürün bilgisini al (düzenleme modu için)
        val productJson = intent.getStringExtra("productJson")
        val product = if (productJson != null) Gson().fromJson(productJson, ProductModel::class.java) else null

        setContent {
            MidemeGelTheme {
                EditProductScreen(product)
            }
        }
    }
}

/**
 * Ürün ekleme/düzenleme formunun bulunduğu ana ekran tasarımı.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@SuppressLint("ContextCastToActivity")
@Composable
fun EditProductScreen(product: ProductModel?) {
    // --- Mantıksal Değişkenler ve Durum Yönetimi ---
    
    val activity = (LocalContext.current as Activity)
    val context = LocalContext.current
    val darkTheme = activity.intent.getBooleanExtra("darkTheme", false)
    
    // ViewModels ve Coroutine Scope
    val productsViewModel: ProductsViewModel = hiltViewModel()
    val categoriesViewModel: CategoriesViewModel = hiltViewModel()
    val listCategories by categoriesViewModel.categoriesState.collectAsState()
    val listProducts by productsViewModel.productsState.collectAsState()
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current

    // Ekran boyutuna dayalı ölçüler
    val screenWidth = getScreenWidth()
    val headerHeight = screenWidth * 0.20f
    val iconSize = screenWidth * 0.085f
    val titleFontSize = getResponsiveFontSize(0.058f)
    val productImageSize = getProductImageSize()
    val buttonFontSize = getButtonFontSize()

    // Tema Renkleri
    val themeBackgroundColor = getThemeBackgroundColor(darkTheme)
    val themeColor = getThemeTextColor(darkTheme)
    val buttonBackgroundColor = if (!darkTheme) Color(0xFFA6A6A6) else Color(0xFF1C1556)
    val buttonTextColor = getButtonTextColor(darkTheme)

    // Form State'leri (Kullanıcı girdilerini tutar)
    var name by remember { mutableStateOf(product?.name ?: "") }
    var price by remember { mutableStateOf(product?.price?.toString() ?: "") }
    var description by remember { mutableStateOf(product?.description ?: "") }
    var imagePath by remember { mutableStateOf(product?.image ?: "") }
    var favorite by remember { mutableStateOf(product?.favorite == 1) }
    var selectedColorHex by remember { mutableStateOf(product?.color ?: "#FFEBEB") }
    var selectedColor by remember { mutableStateOf(selectedColorHex.toComposeColor()) }
    var ingredients by remember { mutableStateOf(product?.ingredients ?: "") }
    var selectedCategoryId by remember { mutableStateOf(product?.categoryId ?: 0) }
    var chCategory by remember { mutableStateOf(product?.chcategory ?: "") }

    // UI Kontrol Durumları
    var showIngredientsDialog by remember { mutableStateOf(false) }
    val backIconScale = remember { Animatable(1f) }
    val list = remember {
        mutableStateOf(
            product?.ingredients?.split(", ")?.filter { it.isNotBlank() }?.toMutableList()
                ?: mutableListOf()
        )
    }

    // Seçili kategorinin "Kahvehane" olup olmadığını belirler
    val isKahvehane = remember(selectedCategoryId, listCategories) {
        listCategories.find { it.id == selectedCategoryId }?.name == "Kahvehane"
    }

    // İsim çakışmasını kontrol eden bir state eklendi
    val isNameDuplicate = remember(name, listProducts) {
        listProducts.any { it.name.equals(name.trim(), ignoreCase = true) && it.id != product?.id }
    }

    // Formun kaydedilebilir olup olmadığını kontrol eder (Zorunlu alan kontrolü)
    val isFormValid = remember(name, price, imagePath, selectedCategoryId, description, ingredients, chCategory, isKahvehane) {
        name.isNotBlank() && !isNameDuplicate && // İsim boş olmamalı ve benzersiz olmalı
                price.isNotBlank() &&
                imagePath.isNotBlank() &&
                selectedCategoryId != 0 &&
                (!isKahvehane || chCategory.isNotBlank()) &&
                (isKahvehane || description.isNotBlank()) &&
                (isKahvehane || ingredients.isNotBlank())
    }

    // --- Yan Etkiler ve İşleyiciler ---

    // Resim seçme işlemi için sistem arayüzünü başlatır
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                val path = saveUriToInternalStorage(context, it)
                if (path != null) imagePath = path
            }
        }
    )

    // İlk açılışta varsayılan kategori ataması yapar
    LaunchedEffect(listCategories) {
        if (selectedCategoryId == 0 && listCategories.isNotEmpty()) {
            selectedCategoryId = listCategories.first().id
        }
    }

    // Kahvehane kategorisi seçildiğinde otomatik alt kategori seçimi yapar
    LaunchedEffect(isKahvehane) {
        if (isKahvehane && chCategory.isBlank()) {
            chCategory = CoffeeHouseCategoriesEnum.entries.first().chCategoryName
        }
    }

    // --- Tasarım Bileşenleri ---

    // Ürün içeriklerini (malzemeleri) düzenlemek için kullanılan diyalog
    if (showIngredientsDialog) {
        ProductEditDialog(
            darkTheme = darkTheme,
            list = list,
            scrollState = scrollState,
            onDismiss = { showIngredientsDialog = false; focusManager.clearFocus() },
            onSave = { 
                showIngredientsDialog = false
                focusManager.clearFocus()
                ingredients = list.value.joinToString(separator = ", ") 
            })
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = themeBackgroundColor
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Başlık Çubuğu: Geri butonu, başlık metni ve favori durumu
            EditProductHeader(
                title = if (product == null) "Ürün Ekle" else "Ürünü Düzenle",
                isFavorite = favorite,
                themeColor = themeColor,
                headerHeight = headerHeight,
                iconSize = iconSize,
                titleFontSize = titleFontSize,
                onBackClick = { activity.finish() },
                onFavoriteClick = { favorite = !favorite }
            )

            // Ürün Görseli Alanı
            Image(
                painter = imagePath.asPainter(),
                contentDescription = null,
                modifier = Modifier
                    .size(productImageSize)
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (!darkTheme) Color(0xFFE5E5E5) else Color(0xFF827AC0))
                    .clickable { singlePhotoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }
            )

            // Form Giriş Alanları
            Column(
                modifier = Modifier
                    .padding(all = 16.dp)
                    .verticalScroll(scrollState)
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Ürün İsmi
                CustomTextField(
                    value = name,
                    onValueChange = { newText ->
                        if (newText.lines().size <= 5) name = newText
                    },
                    label = "Ürün İsmi *",
                    darkTheme = darkTheme,
                    isError = name.isBlank() || isNameDuplicate,
                    placeholder = "Örn: Eğlenceli Düşünce Kahvesi",
                    maxLength = 100
                )

                // Fiyat Bilgisi
                CustomTextField(
                    value = price,
                    onValueChange = { newValue ->
                        if (newValue.isEmpty() || newValue.matches(Regex("^\\d*[,.]?\\d*$"))) price = newValue
                    },
                    label = "Fiyat *",
                    darkTheme = darkTheme,
                    isError = price.isBlank(),
                    singleLine = true,
                    placeholder = "Örn: 24,99"
                )

                // Kategori Seçimi
                CustomTextField(
                    value = listCategories.find { it.id == selectedCategoryId }?.name ?: "Kategori Seç",
                    onValueChange = {},
                    label = "Kategori *",
                    darkTheme = darkTheme,
                    isError = selectedCategoryId == 0,
                    items = listCategories.map { it.name to it.id },
                    onItemSelected = { selectedId ->
                        selectedCategoryId = selectedId as Int
                        val selectedCategory = listCategories.find { it.id == selectedId }
                        if (selectedCategory?.name != "Kahvehane") chCategory = ""
                    }
                )

                // Kahvehane ise Alt Kategori Seçimi
                CustomTextField(
                    value = chCategory.ifBlank { "Kahvehaneyi Seçiniz!" },
                    onValueChange = {},
                    label = "Alt Kategori *",
                    darkTheme = darkTheme,
                    isError = chCategory.isBlank(),
                    enabled = isKahvehane,
                    items = CoffeeHouseCategoriesEnum.entries.map { it.chCategoryName to it.chCategoryName },
                    onItemSelected = { selectedEnumName -> chCategory = selectedEnumName as String }
                )

                // İçindekiler Düzenleme ve Renk Seçimi
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { showIngredientsDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = buttonBackgroundColor),
                        modifier = Modifier.weight(1f),
                        enabled = !isKahvehane
                    ) {
                        Text(
                            text = "İçindekiler ${if (!isKahvehane) "*" else ""}",
                            color = buttonTextColor,
                            fontSize = buttonFontSize
                        )
                    }
                    ColorPickerButton(
                        selectedColor = selectedColor,
                        onColorSelected = { color ->
                            selectedColor = color
                            selectedColorHex = String.format("#%06X", color.toArgb() and 0xFFFFFF)
                        },
                        modifier = Modifier.weight(1f),
                        buttonBorderColor = themeColor
                    )
                }

                // Ürün Açıklaması
                CustomTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = "Açıklama ${if (!isKahvehane) "*" else ""}",
                    darkTheme = darkTheme,
                    modifier = Modifier.heightIn(min = 150.dp),
                    enabled = !isKahvehane,
                    isError = !isKahvehane && description.isBlank(),
                    placeholder = "Ürünü tanımlayınız!",
                    maxLength = 1000
                )
            }

            // Kaydet / Güncelle Butonu
            Button(
                onClick = {
                    val currentOrderIndex = if (product == null) {
                        (listProducts.filter { it.categoryId == selectedCategoryId }.maxOfOrNull { it.orderIndex } ?: -1) + 1
                    } else {
                        product.orderIndex
                    }

                    val newProduct = product?.copy(
                        name = name,
                        price = price.replace(",", ".").toDoubleOrNull() ?: 0.0,
                        image = imagePath,
                        categoryId = selectedCategoryId,
                        chcategory = if (isKahvehane) chCategory else "",
                        color = selectedColorHex,
                        ingredients = if (isKahvehane) "" else ingredients,
                        description = if (isKahvehane) "" else description,
                        favorite = if (favorite) 1 else 0,
                        orderIndex = currentOrderIndex
                    ) ?: ProductModel(
                        name = name,
                        price = price.replace(",", ".").toDoubleOrNull() ?: 0.0,
                        image = imagePath,
                        categoryId = selectedCategoryId,
                        chcategory = if (isKahvehane) chCategory else "",
                        color = selectedColorHex,
                        ingredients = if (isKahvehane) "" else ingredients,
                        description = if (isKahvehane) "" else description,
                        favorite = if (favorite) 1 else 0,
                        orderIndex = currentOrderIndex
                    )
                    
                    if (product == null) productsViewModel.addProduct(newProduct) else productsViewModel.updateProduct(newProduct)
                    activity.finish()
                },
                colors = ButtonDefaults.buttonColors(containerColor = buttonBackgroundColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 16.dp),
                enabled = isFormValid
            ) {
                Text(
                    text = if (product == null) "Ekle" else "Güncelle",
                    color = buttonTextColor,
                    fontSize = buttonFontSize
                )
            }
        }
    }
}
