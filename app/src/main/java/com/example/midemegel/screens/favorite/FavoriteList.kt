package com.example.midemegel.screens.favorite

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
import androidx.compose.runtime.collectAsState
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
import com.example.midemegel.data.viewmodel.ProductsViewModel
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import com.example.midemegel.screens.product.productitem.ProductItemDesign
import com.example.midemegel.screens.product.ProductItemDesignDarkTheme
import com.example.midemegel.helpers.utils.getScreenWidth
import com.example.midemegel.data.viewmodel.CategoriesViewModel

/**
 * Favoriye eklenmiş ürünlerin listelendiği dikey menü yapısı.
 * Ürünler kategorilerine göre gruplanmış ve sıralanmış şekilde gösterilir.
 */
@OptIn(ExperimentalFoundationApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun FavoriteList(
    navController: NavController,
    darkTheme: Boolean,
    productCount: (Int) -> Unit,
    onNavigateToHome: (Int, String) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    // --- Mantıksal Değişkenler ve Durum Yönetimi ---
    
    // ViewModels
    val categoriesViewModel: CategoriesViewModel = hiltViewModel()
    val categories by categoriesViewModel.categoriesState.collectAsState()
    val viewModel: ProductsViewModel = hiltViewModel()
    val list by viewModel.productsState.collectAsState()
    
    // UI Yardımcıları
    val density = LocalDensity.current
    val state = rememberLazyListState()
    val snappingLayout = remember(state) { SnapLayoutInfoProvider(state) }
    val flingBehavior = rememberSnapFlingBehavior(snappingLayout)
    val coroutineScope = rememberCoroutineScope()

    // Ekran boyutuna dayalı ölçüler
    val screenWidth = getScreenWidth()
    val verticalSpacing = screenWidth * 0.12f

    // Favori ürünleri filtrele ve kategorilerine göre sırala
    val filteredList by remember(list, categories) {
        derivedStateOf {
            list
                .filter { it.favorite == 1 }
                .sortedWith(
                    compareBy(
                        { categories.indexOfFirst { category -> category.id == it.categoryId } },
                        { it.id }
                    )
                )
        }
    }

    // Toplam favori ürün sayısını üst bileşene bildir
    LaunchedEffect(filteredList.size) {
        productCount(filteredList.size)
    }

    // Seçim ve boyut durumları
    var selectedIndex by rememberSaveable { mutableIntStateOf(-1) }
    var columnHeight by rememberSaveable { mutableIntStateOf(0) }
    var firstItemHeight by remember { mutableIntStateOf(0) }

    // --- Yan Etkiler ---

    // Ekranın merkezindeki öğeyi hesaplar
    val centeredItemIndex by remember(state.firstVisibleItemIndex, state.firstVisibleItemScrollOffset) {
        derivedStateOf {
            val visibleItems = state.layoutInfo.visibleItemsInfo
            if (visibleItems.isEmpty()) return@derivedStateOf -1
            val firstVisible = state.firstVisibleItemIndex
            val scrollOffset = state.firstVisibleItemScrollOffset
            if (scrollOffset > (visibleItems.firstOrNull()?.size?.div(2) ?: 0)) firstVisible + 1 else firstVisible
        }
    }

    // Kaydırma sırasında aktif öğeyi günceller
    LaunchedEffect(centeredItemIndex) {
        if (centeredItemIndex != -1 && (state.isScrollInProgress || selectedIndex == -1)) {
            selectedIndex = centeredItemIndex
        }
    }

    // Listenin başındaki ve sonundaki öğelerin ortalanabilmesi için padding hesabı
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

                // Temaya göre uygun kart tasarımını göster
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

                                val categoryName =
                                    categoriesViewModel.getCategoryNameById(filteredList[index].categoryId)
                                // Eğer ürün Kahvehane kategorisindeyse ana sayfadaki özel tasarıma yönlendir
                                if (categoryName == "Kahvehane") {
                                    val kahvehaneIndex =
                                        categories.indexOfFirst { it.name == "Kahvehane" }
                                    onNavigateToHome(
                                        if (kahvehaneIndex != -1) kahvehaneIndex else 2,
                                        filteredList[index].id.toString()
                                    )
                                } else {
                                    val productJson = Gson().toJson(filteredList[index])
                                    val encodedJson = URLEncoder.encode(
                                        productJson,
                                        StandardCharsets.UTF_8.name()
                                    )
                                    navController.navigate("detaysayfa/$encodedJson")
                                }
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
                                state.animateScrollToItem(index)
                                selectedIndex = index
                                val categoryName =
                                    categoriesViewModel.getCategoryNameById(filteredList[index].categoryId)
                                if (categoryName == "Kahvehane") {
                                    val kahvehaneIndex =
                                        categories.indexOfFirst { it.name == "Kahvehane" }
                                    onNavigateToHome(
                                        if (kahvehaneIndex != -1) kahvehaneIndex else 2,
                                        filteredList[index].id.toString()
                                    )
                                } else {
                                    val productJson = Gson().toJson(filteredList[index])
                                    val encodedJson = URLEncoder.encode(
                                        productJson,
                                        StandardCharsets.UTF_8.name()
                                    )
                                    navController.navigate("detaysayfa/$encodedJson")
                                }
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
