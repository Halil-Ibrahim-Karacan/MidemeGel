package com.example.midemegel.screens.product.productitem.productitemcomponents

import android.graphics.Matrix
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.midemegel.helpers.asPainter
import com.example.midemegel.data.model.ProductModel

/**
 * Ürün kartı üzerinde yer alan ana görsel bileşeni.
 * Görselin altına dinamik gölge ve parlama efektleri ekler. 
 * Kategoriye bağlı olarak zıplama veya dönme animasyonlarını destekler.
 * Ekranlar arası geçişte 'Shared Element' animasyonunu koordine eder.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ProductCardImage(
    productModel: ProductModel,
    imageWidth: Dp,
    verticalOffset: Dp,
    rotationValue: Float,
    isSicrama: Boolean, // Zıplama animasyonunun aktifliği
    isDondur: Boolean,  // Dönme animasyonunun aktifliği
    tiltAmount: Float,  // Gölge eğim miktarı
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier
) {
    // --- Tasarım Bileşenleri ---
    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        contentAlignment = Alignment.TopCenter
    ) {
        // --- Gölge/Parlama Efekti Bölümü ---
        // Görselin altına eğimli ve bulanık (blur) bir gölge katmanı çizer.
        Box(
            modifier = Modifier
                .size(imageWidth)
                .drawWithContent {
                    // Native canvas kullanarak gölgeye perspektif eğimi (tilt) uygular.
                    val w = size.width
                    val h = size.height
                    val src = floatArrayOf(0f, 0f, w, 0f, w, h, 0f, h)
                    val dst = floatArrayOf(0f - w * tiltAmount, 0f, w - w * tiltAmount, 0f, w, h, 0f, h)
                    val matrix = Matrix().apply { setPolyToPoly(src, 0, dst, 0, 4) }
                    val canvas = drawContext.canvas.nativeCanvas
                    canvas.save()
                    canvas.concat(matrix)
                    drawContent()
                    canvas.restore()
                }
        ) {
            Image(
                painter = productModel.image.asPainter(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .offset(y = if (isSicrama) verticalOffset else 0.dp)
                    .graphicsLayer {
                        alpha = 0.3f // Gölge şeffaflığı
                        rotationX = 30f // Yatay perspektif
                        rotationZ = if (isDondur) rotationValue else 0f
                        // Animasyon tipine göre pivot noktasını belirler
                        transformOrigin = if (isSicrama) TransformOrigin(0.5f, 1f) else TransformOrigin(0.5f, 0.5f)
                        // Gölgeyi yumuşatmak için Blur efekti uygular
                        renderEffect = BlurEffect(20f, 8f, TileMode.Decal)
                    },
                colorFilter = ColorFilter.tint(Color.Black, blendMode = BlendMode.SrcAtop)
            )
        }

        // --- Ana Ürün Görseli Bölümü ---
        // Shared Element animasyonu ile ekran geçişlerini akıcı hale getirir.
        with(sharedTransitionScope) {
            Image(
                painter = productModel.image.asPainter(),
                contentDescription = productModel.name,
                modifier = Modifier
                    .sharedElement(
                        rememberSharedContentState(key = "image-${productModel.id}"),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                    .size(imageWidth)
                    .offset(y = if (isSicrama) verticalOffset else 0.dp)
                    .graphicsLayer {
                        // Eğer kategori dönme animasyonuna sahipse rotasyonu uygular
                        if (isDondur) {
                            rotationZ = rotationValue
                            transformOrigin = TransformOrigin(0.5f, 0.5f)
                        }
                    }
            )
        }
    }
}
