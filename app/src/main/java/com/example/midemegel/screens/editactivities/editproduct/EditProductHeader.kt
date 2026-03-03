package com.example.midemegel.screens.editactivities.editproduct

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.example.midemegel.R
import kotlinx.coroutines.launch

@Composable
fun EditProductHeader(
    title: String,
    isFavorite: Boolean,
    themeColor: Color,
    headerHeight: Dp,
    iconSize: Dp,
    titleFontSize: TextUnit,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val backIconScale = remember { Animatable(1f) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(headerHeight),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.arror_wight_icon),
            contentDescription = "Geri",
            colorFilter = ColorFilter.tint(themeColor),
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 16.dp)
                .size(iconSize)
                .rotate(180f)
                .graphicsLayer(scaleX = backIconScale.value, scaleY = backIconScale.value)
                .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {
                    scope.launch {
                        backIconScale.animateTo(0.85f, tween(120, easing = LinearOutSlowInEasing))
                        backIconScale.animateTo(1f, spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow))
                        onBackClick()
                    }
                }
        )
        Text(
            text = title,
            color = themeColor,
            fontSize = titleFontSize,
            fontFamily = FontFamily.Serif
        )

        Image(
            painter = painterResource(id = if (isFavorite) R.drawable.favorite_selected_icon else R.drawable.favorite_unselected_icon),
            contentDescription = "Favori",
            colorFilter = ColorFilter.tint(themeColor),
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp)
                .size(iconSize)
                .clickable { onFavoriteClick() }
        )
    }
}
