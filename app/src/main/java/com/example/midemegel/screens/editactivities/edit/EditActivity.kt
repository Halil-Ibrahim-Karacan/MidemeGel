package com.example.midemegel.screens.editactivities.edit

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.midemegel.components.dialogs.DeleteDialog
import com.example.midemegel.screens.editactivities.edit.editcomponents.EditCategorySelector
import com.example.midemegel.screens.editactivities.edit.editcomponents.EditHeader
import com.example.midemegel.screens.editactivities.edit.editcomponents.EditProductItem
import com.example.midemegel.helpers.enums.CoffeeHouseCategoriesEnum
import com.example.midemegel.helpers.utils.*
import com.example.midemegel.data.model.ProductModel
import com.example.midemegel.ui.theme.MidemeGelTheme
import com.example.midemegel.data.viewmodel.CategoriesViewModel
import com.example.midemegel.data.viewmodel.ProductsViewModel
import com.example.midemegel.screens.editactivities.editcategory.EditCategoryActivity
import com.example.midemegel.screens.editactivities.editproduct.EditProductActivity
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt

/**
 * Ürün düzenleme ve yönetme ekranı.
 * Kullanıcıların ürünleri silmesine, düzenlemesine ve sıralamasını değiştirmesine olanak tanır.
 */
@AndroidEntryPoint
class EditActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MidemeGelTheme {
                EditScreen()
            }
        }
    }
}

/**
 * Düzenleme ekranının ana Composable yapısı.
 * Kategorileri listeler ve seçili kategoriye ait ürünleri drag-and-drop desteğiyle sunar.
 */
