package com.example.midemegel.screens.favorite

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.midemegel.components.common.HeaderDesign
import com.example.midemegel.R
import com.example.midemegel.helpers.utils.getThemeBackgroundColor

/**
 * Kullanıcının favoriye eklediği ürünlerin listelendiği sayfa tasarımı.
 * Favori ürünleri dikey bir liste halinde sunar.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun FavoritePageDesign(
    navController: NavController,
    paddingValues: PaddingValues,
    darkTheme: Boolean,
    onNavigateToHome: (Int, String) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    // --- Mantıksal Değişkenler ve Durumlar ---
    
    // Favori listesindeki toplam ürün sayısını tutar
    var productCount by remember { mutableIntStateOf(0) }
    // Temaya göre arka plan rengini belirler
    val themeBackgroundColor = getThemeBackgroundColor(darkTheme)

    // --- Tasarım Bileşenleri ---
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = paddingValues.calculateBottomPadding())
            .background(color = themeBackgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Sayfa başlığı (Favori ikonu ve "Favoriler" yazısı)
        HeaderDesign(
            darkTheme = darkTheme,
            productCount = productCount,
            headerIcon = R.drawable.favorite_selected_icon,
            headerText = "Favoriler"
        )

        // Favori ürün kartlarının listelendiği bileşen
        FavoriteList(
            navController = navController,
            darkTheme = darkTheme,
            productCount = { productCount = it },
            onNavigateToHome = onNavigateToHome,
            sharedTransitionScope = sharedTransitionScope,
            animatedVisibilityScope = animatedVisibilityScope
        )
    }
}
