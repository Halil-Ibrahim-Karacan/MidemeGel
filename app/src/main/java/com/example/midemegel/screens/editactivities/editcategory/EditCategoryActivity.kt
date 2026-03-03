package com.example.midemegel.screens.editactivities.editcategory

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.midemegel.components.dialogs.CategoryEditDialog
import com.example.midemegel.components.dialogs.DeleteDialog
import com.example.midemegel.screens.editactivities.editcategory.editcategorycomponents.EditCategoryHeader
import com.example.midemegel.screens.editactivities.editcategory.editcategorycomponents.EditCategoryList
import com.example.midemegel.helpers.utils.*
import com.example.midemegel.data.model.CategoryModel
import com.example.midemegel.ui.theme.MidemeGelTheme
import com.example.midemegel.data.viewmodel.CategoriesViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Kategori yönetimi (ekleme, silme, düzenleme ve sıralama) ekranını başlatan Activity.
 */
@AndroidEntryPoint
class EditCategoryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MidemeGelTheme {
                EditCategoryScreen()
            }
        }
    }
}

/**
 * Kategori düzenleme ekranının ana Composable yapısı.
 * Kategorilerin listelenmesini ve CRUD işlemlerinin yönetilmesini sağlar.
 */
@SuppressLint("ContextCastToActivity")
@Composable
fun EditCategoryScreen(
    categoriesViewModel: CategoriesViewModel = hiltViewModel()
) {
    // --- Mantıksal Değişkenler ve Durum Yönetimi ---
    
    val activity = (LocalContext.current as Activity)
    val screenWidth = getScreenWidth()
    val darkTheme = activity.intent.getBooleanExtra("darkTheme", false)
    
    // Veritabanındaki kategorileri takip eder
    val categoriesState by categoriesViewModel.categoriesState.collectAsState()

    // Ekran boyutuna dayalı dinamik ölçüler
    val headerHeight = screenWidth * 0.20f
    val iconSize = screenWidth * 0.085f
    val titleFontSize = getResponsiveFontSize(0.058f)
    val countFontSize = getResponsiveFontSize(0.045f)
    val listItemHeight = screenWidth * 0.18f
    val listItemTextSize = getResponsiveFontSize(0.045f)

    // UI tarafında manipüle edilecek kategori listesi
    val listItems = remember(categoriesState) { categoriesState.toMutableStateList() }

    // Kategori verisi güncellendiğinde UI listesini de güncelle
    LaunchedEffect(categoriesState) {
        listItems.clear()
        listItems.addAll(categoriesState)
    }

    // Diyalog ve düzenleme durumları
    var showDialog by remember { mutableStateOf(false) }
    var showDialogDelete by remember { mutableStateOf(false) }
    var editingCategory by remember { mutableStateOf<CategoryModel?>(null) }

    // Tema renkleri
    val itemBackgroundColor = if (!darkTheme) Color(0xFFE5E5E5) else Color(0xFF827AC0)
    val itemTextColor = if (!darkTheme) Color(0xFF000000) else Color(0xFF1C1556)
    val themeBackgroundColor = getThemeBackgroundColor(darkTheme)
    val themeTextColor = getThemeTextColor(darkTheme)
    val themeInverseTextColor = getThemeInverseTextColor(darkTheme)

    val lazyListState = rememberLazyListState()

    // --- Tasarım Bileşenleri ---

    // Kategori ekleme/düzenleme diyaloğu
    if (showDialog) {
        CategoryEditDialog(
            darkTheme = darkTheme,
            itemTextColor = itemTextColor,
            category = editingCategory,
            listCategories = listItems,
            onDismiss = { showDialog = false },
            onSave = { category ->
                if (editingCategory == null) {
                    categoriesViewModel.addCategory(category)
                } else {
                    categoriesViewModel.updateCategory(category)
                }
                showDialog = false
            }
        )
    }

    // Kategori silme onay diyaloğu
    if (showDialogDelete) {
        DeleteDialog(
            informationText = "${editingCategory?.name} Kategorisini Silmek İstediğinize Emin Misiniz!\nKategori İçerisindeki Tüm Ürünler Silinir!",
            textColor = MaterialTheme.colorScheme.error,
            onDismissRequest = { showDialogDelete = false },
            onDelete = { 
                categoriesViewModel.deleteCategory(editingCategory!!)
                showDialogDelete = false 
            }
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = themeBackgroundColor,
        floatingActionButton = {
            // Yeni kategori ekleme butonu
            FloatingActionButton(
                onClick = {
                    editingCategory = null
                    showDialog = true
                },
                containerColor = themeTextColor,
                contentColor = themeInverseTextColor,
                shape = CircleShape,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Kategori Ekle")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Başlık Çubuğu
            EditCategoryHeader(
                themeTextColor = themeTextColor,
                headerHeight = headerHeight,
                iconSize = iconSize,
                titleFontSize = titleFontSize,
                onBackClick = { activity.finish() }
            )

            // Toplam kategori sayısı bilgisi
            Text(
                text = "Listelenen Toplam Kategori Sayısı: ${listItems.size}",
                color = themeTextColor,
                fontSize = countFontSize,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Sürükle-bırak destekli kategori listesi
            EditCategoryList(
                lazyListState = lazyListState,
                categories = listItems,
                itemBackgroundColor = itemBackgroundColor,
                itemTextColor = itemTextColor,
                listItemHeight = listItemHeight,
                listItemTextSize = listItemTextSize,
                iconSize = iconSize,
                onOrderChanged = { updatedList -> categoriesViewModel.updateCategories(updatedList) },
                onEditClick = { category -> editingCategory = category; showDialog = true },
                onDeleteClick = { category -> editingCategory = category; showDialogDelete = true }
            )
        }
    }
}
