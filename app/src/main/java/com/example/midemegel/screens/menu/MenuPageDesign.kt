package com.example.midemegel.screens.menu

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.midemegel.components.common.HeaderDesign
import com.example.midemegel.R
import com.example.midemegel.helpers.utils.getThemeBackgroundColor

/**
 * Ana sayfa genel tasarımını oluşturan Composable.
 * Kategorileri ve bunlara bağlı ürünleri listeleyen ana yapıyı barındırır.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MenuPageDesign(
    navController: NavController,
    paddingValues: PaddingValues,
    darkTheme: Boolean,
    initialCategoryIndex: Int = 0,
    initialProductId: String? = null,
    onCategoryChanged: (Int) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    // --- Mantıksal Değişkenler ve Durumlar ---
    // Seçili kategorideki ürün sayısını tutar
    var productCount by remember { mutableIntStateOf(0) }
    // Tema bazlı arka plan rengi
    val themeBackgroundColor = getThemeBackgroundColor(darkTheme)

    // --- Tasarım Bileşenleri ---
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(themeBackgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Üst başlık tasarımı (İkon ve "Menü" yazısı)
        HeaderDesign(
            darkTheme = darkTheme,
            productCount = productCount,
            R.drawable.menu_icon,
            "Menü"
        )

        // Kategori listesi ve ürünlerin gösterildiği alan
        MenuCategoryList(
            navController = navController,
            paddingValues = paddingValues,
            darkTheme = darkTheme,
            productCount = { productCount = it },
            selectedIndex = initialCategoryIndex,
            initialProductId = initialProductId,
            onCategoryChanged = onCategoryChanged,
            sharedTransitionScope = sharedTransitionScope,
            animatedVisibilityScope = animatedVisibilityScope
        )
    }
}
