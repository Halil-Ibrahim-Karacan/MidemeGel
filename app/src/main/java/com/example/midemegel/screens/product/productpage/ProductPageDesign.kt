package com.example.midemegel.screens.product.productpage

import android.annotation.SuppressLint
import android.app.Activity
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.midemegel.R
import com.example.midemegel.components.common.MyOutlinedButton
import com.example.midemegel.components.dialogs.dialograting.OpenAlertDialogRating
import com.example.midemegel.screens.product.productpage.productpagecomponents.ProductDetailsSection
import com.example.midemegel.screens.product.productpage.productpagecomponents.ProductImageSection
import com.example.midemegel.screens.product.productpage.productpagecomponents.ProductPageTopBar
import com.example.midemegel.helpers.toComposeColor
import com.example.midemegel.data.model.BagProductModel
import com.example.midemegel.data.model.ProductModel
import com.example.midemegel.helpers.utils.getContrastTextColor
import com.example.midemegel.helpers.utils.getResponsiveFontSize
import com.example.midemegel.helpers.utils.getScreenWidth
import com.example.midemegel.helpers.utils.getTopIconSize
import com.example.midemegel.data.viewmodel.BagProductsViewModel
import com.example.midemegel.data.viewmodel.CategoriesViewModel
import com.example.midemegel.data.viewmodel.ProductsViewModel

