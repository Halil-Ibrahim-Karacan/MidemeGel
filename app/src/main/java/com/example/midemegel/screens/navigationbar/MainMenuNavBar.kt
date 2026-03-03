package com.example.midemegel.screens.navigationbar

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.midemegel.R
import com.example.midemegel.data.model.NavBarItemModel
import com.example.midemegel.helpers.utils.getScreenWidth
import com.example.midemegel.helpers.utils.getThemeBackgroundColor
import com.example.midemegel.helpers.utils.getThemeTextColor

/**
 * Uygulamanın alt kısmında yer alan navigasyon çubuğu (Bottom Navigation Bar).
 * Kullanıcının ana sayfalar (Ürünler, Favoriler, Sepet) arasında geçiş yapmasını sağlar.
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainMenuNavBar(selectedIndex: Int, darkTheme: Boolean, itemSelected: (Int) -> Unit) {
    // --- Mantıksal Değişkenler ve Stil Tanımlamaları ---
    
    // Temaya göre renkleri belirle
    val themeBackgroundColor = getThemeBackgroundColor(darkTheme)
    val themeTextColor = getThemeTextColor(darkTheme)

    // Ekran boyutuna göre dinamik ölçüler
    val screenWidth = getScreenWidth()
    val navBarHeight = screenWidth * 0.15f
    val navBarPadding = screenWidth * 0.024f
    val navBarCorner = screenWidth * 0.036f
    val iconSize = screenWidth * 0.08f

    // Navigasyon öğelerini tanımla
    val navBarItemMainPage = NavBarItemModel(
        "Ürünler",
        R.drawable.main_menu_selected_icon,
        R.drawable.main_menu_unselected_icon
    )
    val navBarItemFavorite = NavBarItemModel(
        "Favoriler",
        R.drawable.favorite_selected_icon,
        R.drawable.favorite_unselected_icon,
    )
    val navBarItemShoppingBag = NavBarItemModel(
        "Sepetim",
        R.drawable.shopping_bag_selected_icon,
        R.drawable.shopping_bag_unselected_icon
    )
    val items = listOf(
        navBarItemMainPage,
        navBarItemFavorite,
        navBarItemShoppingBag
    )

    // --- Tasarım Bileşenleri ---
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(themeBackgroundColor)
            // Sistem navigasyon çubuğu (gesture bar) için alt boşluk ekler
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        NavigationBar(
            modifier = Modifier
                .padding(navBarPadding)
                .height(navBarHeight)
                .border(
                    width = 1.dp,
                    color = themeTextColor,
                    shape = RoundedCornerShape(navBarCorner)
                )
                .clip(RoundedCornerShape(navBarCorner)),
            containerColor = themeBackgroundColor
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Her bir navigasyon öğesi için döngü
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        modifier = Modifier.weight(1f),
                        selected = selectedIndex == index,
                        onClick = { itemSelected(index) },
                        alwaysShowLabel = false,
                        icon = {
                            // Seçili olma durumuna göre ikon ID'sini belirle
                            val iconId = if (selectedIndex == index) item.selectedIcon else item.unselectedIcon
                            Icon(
                                painter = painterResource(id = iconId),
                                contentDescription = item.title,
                                modifier = Modifier.size(iconSize)
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedTextColor = themeTextColor,
                            selectedIconColor = themeTextColor,
                            unselectedTextColor = themeTextColor.copy(0.5f),
                            unselectedIconColor = themeTextColor.copy(0.5f),
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    }
}
