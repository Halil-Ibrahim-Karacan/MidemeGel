package com.example.midemegel.screens.menu

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.SnapLayoutInfoProvider
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.midemegel.screens.coffeehouse.CoffeeHouseDesign
import com.example.midemegel.data.viewmodel.CategoriesViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Kategori seçim menüsünü ve seçilen kategoriye ait ürün listesini yöneten ana bileşen.
 * Yatayda kaydırılabilir kategori seçici ile dikeydeki ürün listesini koordine eder.
 */
@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalFoundationApi::class)
@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun MenuCategoryList(
    navController: NavController,
    paddingValues: PaddingValues,
    darkTheme: Boolean,
    productCount: (Int) -> Unit,
    selectedIndex: Int = 0,
    initialProductId: String? = null,
    onCategoryChanged: (Int) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    // --- Mantıksal Değişkenler ve Durum Yönetimi ---
    
    // ViewModel üzerinden kategori verilerini takip eder
    val categoriesViewModel: CategoriesViewModel = hiltViewModel()
    val list by categoriesViewModel.categoriesState.collectAsState()
    
    // UI etkileşimleri ve animasyonlar için gerekli yardımcılar
    val density = LocalDensity.current
    val state = rememberLazyListState() // Kategori listesinin kaydırma durumu
    val coroutineScope = rememberCoroutineScope()

    // Snapping özelliği: Listenin durduğunda en yakın öğeyi merkeze almasını sağlar
    val snappingLayout = remember(state) { SnapLayoutInfoProvider(state) }
    val flingBehavior = rememberSnapFlingBehavior(snappingLayout)

    // Sayfa içi durum değişkenleri
    var productCountValue by remember { mutableIntStateOf(0) } // Mevcut kategori ürün sayısı
    var currentCategoryIndex by rememberSaveable { mutableIntStateOf(selectedIndex) } // Seçili kategori indeksi
    
    // Boyut hesaplamaları için ölçüm değişkenleri
    var screenWidth by rememberSaveable { mutableIntStateOf(0) }
    var firstItemHeight by rememberSaveable { mutableIntStateOf(0) }

    // "Kahvehane" kategorisinin seçili olup olmadığını kontrol eder (Özel tasarım için)
    val isKahvehaneSelected by remember(currentCategoryIndex, list) {
        derivedStateOf {
            if (list.isNotEmpty() && currentCategoryIndex in list.indices) {
                list[currentCategoryIndex].name == "Kahvehane"
            } else {
                false
            }
        }
    }

    // Kategorilerin merkezlenmesi için gerekli olan kenar boşluğu hesaplaması
    val halfScreenPadding by remember(screenWidth, firstItemHeight) {
        derivedStateOf {
            if (screenWidth > 0 && firstItemHeight > 0) {
                with(density) { ((screenWidth - firstItemHeight) / 2).coerceAtLeast(0).toDp() }
            } else {
                0.dp
            }
        }
    }

    // --- Yan Etkiler (Side Effects) ---

    // Ürün sayısı değiştiğinde üst bileşene haber verir
    LaunchedEffect(productCountValue) {
        productCount(productCountValue)
    }

    // Kategori listesinin içeriğini veya sırasını takip et
    var lastListIds by remember { mutableStateOf<List<Int>?>(null) }
    LaunchedEffect(list) {
        val currentIds = list.map { it.id }
        if (list.isNotEmpty()) {
            // Eğer liste sırası veya içeriği değiştiyse ilk öğeye odaklan ve ortala
            if (lastListIds != null && lastListIds != currentIds) {
                currentCategoryIndex = 0
                onCategoryChanged(0)
                state.animateScrollToItem(0)
            }
            // İlk yükleme veya geri dönüş durumu: mevcut konumu koru
            else if (lastListIds == null) {
                state.scrollToItem(currentCategoryIndex)
            }
        }
        lastListIds = currentIds
    }

    // Seçili indeksi ve kaydırma konumunu senkronize et (Dönüşlerde konumu korur)
    LaunchedEffect(currentCategoryIndex) {
        if (list.isNotEmpty() && state.firstVisibleItemIndex != currentCategoryIndex && !state.isScrollInProgress) {
            state.scrollToItem(currentCategoryIndex)
        }
    }

    // Kaydırma sırasında ekranın merkezinde duran öğenin indeksini hesaplar
    val centeredItemIndex by remember(state.firstVisibleItemIndex, state.firstVisibleItemScrollOffset) {
        derivedStateOf {
            val visibleItems = state.layoutInfo.visibleItemsInfo
            if (visibleItems.isEmpty()) return@derivedStateOf -1
            val firstVisible = state.firstVisibleItemIndex
            val scrollOffset = state.firstVisibleItemScrollOffset
            if (scrollOffset > (visibleItems.firstOrNull()?.size?.div(2) ?: 0)) firstVisible + 1 else firstVisible
        }
    }

    // Kullanıcı kaydırdıkça aktif kategoriyi günceller
    LaunchedEffect(centeredItemIndex) {
        if (centeredItemIndex != -1 && centeredItemIndex != currentCategoryIndex && state.isScrollInProgress) {
            currentCategoryIndex = centeredItemIndex
            onCategoryChanged(centeredItemIndex)
        }
    }

    // Sadece kategoriler İLK KEZ yüklendiğinde veya tamamen değiştiğinde başa sar
    var previousListSize by remember { mutableIntStateOf(0) }
    LaunchedEffect(list) {
        if (list.isNotEmpty() && previousListSize == 0) {
            state.scrollToItem(currentCategoryIndex)
            previousListSize = list.size
        }
    }

    // --- Tasarım Bileşenleri ---
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = paddingValues.calculateBottomPadding())
    ) {
        // Eğer bir ürüne tıklanmamışsa kategori seçiciyi göster
                LazyRow(
                    modifier = Modifier
                        .wrapContentHeight()
                        .onGloballyPositioned {
                            // Ekran genişliğini ölçerek üst bileşene bildirir (hesaplamalar için)
                            if (it.size.width > 0) screenWidth = it.size.width
                        },
                    horizontalArrangement = Arrangement.spacedBy(50.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    state = state,
                    flingBehavior = flingBehavior,
                    // Listenin başındaki ve sonundaki öğelerin ekranın ortasına gelebilmesi için yan boşluklar
                    contentPadding = PaddingValues(horizontal = halfScreenPadding),
                ) {
                    // Her bir kategori öğesi için döngü
                    itemsIndexed(items = list, key = { _, item -> item.id }) { index, category ->
                        MenuCategoryDesign(
                            currentSelectedIndex = currentCategoryIndex,
                            index = index,
                            darkTheme = darkTheme,
                            categoryname = category.name,
                            categoryImage = category.image,
                            onCardClick = {
                                coroutineScope.launch {
                                    state.animateScrollToItem(index)
                                    currentCategoryIndex = index
                                    onCategoryChanged(index)
                                }
                            },
                            onHeightMeasured = { height ->
                                // İlk öğenin boyutunu ölçerek padding hesaplamalarına yardımcı olur
                                if (index == 0) firstItemHeight = height
                            }
                        )
                    }

        }
        
        // Seçili kategoriye göre ya Kahvehane özel tasarımını ya da standart ürün kartlarını göster
        if (isKahvehaneSelected) {
            CoffeeHouseDesign(
                indx = initialProductId?.toIntOrNull() ?: 11,
                darkTheme = darkTheme,
                productCounts = { productCountValue = it })
        } else {
            MenuList(
                navController = navController,
                categoryIndex = currentCategoryIndex,
                darkTheme = darkTheme,
                categories = list, // Güncel liste buraya geçildi
                productCount = { productCountValue = it },
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope
            )
        }
    }
}
