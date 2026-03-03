package com.example.midemegel.screens.main

import com.example.midemegel.screens.navigationbar.MainMenuNavBar
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.midemegel.screens.editactivities.edit.EditActivity
import com.example.midemegel.components.main.MainHeader
import com.example.midemegel.helpers.utils.getResponsiveFontSize
import com.example.midemegel.helpers.utils.getScreenWidth
import com.example.midemegel.helpers.utils.getThemeBackgroundColor
import com.example.midemegel.helpers.utils.getThemeTextColor
import com.example.midemegel.screens.favorite.FavoritePageDesign
import com.example.midemegel.screens.menu.MenuPageDesign
import com.example.midemegel.screens.shoppingcart.ShoppingCartPageDesign

/**
 * Ana ekran tasarımını ve navigasyon mantığını barındıran composable.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@SuppressLint("UnusedBoxWithConstraintsScope", "ContextCastToActivity")
@Composable
fun MainPageDesign(
    navController: NavController,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    darkTheme: Boolean
) {
    // --- Mantıksal Değişkenler ve Durumlar ---
    // Alt navigasyon çubuğunda seçili olan öğenin indeksi
    var selectedItem by rememberSaveable { mutableIntStateOf(0) }
    // Ana sayfada seçili olan kategori indeksi
    var homeCategoryIndex by rememberSaveable { mutableIntStateOf(0) }
    // Ana sayfada odaklanılan ürünün ID'si
    var homeProductId by rememberSaveable { mutableStateOf<String?>(null) }
    
    // Tema renkleri ve ekran boyutlarına dayalı dinamik değerler
    val themeBackgroundColor = getThemeBackgroundColor(darkTheme)
    val themeTextColor = getThemeTextColor(darkTheme)
    val activity = LocalContext.current as Activity
    val screenWidth = getScreenWidth()
    val topRowHeight = screenWidth * 0.12f
    val iconSize = screenWidth * 0.095f
    val titleFontSize = getResponsiveFontSize(0.1f)

    // --- Tasarım Bileşenleri ---
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(themeBackgroundColor),
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(themeBackgroundColor)
                    .padding(top = innerPadding.calculateTopPadding(), bottom = 16.dp, start = 16.dp, end = 16.dp)
            ) {
                // Eğer bir ürüne tıklanmamışsa üst başlığı göster
                    MainHeader(
                        darkTheme = darkTheme,
                        themeTextColor = themeTextColor,
                        topRowHeight = topRowHeight,
                        iconSize = iconSize,
                        titleFontSize = titleFontSize,
                        onEditClick = {
                            val intent = Intent(activity, EditActivity::class.java)
                            intent.putExtra("darkTheme", darkTheme)
                            activity.startActivity(intent)
                        }
                    )

                // Seçili sekmeye göre ilgili sayfa tasarımını yükle
                when (selectedItem) {
                    0 -> MenuPageDesign(
                        navController,
                        innerPadding,
                        darkTheme,
                        initialCategoryIndex = homeCategoryIndex,
                        initialProductId = homeProductId,
                        onCategoryChanged = { homeCategoryIndex = it },
                        sharedTransitionScope = sharedTransitionScope,
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                    1 -> FavoritePageDesign(
                        navController,
                        innerPadding,
                        darkTheme,
                        onNavigateToHome = { catIdx, productId ->
                            homeCategoryIndex = catIdx
                            homeProductId = productId
                            selectedItem = 0
                        },
                        sharedTransitionScope,
                        animatedVisibilityScope
                    )
                    2 -> ShoppingCartPageDesign(
                        navController,
                        innerPadding,
                        darkTheme,
                        onNavigateToHome = { catIdx, productId ->
                            homeCategoryIndex = catIdx
                            homeProductId = productId
                            selectedItem = 0
                        }
                    )
                }
            }
        },
        bottomBar = {
            // Eğer bir detay sayfasına girilmemişse alt navigasyon menüsünü göster
                MainMenuNavBar(selectedIndex = selectedItem, darkTheme = darkTheme, itemSelected = {
                    if (it == 0 && selectedItem != 0) {
                        homeCategoryIndex = 0
                        homeProductId = null
                    }
                    selectedItem = it
                })
        }
    )
}