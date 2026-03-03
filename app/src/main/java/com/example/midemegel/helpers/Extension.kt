package com.example.midemegel.helpers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.rememberAsyncImagePainter
import com.example.midemegel.R
import java.io.File
import kotlin.math.roundToInt

/**
 * Bir rengin algılanan parlaklığını hesaplar.
 * @return Parlaklık değeri (0 ile 255 arasında).
 */
fun Color.calculateBrightness(): Int {
    // Color nesnesinin red, green, blue özellikleri 0.0f-1.0f aralığındadır.
    // Bunları 0-255 aralığına dönüştürüyoruz.
    val kirmizi255 = (this.red * 255).roundToInt()
    val yesil255 = (this.green * 255).roundToInt()
    val mavi255 = (this.blue * 255).roundToInt()

    // W3C parlaklık formülünü kullanarak hesaplama yapıyoruz.
    return ((kirmizi255 * 0.299) + (yesil255 * 0.587) + (mavi255 * 0.114)).roundToInt()
}

/**
 * Rengin "parlak" olup olmadığını kontrol eder.
 * @param threshold Parlaklık sınırı. Genellikle 128 veya 150 kullanılır.
 * @return Parlaklık eşik değerinden büyükse `true`, değilse `false` döner.
 */
fun Color.isBright(threshold: Int = 200): Boolean {
    return calculateBrightness() > threshold
}

/**
 * String Hex kodunu Compose Color nesnesine dönüştürür.
 * Geçersiz bir kod durumunda şeffaf renk döner.
 */
fun String?.toComposeColor(): Color {
    return try {
        if (this.isNullOrEmpty()) Color.Transparent
        else Color(android.graphics.Color.parseColor(this))
    } catch (e: Exception) {
        Color.Transparent
    }
}

/**
 * String formatındaki kaynak adını veya dosya yolunu Painter nesnesine dönüştürür.
 * @param fallback Kaynak bulunamazsa kullanılacak yedek ikon.
 * @return Ekranda çizilebilir bir Painter nesnesi.
 */
@Composable
fun String.asPainter(
    fallback: Int = R.drawable.app_icon
): Painter {
    val context = LocalContext.current

    // 1. Drawable mı? (Uygulama kaynakları içinde ara)
    val drawableId = remember(this) {
        context.resources.getIdentifier(
            this,
            "drawable",
            context.packageName
        )
    }

    return when {
        drawableId != 0 -> {
            painterResource(id = drawableId)
        }

        else -> {
            // 2. Dosya sistemi yolu mu? (Dahili depolamadan oku)
            val file = remember(this) { File(this) }

            if (file.exists() && file.canRead()) {
                rememberAsyncImagePainter(model = file)
            } else {
                // 3. Hiçbiri değilse varsayılan ikonu kullan
                painterResource(id = fallback)
            }
        }
    }
}