@SuppressLint("ContextCastToActivity")
@Composable
fun EditScreen() {
    // --- Mantıksal Değişkenler ve Durum Yönetimi ---
    
    // Diyalog ve silme işlemi durumları
    var showDialog by remember { mutableStateOf(false) }
    var productToDelete by remember { mutableStateOf<ProductModel?>(null) }

    // Context ve tema bilgileri
    val activity = (LocalContext.current as Activity)
    val darkTheme = activity.intent.getBooleanExtra("darkTheme", false)
    
    // ViewModels
    val categoriesViewModel: CategoriesViewModel = hiltViewModel()
    val listCategories by categoriesViewModel.categoriesState.collectAsState()
    val productsViewModel: ProductsViewModel = hiltViewModel()
    val listProducts by productsViewModel.productsState.collectAsState()

    // Seçim durumları
    var selectedCategoryId by remember { mutableStateOf<Int?>(null) }
    var selectedSubCategory by remember { mutableStateOf<CoffeeHouseCategoriesEnum?>(null) }

    // Sürükle ve bırak (Drag-and-Drop) durumları
    var draggedItemIndex by remember { mutableStateOf<Int?>(null) }
    var draggedOverIndex by remember { mutableStateOf<Int?>(null) }
    var draggedItemOffset by remember { mutableStateOf(0f) }
    val lazyListState = rememberLazyListState()

    // Ekran boyutuna dayalı dinamik ölçüler
    val screenWidth = getScreenWidth()
    val headerHeight = screenWidth * 0.20f
    val iconSize = screenWidth * 0.085f
    val titleFontSize = getResponsiveFontSize(0.058f)
    val categoryIconSize = screenWidth * 0.12f
    val categoryTextSize = getResponsiveFontSize(0.040f)
    val subCategoryFontSize = getResponsiveFontSize(0.034f)
    val listItemHeight = screenWidth * 0.23f
    val listItemIconSize = screenWidth * 0.18f

    // Tema renkleri
    val themeBackgroundColor = getThemeBackgroundColor(darkTheme)
    val themeTextColor = getThemeTextColor(darkTheme)
    val themeInverseTextColor = getThemeInverseTextColor(darkTheme)

    // Kahvehane kategorisini özel durumlar için hafızada tutar
    val kahvehaneCategory = remember(listCategories) { listCategories.find { it.name == "Kahvehane" } }

    // --- Yan Etkiler (Side Effects) ---

    // Kategori değiştiğinde alt kategori seçimlerini ve sürükleme durumlarını sıfırlar
    LaunchedEffect(selectedCategoryId) {
        selectedSubCategory = if (selectedCategoryId == kahvehaneCategory?.id) CoffeeHouseCategoriesEnum.entries.first() else null
        draggedItemIndex = null
        draggedOverIndex = null
        draggedItemOffset = 0f
    }

    // İlk açılışta veya kategoriler yüklendiğinde varsayılan bir kategori seçer
    LaunchedEffect(listCategories) {
        if (listCategories.isNotEmpty() && (selectedCategoryId == null || listCategories.none { it.id == selectedCategoryId })) {
            selectedCategoryId = listCategories.firstOrNull()?.id
        }
    }

    // Seçili kategoriye ve alt kategoriye göre ürünleri filtreler
    val filteredProducts = remember(selectedCategoryId, selectedSubCategory, listProducts) {
        val base = if (selectedCategoryId == null) listProducts else listProducts.filter { it.categoryId == selectedCategoryId }
        if (selectedCategoryId == kahvehaneCategory?.id && selectedSubCategory != null) {
            base.filter { it.chcategory == selectedSubCategory?.chCategoryName }.toMutableStateList()
        } else {
            base.toMutableStateList()
        }
    }

    // --- Tasarım Bileşenleri ---

    // Ürün silme onay diyaloğu
    if (showDialog && productToDelete != null) {
        DeleteDialog(
            informationText = "Ürünü Silmek İstediğinize Emin Misiniz!",
            textColor = Color.Black,
            onDismissRequest = { showDialog = false },
            onDelete = {
                productsViewModel.deleteProduct(productToDelete!!)
                showDialog = false
            })
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = themeBackgroundColor,
        floatingActionButton = {
            // Yeni ürün ekleme butonu
            FloatingActionButton(
                onClick = {
                    val intent = Intent(activity, EditProductActivity::class.java)
                    intent.putExtra("darkTheme", darkTheme)
                    activity.startActivity(intent)
                },
                containerColor = themeTextColor,
                contentColor = themeInverseTextColor,
                shape = CircleShape,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Ürün Ekle")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Geri butonu ve başlık
            EditHeader(
                themeTextColor,
                headerHeight,
                iconSize,
                titleFontSize,
                onBackClick = { activity.finish() })

            // Kategori seçimi ve kategori düzenleme sayfasına geçiş
            EditCategorySelector(
                categories = listCategories,
                selectedCategoryId = selectedCategoryId,
                onCategorySelected = { selectedCategoryId = it },
                onEditCategoriesClick = {
                    val intent = Intent(activity, EditCategoryActivity::class.java)
                    intent.putExtra("darkTheme", darkTheme)
                    activity.startActivity(intent)
                },
                darkTheme = darkTheme,
                themeTextColor = themeTextColor,
                categoryIconSize = categoryIconSize,
                categoryTextSize = categoryTextSize,
                screenWidth = screenWidth
            )

            // Toplam ürün sayısı bilgisi
            Text(
                text = "Listelenen Toplam Ürün Sayısı: ${filteredProducts.size}",
                color = themeTextColor,
                modifier = Modifier.padding(vertical = 8.dp),
                fontSize = categoryTextSize
            )

            // Eğer Kahvehane kategorisi seçiliyse alt kategorileri göster
            if (selectedCategoryId == kahvehaneCategory?.id) {
                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    CoffeeHouseCategoriesEnum.entries.forEach { sub ->
                        val isSel = selectedSubCategory == sub
                        Box(
                            modifier = Modifier.weight(1f).background(if (isSel) themeTextColor else Color.Transparent, RoundedCornerShape(12.dp))
                                .clickable { selectedSubCategory = if (isSel) null else sub }.padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = sub.chCategoryName, color = if (isSel) themeInverseTextColor else themeTextColor, fontSize = subCategoryFontSize)
                        }
                    }
                }
            }

            // Ürünlerin listelendiği alan (Sürükle-bırak desteği ile)
            LazyColumn(
                state = lazyListState,
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 10.dp, bottom = 100.dp)
            ) {
                itemsIndexed(items = filteredProducts, key = { _, p -> p.id }) { index, product ->
                    EditProductItem(
                        product = product,
                        isDragged = draggedItemIndex == index,
                        offsetAnim = if (draggedItemIndex == index) draggedItemOffset else 0f,
                        themeTextColor = themeTextColor,
                        listItemHeight = listItemHeight,
                        listItemIconSize = listItemIconSize,
                        categoryTextSize = categoryTextSize,
                        iconSize = iconSize,
                        onDragModifier = Modifier.pointerInput(index) {
                            detectDragGesturesAfterLongPress(
                                onDragStart = {
                                    draggedItemIndex = index; draggedOverIndex = index
                                },
                                onDragEnd = {
                                    draggedItemIndex?.let { start ->
                                        draggedOverIndex?.let { target ->
                                            if (start != target) {
                                                val item = filteredProducts.removeAt(start)
                                                filteredProducts.add(target, item)
                                                // Yeni sıralamayı veritabanında güncelle
                                                productsViewModel.updateProducts(filteredProducts.mapIndexed { i, p ->
                                                    p.copy(
                                                        orderIndex = i
                                                    )
                                                })
                                            }
                                        }
                                    }
                                    draggedItemIndex = null; draggedOverIndex =
                                    null; draggedItemOffset = 0f
                                },
                                onDragCancel = {
                                    draggedItemIndex = null; draggedOverIndex =
                                    null; draggedItemOffset = 0f
                                },
                                onDrag = { change, dragAmount ->
                                    change.consume()
                                    draggedItemOffset += dragAmount.y
                                    draggedItemIndex?.let { start ->
                                        // Ürünün hangi indeks üzerine geldiğini hesapla
                                        draggedOverIndex =
                                            (start + (draggedItemOffset / 95.dp.toPx()).roundToInt()).coerceIn(
                                                0,
                                                filteredProducts.size - 1
                                            )
                                    }
                                }
                            )
                        },
                        onEditClick = {
                            val intent = Intent(activity, EditProductActivity::class.java)
                            intent.putExtra("productJson", Gson().toJson(product))
                            activity.startActivity(intent)
                        },
                        onDeleteClick = { productToDelete = product; showDialog = true }
                    )
                }
            }
        }
    }
}
