package com.example.midemegel.helpers.utils

import android.content.Context
import android.graphics.BlurMaskFilter
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.io.File
import java.io.FileOutputStream

/**
 * Piksel (px) değerini Yoğunluktan Bağımsız Piksel (dp) değerine dönüştürür.
 * @param px Dönüştürülecek piksel değeri.
 */
@Composable
fun pxToDp(px: Float): Dp {
    return with(LocalDensity.current) { px.toDp() }
}

/**
 * Verilen Uri'yi uygulamanın dahili depolama alanına kaydeder.
 * @param context Uygulama bağlamı.
 * @param uri Kaydedilecek verinin Uri adresi.
 * @return Kaydedilen dosyanın mutlak yolu veya hata durumunda null.
 */
fun saveUriToInternalStorage(context: Context, uri: Uri): String? {
    return try {
        val fileName = "product_${System.currentTimeMillis()}.jpg"
        val file = File(context.filesDir, fileName)
        context.contentResolver.openInputStream(uri)?.use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }
        file.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
