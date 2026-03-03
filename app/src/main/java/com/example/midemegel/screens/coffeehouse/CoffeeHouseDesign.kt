package com.example.midemegel.screens.coffeehouse

import android.annotation.SuppressLint
import android.graphics.Paint
import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.midemegel.R
import com.example.midemegel.components.common.FancyAnimatedCounter_Fixed
import com.example.midemegel.screens.coffeehouse.coffeehousecomponents.CoffeeCup
import com.example.midemegel.screens.coffeehouse.coffeehousecomponents.ProductTrayItem
import com.example.midemegel.screens.coffeehouse.coffeehousecomponents.SizeSelector
import com.example.midemegel.components.dialogs.dialograting.OpenAlertDialogRating
import com.example.midemegel.helpers.enums.CoffeeHouseCategoriesEnum
import com.example.midemegel.helpers.enums.SizeEnum
import com.example.midemegel.helpers.toComposeColor
import com.example.midemegel.data.model.BagProductModel
import com.example.midemegel.data.model.ProductModel
import com.example.midemegel.helpers.utils.getContrastTextColor
import com.example.midemegel.helpers.utils.getResponsiveFontSize
import com.example.midemegel.helpers.utils.getThemeTextColor
import com.example.midemegel.helpers.utils.pxToDp
import com.example.midemegel.data.viewmodel.BagProductsViewModel
import com.example.midemegel.data.viewmodel.ProductsViewModel
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * "Kahvehane" kategorisine özel olarak tasarlanmış interaktif içecek hazırlama ekranı.
 * Kullanıcıların içecek seçmesini, boyut belirlemesini ve animasyonlu bir şekilde sepete eklemesini sağlar.
 */
