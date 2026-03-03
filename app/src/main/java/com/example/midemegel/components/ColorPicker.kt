package com.example.midemegel.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.*
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.example.midemegel.helpers.isBright
import com.example.midemegel.helpers.utils.getButtonFontSize
import com.example.midemegel.helpers.utils.getResponsiveFontSize
import com.example.midemegel.helpers.utils.getScreenWidth
import com.example.midemegel.helpers.utils.getTagFontSize
import com.example.midemegel.helpers.utils.getTitleFontSize

/**
 * Kullanıcının bir renk seçmesini sağlayan buton ve bağlı olduğu renk seçici diyalogu.
 */
@Composable
fun ColorPickerButton(
    selectedColor: Color,
    onColorSelected: (Color) -> Unit,
    modifier: Modifier = Modifier,
    buttonBorderColor: Color
) {
    // --- Mantıksal Değişkenler ve Durumlar ---
    var showDialog by remember { mutableStateOf(false) }
    val fontSize = getTagFontSize()

    // --- Tasarım Bileşenleri ---
    
    // Mevcut rengi gösteren ve tıklandığında seçiciyi açan buton
    OutlinedButton(
        onClick = { showDialog = true },
        modifier = modifier,
        border = BorderStroke(2.dp, buttonBorderColor),
        colors = ButtonDefaults.buttonColors(containerColor = selectedColor)
    ) {
        Text(
            text = "Renk Seç",
            // Rengin parlaklığına göre metin rengini siyah veya beyaz yapar
            color = if (selectedColor.isBright()) Color.Black else Color.White,
            fontSize = fontSize
        )
    }

    // Renk seçici diyalog penceresi
    if (showDialog) {
        ColorPickerDialog(
            initialColor = selectedColor,
            onDismiss = { showDialog = false },
            onColorSelected = { color ->
                onColorSelected(color)
                showDialog = false
            }
        )
    }
}

/**
 * HSV tabanlı gelişmiş renk seçici diyalogu.
 * Kullanıcının ton (hue), doygunluk (saturation) ve parlaklık (brightness) değerlerini ayarlamasını sağlar.
 */
@Composable
fun ColorPickerDialog(
    initialColor: Color,
    onDismiss: () -> Unit,
    onColorSelected: (Color) -> Unit
) {
    // --- Mantıksal Değişkenler ve Renk Hesaplamaları ---
    
    var selectedColor by remember { mutableStateOf(initialColor) }
    val screenWidth = getScreenWidth()

    // Dinamik yazı boyutları
    val titleFontSize = getTitleFontSize()
    val hexFontSize = getResponsiveFontSize(0.045f)
    val buttonFontSize = getButtonFontSize()
    val boxSize = screenWidth * 0.65f

    // Başlangıç renginden HSV değerlerini çıkar
    val hsv = remember(initialColor) {
        val hsvArray = FloatArray(3)
        android.graphics.Color.colorToHSV(initialColor.toArgb(), hsvArray)
        hsvArray
    }

    // Ayrı ayrı HSV durumlarını takip eder
    var hue by remember { mutableFloatStateOf(hsv[0]) }
    var saturation by remember { mutableFloatStateOf(hsv[1]) }
    var brightness by remember { mutableFloatStateOf(hsv[2]) }

    // --- Tasarım Bileşenleri ---
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        title = {
            Text(
                text = "Renk Seç",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.Black,
                fontSize = titleFontSize
            )
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Doygunluk ve Parlaklık Seçim Kutusu (Gradient Box)
                GradientColorBox(
                    hue = hue,
                    initialSaturation = saturation,
                    initialBrightness = brightness,
                    onColorSelected = { sat, bright ->
                        saturation = sat
                        brightness = bright
                        selectedColor = Color.hsv(hue, sat, bright)
                    },
                    modifier = Modifier.size(boxSize).clip(RoundedCornerShape(0.dp))
                )

                Spacer(modifier = Modifier.height(screenWidth * 0.04f))

                // Renk Tonu (Gökkuşağı) Kaydırıcısı
                HueSlider(
                    hue = hue,
                    onHueChanged = { newHue ->
                        hue = newHue
                        selectedColor = Color.hsv(hue, saturation, brightness)
                    },
                    modifier = Modifier.fillMaxWidth().height(screenWidth * 0.1f)
                )

                Spacer(modifier = Modifier.height(screenWidth * 0.04f))

                // Seçilen Renk Önizlemesi ve Hex Kodu
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(screenWidth * 0.15f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(selectedColor)
                            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                    )

                    Text(
                        text = "#${selectedColor.toArgb().toUInt().toString(16).uppercase().takeLast(6)}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontFamily = FontFamily.Monospace,
                        color = Color.Black,
                        fontSize = hexFontSize
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onColorSelected(selectedColor) }) {
                Text("Seç", color = Color.Black, fontSize = buttonFontSize)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("İptal", color = Color.Black, fontSize = buttonFontSize)
            }
        }
    )
}

