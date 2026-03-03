package com.example.midemegel.helpers

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.midemegel.data.model.ProductModel
import com.example.midemegel.screens.main.MainPageDesign
import com.example.midemegel.screens.product.productpage.ProductPageDesign
import com.google.gson.Gson

/**
 * Uygulama içi ekran geçişlerini ve navigasyon grafiğini yöneten Composable.
 * Shared Transition (Paylaşılan Geçiş) animasyonlarını destekleyen bir yapı kurar.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Transitions(paddingValues: PaddingValues, darkTheme: Boolean) {
    // --- Mantıksal Değişkenler ---
    
    // Uygulama genelinde navigasyonu kontrol eden controller
    val navController = rememberNavController()

    // --- Tasarım Bileşenleri ---
    
    // SharedTransitionLayout, ekranlar arası ortak öğelerin animasyonlu geçişini sağlar
    SharedTransitionLayout {
        NavHost(navController = navController, startDestination = "anasayfa") {
            
            // Ana Sayfa Rotası
            composable(route = "anasayfa") {
                MainPageDesign(
                    navController = navController,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@composable,
                    darkTheme = darkTheme
                )
            }

            // Ürün Detay Sayfası Rotası (Parametre olarak JSON formatında ürün alır)
            composable(
                route = "detaysayfa/{product}",
                arguments = listOf(
                    navArgument("product") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                // --- Veri Hazırlama ---
                val encodedJson = backStackEntry.arguments?.getString("product")
                // URL encoding kaynaklı boşluk karakterlerini geri dönüştür
                val decodedJson = encodedJson?.replace('+', ' ')
                val productModel = Gson().fromJson(decodedJson, ProductModel::class.java)

                // Detay sayfası tasarımı
                ProductPageDesign(
                    navController = navController,
                    productModel = productModel,
                    paddingValues = paddingValues,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@composable
                )
            }
        }
    }
}
