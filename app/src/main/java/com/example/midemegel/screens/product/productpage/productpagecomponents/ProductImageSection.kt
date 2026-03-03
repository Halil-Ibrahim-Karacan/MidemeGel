package com.example.midemegel.screens.product.productpage.productpagecomponents

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.midemegel.components.common.FancyAnimatedCounter_Fixed
import com.example.midemegel.helpers.enums.CategoryAnimationEnum
import com.example.midemegel.helpers.asPainter
import com.example.midemegel.data.model.ProductModel

/**
 * Ürün detay sayfasında ürünün görselini ve miktar seçiciyi (counter) barındıran bölüm.
 * Arka planda radyal bir gradyan kullanır ve ürün görseline kategori bazlı animasyonlar uygular.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ProductImageSection(
    productModel: ProductModel,
    currentColor: Color,
    contrastTextColor: Color,
    paddingValues: PaddingValues,
    productImagePadding: Dp,
    categoryAnimation: String,
    bounceOffset: Float,
    rotationValue: Float,
    imageScale: Float,
    productCount: Int,
    onProductCountChange: (Int) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    // --- Tasarım Bileşenleri ---
    Box(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            // Arka plana ürüne özel renk geçişi (gradient) uygular
            .background(Brush.radialGradient(colors = listOf(Color.White, currentColor))),
        contentAlignment = Alignment.BottomCenter
    ) {
        // --- Ürün Görseli ---
        // Shared Transition desteği ile ana listeden detay sayfasına akıcı geçiş sağlar.
        with(sharedTransitionScope) {
            Image(
                modifier = Modifier
                    .sharedElement(
                        rememberSharedContentState(key = "image-${productModel.id}"),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                    // Kategoriye göre zıplama (bounce) animasyonu uygular
                    .offset(y = if (categoryAnimation == CategoryAnimationEnum.Sicrama.animationName) bounceOffset.dp else 0.dp)
                    .graphicsLayer {
                        // Kategoriye göre dönme (rotate) animasyonu uygular
                        if (categoryAnimation == CategoryAnimationEnum.Dondur.animationName) {
                            rotationZ = rotationValue
                            transformOrigin = TransformOrigin(0.5f, 0.5f)
                        }
                    }
                    .fillMaxSize()
                    .padding(productImagePadding)
                    .align(Alignment.Center),
                painter = productModel.image.asPainter(),
                contentDescription = productModel.name
            )
        }

        // --- Miktar Seçici (Counter) ---
        // Ürünün kaç adet sepete ekleneceğini belirler.
        Box(modifier = Modifier.scale(imageScale)) {
            FancyAnimatedCounter_Fixed(
                backgroundColor = Color.White,
                color = currentColor,
                buttonTextColor = contrastTextColor,
                Count = productCount,
                removeProduct = { if (productCount > 1) onProductCountChange(productCount - 1) },
                addProduct = { onProductCountChange(productCount + 1) }
            )
        }
    }
}
