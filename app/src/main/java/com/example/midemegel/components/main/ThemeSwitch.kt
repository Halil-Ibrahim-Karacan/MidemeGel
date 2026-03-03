package com.example.midemegel.components.main

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.midemegel.R
import com.example.midemegel.helpers.utils.getScreenWidth
import com.example.midemegel.data.viewmodel.ThemeViewModel

/**
 * Uygulamanın temasını (aydınlık/karanlık) değiştirmek için kullanılan özel tasarımlı anahtar (Switch).
 * Gece ve gündüz görselleriyle zenginleştirilmiş bir kullanıcı deneyimi sunar.
 */
@Composable
fun ThemeSwitch(darkThemeChanged: (Boolean) -> Unit){
    // --- Mantıksal Değişkenler ve Durum Yönetimi ---
    
    val context = LocalContext.current
    
    // ThemeViewModel'ı Activity seviyesinde scope'layarak tema durumunun tüm uygulama boyunca (navigasyon dahil) korunmasını sağlıyoruz.
    val themeViewModel: ThemeViewModel = hiltViewModel(viewModelStoreOwner = context as ComponentActivity)

    // Mevcut tema durumunu (koyu/açık) ViewModel üzerinden takip eder
    val darkTheme by themeViewModel.selectedTheme.collectAsState()

    // Ekran boyutuna dayalı dinamik ölçüler
    val screenWidth = getScreenWidth()
    val switchWidth = screenWidth * 0.126f 
    val switchHeight = screenWidth * 0.078f 
    val iconSize = screenWidth * 0.063f 

    // --- Tasarım Bileşenleri ---
    Box(
        modifier = Modifier
            .width(switchWidth)
            .height(switchHeight)
            .clip(RoundedCornerShape(50))
            .background(Color.LightGray)
    ) {
        // Arka plan resmi (Seçili temaya göre gündüz veya gece manzarası gösterilir)
        Image(
            painter = painterResource(
                id = if (!darkTheme) R.drawable.day_background_icon else R.drawable.night_background_icon
            ),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(50)),
            contentScale = ContentScale.Crop
        )

        // Arka plan üzerine hafif bir karartma katmanı ekler
        Box(modifier = Modifier.fillMaxSize().background(color = Color.Black.copy(0.2f)))

        // Özelleştirilmiş Switch bileşeni
        Switch(
            checked = darkTheme,
            onCheckedChange = { 
                themeViewModel.setDarkTheme(it) // ViewModel üzerinden temayı günceller
                darkThemeChanged(it) // Değişikliği üst bileşene bildirir
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.Transparent,
                uncheckedThumbColor = Color.Transparent,
                checkedTrackColor = Color.Transparent, // Arka plan görselinin görünmesi için şeffaf iz
                uncheckedTrackColor = Color.Transparent,
                checkedBorderColor = Color(0xFF6673AD),
                uncheckedBorderColor = Color(0xFFA6A6A6)
            ),
            thumbContent = {
                // Anahtarın (thumb) içinde güneş veya ay ikonu gösterilir
                Image(
                    painter = painterResource(
                        id = if (!darkTheme) R.drawable.sun_icon else R.drawable.moon_icon
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(iconSize)
                )
            },
            modifier = Modifier.align(Alignment.Center).fillMaxSize()
        )
    }
}