/**
 * Saturation (doygunluk) ve Brightness (parlaklık) değerlerini seçmek için kullanılan kare gradient kutu.
 */
@Composable
fun GradientColorBox(
    hue: Float,
    initialSaturation: Float = 1f,
    initialBrightness: Float = 1f,
    onColorSelected: (saturation: Float, brightness: Float) -> Unit,
    modifier: Modifier = Modifier
) {
    // --- Mantıksal Değişkenler ---
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    var boxSize by remember { mutableStateOf(IntSize.Zero) }

    // Uygulama tasarımına göre minimum sınır değerleri
    val minSaturation = 0.08f
    val minBrightness = 0.5f

    val selectedColor = Color.hsv(hue, initialSaturation, initialBrightness)

    // Başlangıç değerlerine göre seçici imlecin (pointer) konumunu ayarlar
    LaunchedEffect(boxSize, initialSaturation, initialBrightness) {
        if (boxSize.width > 0) {
            offsetX = ((initialSaturation - minSaturation) / (1f - minSaturation)).coerceIn(0f, 1f) * boxSize.width
            offsetY = (1f - ((initialBrightness - minBrightness) / (1f - minBrightness))).coerceIn(0f, 1f) * boxSize.height
        }
    }

    // --- Tasarım ve Etkileşim ---
    Box(
        modifier = modifier
            .onSizeChanged { boxSize = it }
            .pointerInput(hue) {
                // Kullanıcının sürükleme hareketlerini takip eder
                detectDragGestures { change, _ ->
                    change.consume()
                    offsetX = change.position.x.coerceIn(0f, size.width.toFloat())
                    offsetY = change.position.y.coerceIn(0f, size.height.toFloat())

                    // Konumu HSV değerlerine dönüştürür
                    val rawSat = offsetX / size.width
                    val rawBright = 1f - (offsetY / size.height)
                    val saturation = minSaturation + rawSat * (1f - minSaturation)
                    val brightness = minBrightness + rawBright * (1f - minBrightness)

                    onColorSelected(saturation, brightness)
                }
            }
            .drawBehind {
                // 🎨 Doygunluk Gradienti (Yatay)
                drawRect(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color.hsv(hue, minSaturation, 1f), Color.hsv(hue, 1f, 1f))
                    )
                )

                // 🌑 Parlaklık Gradienti (Dikey karartma)
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.35f))
                    )
                )

                // 🎯 Seçim İmleci (Daire)
                drawCircle(color = selectedColor, radius = 10f, center = Offset(offsetX, offsetY))
                drawCircle(color = Color.White, radius = 12f, center = Offset(offsetX, offsetY), style = Stroke(width = 2.5f))
            }
    )
}

/**
 * Renk tonunu (Hue) seçmek için kullanılan yatay gökkuşağı kaydırıcısı.
 */
@Composable
fun HueSlider(
    hue: Float,
    onHueChanged: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    // --- Mantıksal Değişkenler ---
    var sliderPosition by remember { mutableFloatStateOf(hue / 360f) }

    LaunchedEffect(hue) { sliderPosition = hue / 360f }

    val thumbColor = Color.hsv(hue, 1f, 1f)

    // --- Tasarım ve Etkileşim ---
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color.Red, Color.Yellow, Color.Green, Color.Cyan, Color.Blue, Color.Magenta, Color.Red)
                )
            )
            .pointerInput(Unit) {
                // Sürükleme ile ton seçimi
                detectDragGestures { change, _ ->
                    change.consume()
                    sliderPosition = (change.position.x / size.width).coerceIn(0f, 1f)
                    onHueChanged(sliderPosition * 360f)
                }
            }
            .pointerInput(Unit) {
                // Tıklama ile ton seçimi
                detectTapGestures { offset ->
                    sliderPosition = (offset.x / size.width).coerceIn(0f, 1f)
                    onHueChanged(sliderPosition * 360f)
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val x = sliderPosition * size.width
            val y = size.height / 2

            // 🎯 Kaydırıcı İmleci
            drawCircle(color = thumbColor, radius = 10f, center = Offset(x, y))
            drawCircle(color = Color.White, radius = 12f, center = Offset(x, y), style = Stroke(width = 2.5f))
        }
    }
}
