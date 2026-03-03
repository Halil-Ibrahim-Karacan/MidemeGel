package com.example.midemegel.screens.product.productpage.productpagecomponents

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.midemegel.R
import kotlinx.coroutines.launch

/**
 * Ürün detay sayfasının üst kısmında yer alan kontrol çubuğu.
 * Geri dönme ve favoriye ekleme butonlarını içerir.
 * Sayfa açılışında yanlardan içeri doğru kayma animasyonu ile belirir.
 */
@Composable
fun ProductPageTopBar(
    visibleState: Boolean, // Animasyonun tetiklenmesini sağlayan görünürlük durumu
    visibleDuration: Int,  // Animasyonun süresi (milisaniye)
    paddingValues: PaddingValues, // Sistem çubukları için güvenli alan boşlukları
    contrastTextColor: Color,    // İkonların rengi
    favoriteIcon: Int,           // Favori durumuna göre gösterilecek ikon (ID)
    topIconsSize: Dp,            // İkonların boyutu
    onBackClick: () -> Unit,     // Geri butonuna basıldığında yapılacak işlem
    onFavoriteClick: () -> Unit  // Favori butonuna basıldığında yapılacak işlem
) {
    // --- Mantıksal Değişkenler ve Animasyon Durumları ---
    
    val scope = rememberCoroutineScope()
    // Geri butonu tıklama animasyonu için ölçekleme değeri
    val minusScaleBack = remember { Animatable(1f) }
    // Favori butonu tıklama animasyonu için ölçekleme değeri
    val minusScaleFavorite = remember { Animatable(1f) }

    // --- Tasarım Bileşenleri ---
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(paddingValues)
    ) {
        // --- Geri Dön Butonu ---
        AnimatedVisibility(
            visible = visibleState,
            enter = slideInHorizontally(
                initialOffsetX = { -it }, // Soldan içeri kayar
                animationSpec = tween(durationMillis = visibleDuration)
            ),
            exit = slideOutHorizontally(
                targetOffsetX = { -it }, // Sola doğru çıkar
                animationSpec = tween(durationMillis = visibleDuration)
            )
        ) {
            Image(
                modifier = Modifier
                    .graphicsLayer(
                        scaleX = minusScaleBack.value,
                        scaleY = minusScaleBack.value
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            // Tıklama animasyonu ve geri gitme aksiyonu
                            scope.launch {
                                minusScaleBack.animateTo(0.85f, tween(120))
                                minusScaleBack.animateTo(1f, spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow))
                                onBackClick()
                            }
                        })
                    .rotate(180f) // Beyaz ok ikonunu geri oku yapmak için döndürür
                    .size(topIconsSize)
                    .align(Alignment.CenterStart),
                painter = painterResource(id = R.drawable.arror_wight_icon),
                colorFilter = ColorFilter.tint(contrastTextColor),
                contentDescription = "Geri"
            )
        }

        // --- Favori Butonu ---
        AnimatedVisibility(
            visible = visibleState,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .offset(x = (-10).dp),
            enter = slideInHorizontally(
                initialOffsetX = { it + 15 }, // Sağdan içeri kayar
                animationSpec = tween(durationMillis = visibleDuration)
            ),
            exit = slideOutHorizontally(
                targetOffsetX = { it + 15 }, // Sağa doğru çıkar
                animationSpec = tween(durationMillis = visibleDuration)
            )
        ) {
            Image(
                modifier = Modifier
                    .graphicsLayer(
                        scaleX = minusScaleFavorite.value,
                        scaleY = minusScaleFavorite.value
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            // Tıklama animasyonu ve favori aksiyonu
                            scope.launch {
                                minusScaleFavorite.animateTo(0.85f, tween(120))
                                minusScaleFavorite.animateTo(1f, spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow))
                                onFavoriteClick()
                            }
                        })
                    .size(topIconsSize),
                painter = painterResource(id = favoriteIcon),
                colorFilter = ColorFilter.tint(contrastTextColor),
                contentDescription = "Favori"
            )
        }
    }
}
