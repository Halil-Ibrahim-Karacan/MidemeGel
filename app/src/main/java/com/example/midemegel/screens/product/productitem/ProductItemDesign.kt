package com.example.midemegel.screens.product.productitem

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.midemegel.screens.product.productitem.productitemcomponents.ProductCardImage
import com.example.midemegel.screens.product.productitem.productitemcomponents.ProductCardRating
import com.example.midemegel.screens.product.productitem.productitemcomponents.ProductCardFavorite
import com.example.midemegel.helpers.enums.CategoryAnimationEnum
import com.example.midemegel.helpers.toComposeColor
import com.example.midemegel.helpers.utils.getContrastTextColor
import com.example.midemegel.helpers.utils.getScreenWidth
import com.example.midemegel.data.model.ProductModel

/**
 * Ana menüde listelenen her bir ürün için kullanılan standart kart tasarımı.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@SuppressLint("ContextCastToActivity", "ConfigurationScreenWidthHeight")
@Composable
fun ProductItemDesign(
    currentSelectedIndex: Int,
    index: Int,
    productModel: ProductModel,
    animationType: String,
    animationSpeed: Int,
    onFavoriteUpdate: (ProductModel) -> Unit,
    onCardClick: () -> Unit,
    onHeightMeasured: (Int) -> Unit = {},
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    // --- Animasyon Durumları ---
    val rotation = remember { Animatable(0f) }
    val bounce = remember { Animatable(0f) }

    // Dönme animasyonu hız değişimine anında tepki verir
    LaunchedEffect(animationSpeed, animationType) {
        if (animationType == CategoryAnimationEnum.Dondur.animationName) {
            while (true) {
                rotation.animateTo(
                    targetValue = rotation.value + 360f,
                    animationSpec = tween(durationMillis = animationSpeed * 10000, easing = LinearEasing)
                )
            }
        } else {
            rotation.snapTo(0f)
        }
    }

    // Zıplama animasyonu hız değişimine anında tepki verir
    LaunchedEffect(animationSpeed, animationType) {
        if (animationType == CategoryAnimationEnum.Sicrama.animationName) {
            while (true) {
                bounce.animateTo(-20f, tween(animationSpeed * 1000, easing = EaseInOutCubic))
                bounce.animateTo(0f, tween(animationSpeed * 1000, easing = EaseInOutCubic))
            }
        } else {
            bounce.snapTo(0f)
        }
    }

    val tiltAmount by remember(animationType) {
        mutableFloatStateOf(if (animationType == CategoryAnimationEnum.Sicrama.animationName) 0.5f else 0.2f)
    }

    val isSelected = currentSelectedIndex == index
    val rotationValue = if (isSelected) rotation.value else 0f
    val verticalOffset = if (isSelected) bounce.value.dp else 0.dp

    val currentColor = productModel.color.toComposeColor()
    val contrastTextColor = getContrastTextColor(productModel)
    val screenWidth = getScreenWidth()
    
    val baseCardWidth = screenWidth * 0.75f
    val collapsedImageWidth = baseCardWidth * 0.58f
    val iconSize = baseCardWidth * 0.10f
    val titleFont = (baseCardWidth.value * 0.065f).sp
    val priceFont = (baseCardWidth.value * 0.060f).sp

    Box(
        modifier = Modifier
            .wrapContentSize()
            .scale(if (isSelected) 1.1f else 1f)
            .zIndex(if (isSelected) 2f else 1f)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onCardClick() }
            .onGloballyPositioned { coordinates -> onHeightMeasured(coordinates.size.height) }
    ) {
        Column(
            modifier = Modifier
                .padding(top = collapsedImageWidth / 2)
                .background(color = currentColor, RoundedCornerShape(10.dp))
                .wrapContentHeight()
                .border(width = 2.dp, color = Color.Black, RoundedCornerShape(10.dp))
                .width(baseCardWidth)
                .align(Alignment.Center),
            verticalArrangement = Arrangement.Bottom
        ) {
            Box(modifier = Modifier.fillMaxWidth().height(baseCardWidth * 0.30f).padding(horizontal = 10.dp)) {
                ProductCardRating(
                    rating = productModel.ratingSize.toInt(),
                    textColor = contrastTextColor,
                    iconSize = iconSize,
                    modifier = Modifier.align(Alignment.CenterStart)
                )

                ProductCardFavorite(
                    isFavorite = productModel.favorite == 1,
                    tintColor = contrastTextColor,
                    iconSize = iconSize,
                    onFavoriteClick = {
                        val updatedModel =
                            productModel.copy(favorite = if (productModel.favorite == 1) 0 else 1)
                        onFavoriteUpdate(updatedModel)
                    },
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
            
            Text(
                text = productModel.name,
                color = contrastTextColor,
                modifier = Modifier.fillMaxWidth().height((titleFont.value * 4).dp).padding(vertical = 10.dp, horizontal = 5.dp),
                textAlign = TextAlign.Center,
                fontSize = titleFont,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = (titleFont.value * 1.5f).sp
            )
            
            Text(
                text = "${productModel.price}₺",
                color = contrastTextColor,
                modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                textAlign = TextAlign.Center,
                fontSize = priceFont
            )
        }

        ProductCardImage(
            productModel = productModel,
            imageWidth = collapsedImageWidth,
            verticalOffset = verticalOffset,
            rotationValue = rotationValue,
            isSicrama = animationType == CategoryAnimationEnum.Sicrama.animationName,
            isDondur = animationType == CategoryAnimationEnum.Dondur.animationName,
            tiltAmount = tiltAmount,
            sharedTransitionScope = sharedTransitionScope,
            animatedVisibilityScope = animatedVisibilityScope,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}
