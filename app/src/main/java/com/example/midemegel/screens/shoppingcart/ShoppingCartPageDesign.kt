package com.example.midemegel.screens.shoppingcart

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.midemegel.components.common.HeaderDesign
import com.example.midemegel.R
import com.example.midemegel.components.dialogs.DeleteDialog
import com.example.midemegel.data.model.BagProductModel
import com.example.midemegel.helpers.utils.getButtonFontSize
import com.example.midemegel.helpers.utils.getResponsiveFontSize
import com.example.midemegel.helpers.utils.getScreenWidth
import com.example.midemegel.helpers.utils.getThemeBackgroundColor
import com.example.midemegel.helpers.utils.getThemeTextColor
import com.example.midemegel.data.viewmodel.BagProductsViewModel
import com.example.midemegel.data.viewmodel.CategoriesViewModel
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/**
 * Kullanıcının sepetine eklediği ürünlerin listelendiği ve yönetildiği sayfa tasarımı.
 * Ürün miktarını artırma/azaltma, ürün silme ve toplam tutar hesaplama işlevlerini barındırır.
 */
@SuppressLint("ContextCastToActivity", "SuspiciousIndentation")
@Composable
fun ShoppingCartPageDesign(
    navController: NavController, 
    paddingValues: PaddingValues, 
    darkTheme: Boolean, 
    onNavigateToHome: (Int, String) -> Unit
) {
    // --- Mantıksal Değişkenler ve Durum Yönetimi ---
    
    // ViewModels
    val categoriesViewModel: CategoriesViewModel = hiltViewModel()
    val categories by categoriesViewModel.categoriesState.collectAsState()
    val modelView: BagProductsViewModel = hiltViewModel()
    val list by modelView.productsState.collectAsState() // Sepetteki ürünlerin listesi
    
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    // UI Durumları
    var showDialog by remember { mutableStateOf(false) }
    var prdct by remember { mutableStateOf<BagProductModel?>(null) } // Silinmek istenen ürün

    // Tema Renkleri
    val themeBackgroundColor = getThemeBackgroundColor(darkTheme)
    val themeTextColor = getThemeTextColor(darkTheme)

    // Ekran boyutuna dayalı dinamik ölçüler
    val screenWidth = getScreenWidth()
    val productImageSize = screenWidth * 0.18f
    val productNameFontSize = getResponsiveFontSize(0.04f)
    val priceFontSize = getResponsiveFontSize(0.035f)
    val totalAmountFontSize = getResponsiveFontSize(0.045f)
    val buttonFontSize = getButtonFontSize()
    val deleteIconSize = screenWidth * 0.06f

    // Sepetteki ürünlerin toplam tutarını hesaplar
    val totalAmount = remember(list) {
        list.sumOf { (it.price ?: 0.0) * it.count }
    }

    // Ürün silme onay diyaloğu
    if (showDialog) {
        DeleteDialog(
            informationText = "Ürünü Silmek İstediğinize Emin Misiniz!",
            textColor = Color.Black,
            onDismissRequest = { showDialog = false },
            onDelete = {
                prdct?.let { modelView.removeProduct(it.name, it.image) }
                showDialog = false
            })
    }

    // --- Tasarım Bileşenleri ---
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(themeBackgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Sayfa Başlığı (Sepet ikonu ve toplam ürün sayısı)
        HeaderDesign(
            darkTheme = darkTheme,
            productCount = list.size,
            headerIcon = R.drawable.shopping_bag_selected_icon,
            headerText = "Sepetim"
        )

        // Sepetteki ürünlerin listelendiği alan
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            itemsIndexed(
                items = list,
                key = { _, product -> product.id }
            ) { index, product ->
                
                // Her bir sepet öğesinin tasarımı
                CartItemDesign(
                    product = product,
                    themeTextColor = themeTextColor,
                    productImageSize = productImageSize,
                    productNameFontSize = productNameFontSize,
                    priceFontSize = priceFontSize,
                    deleteIconSize = deleteIconSize,
                    onProductClick = {
                        scope.launch {
                            val categoryName = categoriesViewModel.getCategoryNameById(product.categoryId)
                            // Kahvehane ürünüyse ana sayfadaki özel yapıya yönlendir
                            if (categoryName == "Kahvehane") {
                                val kahvehaneIndex = categories.indexOfFirst { it.name == "Kahvehane" }
                                onNavigateToHome(if (kahvehaneIndex != -1) kahvehaneIndex else 2, product.originalId.toString())
                            } else {
                                val productJson = Gson().toJson(product)
                                val encodedJson = URLEncoder.encode(productJson, StandardCharsets.UTF_8.name())
                                navController.navigate("detaysayfa/$encodedJson")
                            }
                        }
                    },
                    onDeleteClick = { prdct = product; showDialog = true },
                    onIncreaseCount = { modelView.addOrUpdateProduct(product, 1) },
                    onDecreaseCount = { modelView.addOrUpdateProduct(product, -1) }
                )
            }
        }

        // Alt Bölüm: Toplam Tutar ve Alışverişi Tamamla Butonu
        CartSummaryBar(
            totalAmount = totalAmount,
            themeBackgroundColor = themeBackgroundColor,
            themeTextColor = themeTextColor,
            totalAmountFontSize = totalAmountFontSize,
            buttonFontSize = buttonFontSize,
            paddingValues = paddingValues,
            screenWidth = screenWidth,
            onCheckoutClick = {
                Toast.makeText(context, "Siparişiniz Alındı.", Toast.LENGTH_SHORT).show()
                modelView.clearBag()
            }
        )
    }
}