/**
 * Seçilen ürünün detaylarının gösterildiği sayfa tasarımı.
 * Ürün görseli, açıklaması, puanlaması ve sepete ekleme butonunu içerir.
 * Kategoriye özel animasyon hızlarını destekler.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@SuppressLint("ContextCastToActivity")
@Composable
fun ProductPageDesign(
    navController: NavController,
    productModel: ProductModel,
    paddingValues: PaddingValues,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    // --- Mantıksal Değişkenler ve Durum Yönetimi ---
    
    // Görünürlük ve diyalog durumları
    var showDialog by remember { mutableStateOf(false) }
    var visibleState by remember { mutableStateOf(false) } // Sayfa giriş animasyonu için
    val visibleDuration = 1000
    var rc by remember { mutableIntStateOf(productModel.ratingCount) }
    val activity = (LocalContext.current as Activity)

    // ViewModels ve UI durumları
    val rotation = remember { Animatable(0f) }
    val scrollState = rememberScrollState()
    val modelViewBag: BagProductsViewModel = hiltViewModel()
    val modelView: ProductsViewModel = hiltViewModel()
    val categoriesViewModel: CategoriesViewModel = hiltViewModel()

    // Ürün spesifik durumlar
    var isFavorite by remember { mutableStateOf(productModel.favorite == 1) }
    val favoriteIcon = if (isFavorite) R.drawable.favorite_selected_icon else R.drawable.favorite_unselected_icon
    val currentColor = productModel.color.toComposeColor()
    val contrastTextColor = getContrastTextColor(productModel)

    // Ekran boyutuna dayalı ölçüler
    val screenWidth = getScreenWidth()
    val productImagePadding = screenWidth * 0.12f
    val starIconSize = screenWidth * 0.05f
    val productNameFontSize = getResponsiveFontSize(0.055f)
    val ratingTextFontSize = getResponsiveFontSize(0.030f)
    val priceFontSize = getResponsiveFontSize(0.065f)
    val addToCartButtonWidth = screenWidth * 0.40f
    val topIconsSize = getTopIconSize()

    // Kategori verilerini topla (State olarak dinle)
    val categories by categoriesViewModel.categoriesState.collectAsState()
    
    // Animasyon hızı ve tipi (Derived state ile otomatik güncellenir)
    val category = remember(categories, productModel.categoryId) {
        categories.find { it.id == productModel.categoryId }
    }
    val categoryAnimation = category?.animation ?: ""
    val animationSpeed = category?.animationSpeed ?: 2

    // --- Yan Etkiler (Side Effects) ---

    // Sürekli dönme animasyonu (Hız değiştiğinde yeniden başlar)
    LaunchedEffect(animationSpeed) {
        while (true) {
            rotation.animateTo(
                targetValue = rotation.value + 360f,
                animationSpec = tween(durationMillis = animationSpeed * 10000, easing = LinearEasing)
            )
        }
    }

    // Sayfa açıldığında giriş animasyonlarını başlatır
    LaunchedEffect(Unit) { visibleState = true }

    // Ürün görseli için zıplama animasyonu
    val infiniteTransition = rememberInfiniteTransition(label = "bounce")
    val bounceOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -20f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = animationSpeed * 1000, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bounceOffset"
    )


    // Sayfa şeffaflık animasyonu
    val alpha: Float by animateFloatAsState(
        targetValue = if (visibleState) 1f else 0f,
        animationSpec = tween(durationMillis = visibleDuration), label = "alpha"
    )

    var productCount by remember { mutableIntStateOf(1) }
    val imageScale = remember { Animatable(0f) }

    // Görselin ölçeklenme animasyonu
    LaunchedEffect(key1 = Unit) {
        imageScale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = visibleDuration, easing = LinearOutSlowInEasing)
        )
    }

    // --- Tasarım Bileşenleri ---
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.alpha(alpha).background(currentColor)) {
            // Üst Bölüm: Ürün Görseli ve Adet Seçimi
            Box(modifier = Modifier.weight(50f)) {
                ProductImageSection(
                    productModel = productModel, currentColor = currentColor,
                    contrastTextColor = contrastTextColor, paddingValues = paddingValues,
                    productImagePadding = productImagePadding, categoryAnimation = categoryAnimation,
                    bounceOffset = bounceOffset, rotationValue = rotation.value,
                    imageScale = imageScale.value, productCount = productCount,
                    onProductCountChange = { productCount = it },
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope
                )
            }

            // Alt Bölüm: Ürün Detayları ve Fiyat (Animasyonlu giriş yapar)
            AnimatedVisibility(
                visible = visibleState,
                modifier = Modifier.weight(50f),
                enter = slideInVertically(initialOffsetY = { it }, animationSpec = tween(durationMillis = visibleDuration)),
                exit = slideOutVertically(targetOffsetY = { it }, animationSpec = tween(durationMillis = visibleDuration))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White, RoundedCornerShape(20.dp, 20.dp, 0.dp, 0.dp))
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(scrollState)
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Ürün İsmi, Puanlama ve Açıklama
                        ProductDetailsSection(
                            productModel = productModel,
                            currentColor = currentColor,
                            contrastTextColor = contrastTextColor,
                            productNameFontSize = productNameFontSize,
                            ratingTextFontSize = ratingTextFontSize,
                            starIconSize = starIconSize,
                            ratingCount = rc,
                            onRatingClick = { showDialog = true }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Alt Bar: Fiyat ve Sepete Ekle Butonu
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(bottom = paddingValues.calculateBottomPadding(), top = 8.dp, start = 16.dp, end = 16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${productModel.price}₺",
                            color = currentColor,
                            fontSize = priceFontSize,
                            textAlign = TextAlign.Center,
                            style = TextStyle.Default.copy(shadow = Shadow(color = Color.Black, offset = Offset(3f, 3f), blurRadius = 0f))
                        )

                        MyOutlinedButton(
                            onClick = {
                                val bagProduct = BagProductModel(
                                    id = 0, originalId = productModel.id, image = productModel.image, name = productModel.name,
                                    categoryId = productModel.categoryId, chcategory = productModel.chcategory, color = productModel.color,
                                    price = productModel.price, ingredients = productModel.ingredients, description = productModel.description,
                                    favorite = productModel.favorite, ratingSize = productModel.ratingSize, ratingCount = productModel.ratingCount,
                                    myRatingSize = productModel.myRatingSize, count = 0
                                )
                                modelViewBag.addOrUpdateProduct(bagProduct, productCount)
                                Toast.makeText(activity, "$productCount adet ${productModel.name} sepete eklendi.", Toast.LENGTH_LONG).show()
                            },
                            text = "Sepete Ekle",
                            fontSize = ratingTextFontSize,
                            modifier = Modifier.width(addToCartButtonWidth),
                            containerColor = currentColor,
                            contentColor = contrastTextColor,
                            borderColor = Color.Black
                        )
                    }
                }
            }
        }

        // En Üst Katman: Geri Dön ve Favori Butonları
        ProductPageTopBar(
            visibleState = visibleState, visibleDuration = visibleDuration,
            paddingValues = paddingValues, contrastTextColor = contrastTextColor,
            favoriteIcon = favoriteIcon, topIconsSize = topIconsSize,
            onBackClick = {
                visibleState = false
                navController.popBackStack()
            },
            onFavoriteClick = {
                productModel.favorite = if (productModel.favorite == 1) 0 else 1
                isFavorite = !isFavorite
                modelView.updateProduct(productModel)
            }
        )
    }

    // Puan verme diyaloğu
    if (showDialog) {
        OpenAlertDialogRating(
            onDismissRequest = { showDialog = false },
            onRatingSubmit = { newRating ->
                productModel.myRatingSize = newRating
                if (rc == productModel.ratingCount) {
                    rc++
                    productModel.ratingCount = rc
                }
                modelView.updateProduct(productModel)
            },
            rs = productModel.myRatingSize ?: 0f, rc = productModel.ratingCount ?: 0, color = currentColor
        )
    }
}