@SuppressLint("ContextCastToActivity", "UnrememberedMutableState", "LocalContextResourcesRead")
@Composable
fun CoffeeHouseDesign(indx: Int = 11, darkTheme: Boolean, productCounts: (Int) -> Unit) {
    // --- Mantıksal Değişkenler ve Durum Yönetimi ---
    
    // ViewModels
    val viewModel: ProductsViewModel = hiltViewModel()
    val modelViewBag: BagProductsViewModel = hiltViewModel()
    val list by viewModel.productsState.collectAsState()
    
    // UI Durumları
    val state = rememberLazyListState()
    val listImage = remember { mutableStateListOf("tea_glass_icon", "coffee_glass_icon", "juice_glass_icon") }
    val listImageMask = remember { mutableStateListOf("tea_glass_mask_icon", "coffee_glass_mask_icon", "juice_glass_mask_icon") }

    // Ürünleri kategorilerine göre ayıran liste (Çay, Kahve, Meyve Suyu)
    val products = remember {
        mutableStateListOf(
            mutableStateListOf<ProductModel>(),
            mutableStateListOf<ProductModel>(),
            mutableStateListOf<ProductModel>()
        )
    }

    var selectedTrayIndex by remember { mutableIntStateOf(0) } // Seçili içecek türü indeksi
    var selectedProduct by remember { mutableStateOf<ProductModel?>(null) } // Seçili ürün
    var initialScrollIndex by remember { mutableIntStateOf(-1) }
    var machineSize by remember { mutableStateOf(IntSize.Zero) } // Makine görselinin boyutu
    var initialScrollDone by remember { mutableStateOf(false) }
    
    // Dinamik boyutlandırma hesaplamaları
    val cupBottom = machineSize.height * 0.075f
    val cupBaseSizePx = machineSize.width * 0.27f
    val cupBaseSizeDp = with(LocalDensity.current) { cupBaseSizePx.toDp() }
    val smallRatio = 0.20f
    val mediumRatio = 0.24f
    val largeRatio = 0.27f
    var totalHeight by remember { mutableStateOf(0) }
    val bottomSectionHeightPx = totalHeight - machineSize.height
    val dynamicPadding = with(LocalDensity.current) { (bottomSectionHeightPx * 0.04f).toDp() }
    val dynamicFontSp = getResponsiveFontSize(0.025f)
    val dynamicPriceFontSp = getResponsiveFontSize(0.065f)
    val buttonHeight = with(LocalDensity.current) { (bottomSectionHeightPx * 0.15f).toDp() }
    val buttonWidth = with(LocalDensity.current) { (machineSize.width * 0.50f).toDp() }
    val iconSize = with(LocalDensity.current) { (bottomSectionHeightPx * 0.06f).toDp() }

    val currentList = if (selectedTrayIndex in 0..2) products[selectedTrayIndex] else emptyList()

    // --- Yan Etkiler (Side Effects) ---

    // Ürün listesi yüklendiğinde veya değiştiğinde kategorilere ayırma işlemini yapar
    LaunchedEffect(list, indx) {
        if (list.isNotEmpty()) {
            val currentSelectedProductId = selectedProduct?.id
            val currentSelectedTrayIndex = selectedTrayIndex
            products.forEach { it.clear() }
            list.forEach { product ->
                when (product.chcategory) {
                    CoffeeHouseCategoriesEnum.Cay.chCategoryName -> products[0].add(product)
                    CoffeeHouseCategoriesEnum.Kahve.chCategoryName -> products[1].add(product)
                    CoffeeHouseCategoriesEnum.Meyve_Suyu.chCategoryName -> products[2].add(product)
                }
            }
            // Başlangıçta odaklanılacak ürünü belirler
            if (indx != -1 && currentSelectedProductId == null) {
                val targetProduct = list.find { it.id == indx }
                if (targetProduct != null) {
                    selectedProduct = targetProduct
                    products.forEachIndexed { trayIdx, productList ->
                        val productIndex = productList.indexOfFirst { it.id == indx }
                        if (productIndex != -1) {
                            selectedTrayIndex = trayIdx
                            initialScrollIndex = productIndex
                        }
                    }
                }
            } else if (currentSelectedProductId != null) {
                val updatedProduct = list.find { it.id == currentSelectedProductId }
                if (updatedProduct != null) {
                    selectedProduct = updatedProduct
                    products.forEachIndexed { trayIdx, productList ->
                        val productIndex = productList.indexOfFirst { it.id == currentSelectedProductId }
                        if (productIndex != -1 && trayIdx == currentSelectedTrayIndex) {
                            initialScrollIndex = productIndex
                        }
                    }
                }
            }
        }
    }

    // İlk açılışta seçili ürüne kaydırma yapar
    LaunchedEffect(currentList, initialScrollIndex) {
        if (!initialScrollDone && initialScrollIndex != -1 && currentList.isNotEmpty()) {
            awaitFrame()
            delay(50)
            state.animateScrollToItem(initialScrollIndex, -state.layoutInfo.viewportEndOffset / 3)
            initialScrollDone = true
        }
    }

    val activity = LocalContext.current
    val scope = rememberCoroutineScope()
    val productModel = selectedProduct ?: list.find { it.id == indx } ?: list.firstOrNull()
    
    // Form ve animasyon durumları
    var selectedSize by remember { mutableStateOf(SizeEnum.Medium) }
    val currentColor = productModel?.color.toComposeColor()
    var productCount by remember { mutableIntStateOf(1) }
    var rc by remember(productModel) { mutableIntStateOf(productModel?.ratingCount ?: 0) }
    var showDialog by remember { mutableStateOf(false) }
    var isFillingAnimationActive by remember { mutableStateOf(false) }
    var isCupFull by remember { mutableStateOf(false) }

    // Sepete ekleme animasyonu için progress değeri
    val fillProgressButton = remember { Animatable(0f) }
    
    // Tema renkleri
    val itemBackgroundColorUnselected = if (!darkTheme) Color(0xFFE5E5E5) else Color(0xFF827AC0)
    val itemTextColorUnselected = if (!darkTheme) Color(0xFF8A8A8A) else Color(0xFF1C1556)
    val contrastTextColor = if (productModel != null) getContrastTextColor(productModel) else Color.White
    val themeTextColor = getThemeTextColor(darkTheme)

    // Bardak dolum animasyonu progress ve damla efektleri
    val dropsAlpha by animateFloatAsState(
        targetValue = if (isFillingAnimationActive) 1f else 0f,
        animationSpec = tween(durationMillis = 1000), label = "dropsAlpha"
    )
    val fillProgress by animateFloatAsState(
        targetValue = if (isCupFull) 1f else 0f,
        animationSpec = if (isCupFull) tween(durationMillis = 8000) else tween(durationMillis = 1000), label = "fillProgress"
    )
    val animatedCupSize by animateDpAsState(
        targetValue = when (selectedSize) {
            SizeEnum.Small -> with(LocalDensity.current) { (machineSize.width * smallRatio).toDp() }
            SizeEnum.Medium -> with(LocalDensity.current) { (machineSize.width * mediumRatio).toDp() }
            SizeEnum.Large -> with(LocalDensity.current) { (machineSize.width * largeRatio).toDp() }
        },
        animationSpec = tween(500), label = "responsiveCupSize"
    )

    // Ürün sayısını üst bileşene bildir
    LaunchedEffect(currentList) { productCounts(currentList.size) }

    // Dolum animasyonu tamamlandığında ürünü sepete ekle
    LaunchedEffect(isCupFull) {
        if (isCupFull) {
            delay(8000)
            isFillingAnimationActive = false
            delay(1000)
            isCupFull = false
            productModel?.let { model ->
                val bagProduct = BagProductModel(
                    id = 0, originalId = model.id, image = model.image, name = model.name,
                    categoryId = model.categoryId, chcategory = model.chcategory, color = model.color,
                    price = model.price, ingredients = model.ingredients, description = model.description,
                    favorite = model.favorite, ratingSize = model.ratingSize, ratingCount = model.ratingCount,
                    myRatingSize = model.myRatingSize, count = 0
                )
                modelViewBag.addOrUpdateProduct(bagProduct, productCount)
                Toast.makeText(activity, "$productCount adet ${model.name} sepete eklendi.", Toast.LENGTH_LONG).show()
                productCount = 1
            }
        }
    }

    // --- Tasarım Bileşenleri ---
    Column(
        modifier = Modifier.fillMaxSize().onGloballyPositioned { totalHeight = it.size.height },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (productModel != null) {
            // Üst Bölüm: Kahve Makinesi ve Bardaklar
            Box(modifier = Modifier.weight(50f)) {
                Box(modifier = Modifier.wrapContentSize(), contentAlignment = Alignment.BottomCenter) {
                    Image(
                        painter = painterResource(id = R.drawable.machine),
                        contentDescription = null,
                        modifier = Modifier.wrapContentHeight().align(Alignment.Center).onGloballyPositioned { machineSize = it.size }
                    )

                    Row(
                        modifier = Modifier.wrapContentWidth().padding(bottom = pxToDp(cupBottom)),
                        horizontalArrangement = Arrangement.spacedBy(0.dp, Alignment.CenterHorizontally),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        repeat(3) { index ->
                            val resId = activity.resources.getIdentifier(listImage[index], "drawable", activity.packageName).let { if (it == 0) R.drawable.tea_glass_icon else it }
                            val maskResId = activity.resources.getIdentifier(listImageMask[index], "drawable", activity.packageName).let { if (it == 0) R.drawable.tea_glass_mask_icon else it }

                            CoffeeCup(
                                resId = resId, maskResId = maskResId, isSelected = selectedTrayIndex == index,
                                currentColor = currentColor, fillProgress = fillProgress, dropsAlpha = dropsAlpha,
                                cupBaseSizeDp = cupBaseSizeDp, animatedCupSize = animatedCupSize,
                                standardCupSize = with(LocalDensity.current) { (machineSize.width * mediumRatio).toDp() },
                                extraHeight = with(LocalDensity.current) { (machineSize.width * 0.09f).toDp() },
                                onClick = {
                                    scope.launch {
                                        selectedTrayIndex = index
                                        if (products[index].isNotEmpty()) selectedProduct = products[index][0]
                                        isCupFull = false
                                        isFillingAnimationActive = false
                                        state.animateScrollToItem(0)
                                    }
                                }
                            )
                        }
                    }
                }
            }

            // Alt Bölüm: Ürün Bilgileri ve Kontroller
            Column(
                modifier = Modifier.weight(50f).padding(top = dynamicPadding * 2, bottom = dynamicPadding, start = dynamicPadding, end = dynamicPadding),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Adet sayacı
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    FancyAnimatedCounter_Fixed(itemBackgroundColorUnselected, currentColor, contrastTextColor, productCount, removeProduct = { if (productCount > 1) productCount-- }, addProduct = { productCount++ })
                }

                // Boyut seçici (Small, Medium, Large)
                SizeSelector(
                    selectedSize = selectedSize, onSizeSelected = { selectedSize = it },
                    currentColor = currentColor, itemBackgroundColorUnselected = itemBackgroundColorUnselected,
                    contrastTextColor = contrastTextColor, itemTextColorUnselected = itemTextColorUnselected,
                    maskResId = activity.resources.getIdentifier(listImageMask[selectedTrayIndex], "drawable", activity.packageName).let { if (it == 0) R.drawable.tea_glass_mask_icon else it },
                    iconSize = iconSize, fontSize = dynamicFontSp
                )

                // Ürün tepsisi (Yatay kaydırılabilir ürün listesi)
                LazyRow(
                    modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                    state = state,
                    horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
                ) {
                    itemsIndexed(items = currentList, key = { _, item -> item.id }) { index, item ->
                        ProductTrayItem(
                            item = item, isSelected = item == selectedProduct,
                            currentColor = currentColor, itemBackgroundColorUnselected = itemBackgroundColorUnselected,
                            contrastTextColor = contrastTextColor, itemTextColorUnselected = itemTextColorUnselected,
                            fontSize = dynamicFontSp,
                            onProductClick = {
                                scope.launch {
                                    selectedProduct = item
                                    isCupFull = false
                                    isFillingAnimationActive = false
                                    state.animateScrollToItem(index)
                                }
                            },
                            onFavoriteClick = { updatedItem ->
                                val finalItem = updatedItem.copy(favorite = if (updatedItem.favorite == 1) 0 else 1)
                                products.forEach { tray ->
                                    val idx = tray.indexOfFirst { it.id == finalItem.id }
                                    if (idx != -1) tray[idx] = finalItem
                                }
                                if (selectedProduct?.id == finalItem.id) selectedProduct = finalItem
                                viewModel.updateProduct(finalItem)
                            },
                            onRatingClick = {
                                selectedProduct = item
                                showDialog = true
                            }
                        )
                    }
                }

                // Değerlendirme diyaloğu
                if (showDialog) {
                    OpenAlertDialogRating(
                        onDismissRequest = { showDialog = false },
                        onRatingSubmit = { newRating ->
                            val currentRC = productModel.ratingCount ?: 0
                            val newRC = if (rc == currentRC) currentRC + 1 else currentRC
                            val updatedProduct = productModel.copy(myRatingSize = newRating, ratingCount = newRC)
                            products.forEach { tray ->
                                val idx = tray.indexOfFirst { it.id == updatedProduct.id }
                                if (idx != -1) tray[idx] = updatedProduct
                            }
                            viewModel.updateProduct(updatedProduct)
                            selectedProduct = updatedProduct
                            rc = newRC
                            showDialog = false
                        },
                        rs = productModel.myRatingSize ?: 0f, rc = productModel.ratingCount ?: 0, color = currentColor
                    )
                }

                // Fiyat ve Sepete Ekle butonu
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${productModel.price}₺",
                        color = currentColor,
                        fontSize = dynamicPriceFontSp,
                        style = TextStyle.Default.copy(shadow = Shadow(color = themeTextColor, offset = Offset(0f, 0f), blurRadius = 10f))
                    )

                    // Animasyonlu "Sepete Ekle" butonu (Progress bar içeren özel tasarım)
                    Box(
                        modifier = Modifier.width(buttonWidth).height(buttonHeight).clip(RoundedCornerShape(24.dp)).border(1.dp, themeTextColor, RoundedCornerShape(24.dp))
                            .pointerInput(Unit) {
                                detectTapGestures {
                                    scope.launch {
                                        if (!isCupFull) {
                                            isFillingAnimationActive = true
                                            isCupFull = true
                                            Toast.makeText(activity, "Hazırlanıyor...", Toast.LENGTH_SHORT).show()
                                        }
                                        fillProgressButton.snapTo(0f)
                                        fillProgressButton.animateTo(1f, animationSpec = tween(8000, easing = LinearEasing))
                                    }
                                }
                            }
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val progress = fillProgressButton.value
                            val progressWidth = size.width * progress
                            if (progress == 0f) drawRect(currentColor, size = size)
                            drawRect(color = currentColor, size = Size(progressWidth, size.height))

                            val text = "Sepete Ekle"
                            val baseTextColor = if (progress == 0f) contrastTextColor else Color.Transparent

                            val paintBase = Paint().apply {
                                color = baseTextColor.toArgb(); textSize = 40f; isFakeBoldText = true; textAlign = Paint.Align.CENTER; isAntiAlias = true
                            }
                            val paintFill = Paint().apply {
                                color = contrastTextColor.toArgb(); textSize = 40f; isFakeBoldText = true; textAlign = Paint.Align.CENTER; isAntiAlias = true
                            }

                            val x = size.width / 2
                            val y = size.height / 2 - (paintBase.descent() + paintBase.ascent()) / 2
                            drawContext.canvas.nativeCanvas.drawText(text, x, y, paintBase)
                            drawContext.canvas.save()
                            drawContext.canvas.clipRect(0f, 0f, progressWidth, size.height)
                            drawContext.canvas.nativeCanvas.drawText(text, x, y, paintFill)
                            drawContext.canvas.restore()
                        }
                    }
                }
            }
        }
    }
}
