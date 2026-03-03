package com.example.midemegel.screens.menu

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.SnapLayoutInfoProvider
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.gson.Gson
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.midemegel.screens.product.productitem.ProductItemDesign
import com.example.midemegel.screens.product.ProductItemDesignDarkTheme
import com.example.midemegel.helpers.utils.getScreenWidth
import com.example.midemegel.data.model.CategoryModel
import com.example.midemegel.data.viewmodel.ProductsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/**
 * Ürün kartlarının dikey bir liste (LazyColumn) içinde sergilendiği ana yapı.
 */
@OptIn(ExperimentalFoundationApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun MenuList(
    navController: NavController,
    categoryIndex: Int,
    darkTheme: Boolean,
    categories: List<CategoryModel>,
    productCount: (Int) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    // --- Mantıksal Değişkenler ve Durum Yönetimi ---
    
    val viewModel: ProductsViewModel = hiltViewModel()
    val list by viewModel.productsState.collectAsState()
    
    val density = LocalDensity.current
    val state = rememberLazyListState() 
    val snappingLayout = remember(state) { SnapLayoutInfoProvider(state) }
    val flingBehavior = rememberSnapFlingBehavior(snappingLayout)
    val coroutineScope = rememberCoroutineScope()

    val screenWidth = getScreenWidth()
    val verticalSpacing = screenWidth * 0.12f

    var selectedIndex by rememberSaveable { mutableIntStateOf(-1) }
    var columnHeight by rememberSaveable { mutableIntStateOf(0) }
    var firstItemHeight by remember { mutableIntStateOf(0) }

    // Navigasyon rotası takibi
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    // Seçili kategoriye göre ürün listesini filtreler
    val filteredList by remember(list, categoryIndex, categories) {
        derivedStateOf {
            if (categories.isNotEmpty() && categoryIndex in categories.indices) {
                val targetCategoryId = categories[categoryIndex].id
                list.filter { it.categoryId == targetCategoryId }
                    .sortedBy { it.orderIndex }
            } else {
                emptyList()
            }
        }
    }

    // Ürün sayısı değiştiğinde üst bileşene haber verir
    LaunchedEffect(filteredList.size) {
        productCount(filteredList.size)
    }

    // İlk yükleme ve kategori değişimi senkronizasyonu
    var lastCategoryIndex by rememberSaveable { mutableIntStateOf(-1) }
    LaunchedEffect(categoryIndex) {
        // Eğer kategori gerçekten değiştiyse (detaydan dönüş değilse) başa sar
        if (lastCategoryIndex != -1 && lastCategoryIndex != categoryIndex) {
            state.scrollToItem(0)
            selectedIndex = 0
        }
        lastCategoryIndex = categoryIndex
    }

    // Detay sayfasından dönüldüğünde konumu geri yükle
    LaunchedEffect(currentRoute) {
        if (currentRoute == "anasayfa" && selectedIndex >= 0) {
            delay(100)
            state.scrollToItem(selectedIndex)
        }
    }

    // Kaydırma sırasında ekranın merkezindeki öğeyi takip et
    val centeredItemIndex by remember(state.firstVisibleItemIndex, state.firstVisibleItemScrollOffset) {
        derivedStateOf {
            val visibleItems = state.layoutInfo.visibleItemsInfo
            if (visibleItems.isEmpty()) return@derivedStateOf -1
            val firstVisible = state.firstVisibleItemIndex
            val scrollOffset = state.firstVisibleItemScrollOffset
            if (scrollOffset > (visibleItems.firstOrNull()?.size?.div(2) ?: 0)) firstVisible + 1 else firstVisible
        }
    }

    LaunchedEffect(centeredItemIndex) {
        if (centeredItemIndex != -1 && (state.isScrollInProgress || selectedIndex == -1)) {
            selectedIndex = centeredItemIndex
        }
    }

    val verticalPadding by remember(columnHeight, firstItemHeight) {
        derivedStateOf {
            if (columnHeight > 0) {
                val itemHeight = if (firstItemHeight > 0) firstItemHeight else with(density) { (screenWidth * 0.85f).toPx().toInt() }
                ((columnHeight - itemHeight) / 2).coerceAtLeast(0)
            } else { 0 }
        }
    }

    // --- Tasarım Bileşenleri ---
    Box {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .onGloballyPositioned { if (it.size.height > 0) columnHeight = it.size.height },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(verticalSpacing),
            state = state,
            flingBehavior = flingBehavior,
            contentPadding = PaddingValues(vertical = with(density) { verticalPadding.toDp() })
        ) {
            items(count = filteredList.size, key = { filteredList[it].id }) { index ->
                val onHeightMeasured: (Int) -> Unit = { height ->
                    if (index == 0) firstItemHeight = height
                }

                val productCategory = remember(categories, filteredList[index].categoryId) {
                    categories.find { it.id == filteredList[index].categoryId }
                }
                val animationType = productCategory?.animation ?: ""
                val animationSpeed = productCategory?.animationSpeed ?: 2

                if (!darkTheme) {
                    ProductItemDesign(
                        currentSelectedIndex = selectedIndex,
                        index = index,
                        productModel = filteredList[index],
                        animationType = animationType,
                        animationSpeed = animationSpeed,
                        onFavoriteUpdate = { viewModel.updateProduct(it) },
                        onCardClick = {
                            coroutineScope.launch {
                                selectedIndex = index
                                withFrameNanos { }
                                state.animateScrollToItem(index)
                                val productJson = Gson().toJson(filteredList[index])
                                val encodedJson =
                                    URLEncoder.encode(productJson, StandardCharsets.UTF_8.name())
                                navController.navigate(route = "detaysayfa/$encodedJson")
                            }
                        },
                        onHeightMeasured = onHeightMeasured,
                        sharedTransitionScope = sharedTransitionScope,
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                } else {
                    ProductItemDesignDarkTheme(
                        currentSelectedIndex = selectedIndex,
                        index = index,
                        productModel = filteredList[index],
                        animationType = animationType,
                        animationSpeed = animationSpeed,
                        onFavoriteUpdate = { viewModel.updateProduct(it) },
                        onCardClick = {
                            coroutineScope.launch {
                                selectedIndex = index
                                state.animateScrollToItem(index)
                                val productJson = Gson().toJson(filteredList[index])
                                val encodedJson =
                                    URLEncoder.encode(productJson, StandardCharsets.UTF_8.name())
                                navController.navigate("detaysayfa/$encodedJson")
                            }
                        },
                        onHeightMeasured = onHeightMeasured,
                        sharedTransitionScope = sharedTransitionScope,
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                }
            }
        }
    }
}
