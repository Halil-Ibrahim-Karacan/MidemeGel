package com.example.midemegel.screens.product

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.midemegel.data.model.ProductModel
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.example.midemegel.R
import com.example.midemegel.helpers.enums.CategoryAnimationEnum
import com.example.midemegel.helpers.asPainter
import com.example.midemegel.helpers.toComposeColor
import com.example.midemegel.helpers.utils.getContrastTextColor
import com.example.midemegel.helpers.utils.getScreenWidth
import kotlinx.coroutines.launch

/**
 * Koyu tema (Dark Theme) aktifken kullanılan ürün kartı tasarımı.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@SuppressLint("ContextCastToActivity")
@Composable
fun ProductItemDesignDarkTheme(
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
    val scope = rememberCoroutineScope()
    val minusScaleFavorite = remember { Animatable(1f) }

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

    val isSelected = currentSelectedIndex == index
    val rotationValue = if (isSelected) rotation.value else 0f
    val verticalOffset = if (isSelected) bounce.value else 0f

    val screenWidth = getScreenWidth()
    val baseCardWidth = screenWidth * 0.75f
    val collapsedImageWidth = baseCardWidth * 0.58f
    val iconSize = baseCardWidth * 0.10f
    val titleFont = (baseCardWidth.value * 0.065f).sp
    val priceFont = (baseCardWidth.value * 0.060f).sp

    val currentColor = productModel.color.toComposeColor()
    val contrastTextColor = getContrastTextColor(productModel)
    val favoriteIcon = if (productModel.favorite == 1) R.drawable.favorite_selected_icon else R.drawable.favorite_unselected_icon

    // --- Tasarım Bileşenleri ---
    Box(
        modifier = Modifier
            .width(baseCardWidth)
            .wrapContentHeight()
            .scale(if (isSelected) 1.1f else 1f)
            .zIndex(if (isSelected) 2f else 1f)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onCardClick() }
            .onGloballyPositioned { coordinates -> onHeightMeasured(coordinates.size.height) }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(collapsedImageWidth)
                    .background(color = colorResource(id = R.color.background_color_dark), shape = CircleShape)
                    .padding(10.dp)
            ) {
                with(sharedTransitionScope) {
                    Image(
                        painter = productModel.image.asPainter(),
                        contentDescription = "",
                        modifier = Modifier
                            .sharedElement(
                                rememberSharedContentState(key = "image-${productModel.id}"),
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                            .fillMaxSize()
                            .align(Alignment.Center)
                            .offset(y = if (animationType == CategoryAnimationEnum.Sicrama.animationName) verticalOffset.dp else 0.dp)
                            .graphicsLayer {
                                if (animationType == CategoryAnimationEnum.Dondur.animationName) {
                                    rotationZ = rotationValue
                                    transformOrigin = TransformOrigin(0.5f, 0.5f)
                                }
                            }
                    )
                }
            }

            Row(
                modifier = Modifier
                    .wrapContentSize()
                    .background(color = currentColor, shape = RoundedCornerShape(15.dp)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.wrapContentSize().weight(85f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
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
                        fontSize = priceFont,
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.arror_wight_icon),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(color = contrastTextColor),
                    modifier = Modifier.size(iconSize * 2).weight(15f)
                )
            }
        }

        Image(
            painter = painterResource(id = favoriteIcon),
            contentDescription = "",
            colorFilter = ColorFilter.tint(currentColor),
            modifier = Modifier
                .size(iconSize)
                .align(Alignment.TopEnd)
                .graphicsLayer(scaleX = minusScaleFavorite.value, scaleY = minusScaleFavorite.value)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    scope.launch {
                        minusScaleFavorite.animateTo(0.85f, tween(durationMillis = 120, easing = LinearOutSlowInEasing))
                        minusScaleFavorite.animateTo(1f, spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow))
                    }
                    productModel.favorite = if (productModel.favorite == 1) 0 else 1
                    onFavoriteUpdate(productModel)
                }
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.star_icon),
                contentDescription = "",
                colorFilter = ColorFilter.tint(Color.Yellow),
                modifier = Modifier.size(iconSize)
            )
            Text(
                text = "${productModel.ratingSize.toInt()}",
                color = Color.White,
                style = TextStyle.Default.copy(shadow = Shadow(color = Color.Yellow, offset = Offset(0f, 0f), blurRadius = 30f))
            )
        }
    }
}
