package com.example.midemegel.screens.editactivities.editcategory.editcategorycomponents

import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.example.midemegel.data.model.CategoryModel
import kotlin.math.roundToInt

/**
 * Kategorilerin dikey bir liste halinde gösterildiği ve sürükle-bırak yöntemiyle 
 * sıralanabildiği bileşen.
 */
@Composable
fun EditCategoryList(
    lazyListState: LazyListState,
    categories: MutableList<CategoryModel>,
    itemBackgroundColor: Color,
    itemTextColor: Color,
    listItemHeight: Dp,
    listItemTextSize: TextUnit,
    iconSize: Dp,
    onOrderChanged: (List<CategoryModel>) -> Unit,
    onEditClick: (CategoryModel) -> Unit,
    onDeleteClick: (CategoryModel) -> Unit
) {
    // --- Mantıksal Değişkenler (Sürükle-Bırak Durumları) ---
    
    // Şu an sürüklenen öğenin indeksi
    var draggedItemIndex by remember { mutableStateOf<Int?>(null) }
    // Sürüklenen öğenin üzerine geldiği hedef indeks
    var draggedOverIndex by remember { mutableStateOf<Int?>(null) }
    // Sürüklenen öğenin dikeydeki hareket mesafesi (piksel cinsinden)
    var draggedItemOffset by remember { mutableStateOf(0f) }

    // --- Tasarım Bileşenleri ---
    LazyColumn(
        state = lazyListState,
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(bottom = 80.dp) // Alttaki butonun üzerini kapatmaması için boşluk
    ) {
        itemsIndexed(categories, key = { _, item -> item.id }) { index, category ->
            // --- Öğeye Özel Mantıksal Hesaplamalar ---
            val isDragged = draggedItemIndex == index
            val offsetAnim = if (isDragged) draggedItemOffset else 0f

            // Her bir kategori satırının tasarımı
            EditCategoryItem(
                category = category,
                isDragged = isDragged,
                offsetAnim = offsetAnim,
                itemBackgroundColor = itemBackgroundColor,
                itemTextColor = itemTextColor,
                listItemHeight = listItemHeight,
                listItemTextSize = listItemTextSize,
                iconSize = iconSize,
                // Sürükleme mantığını içeren modifier
                onDragModifier = Modifier.pointerInput(index) {
                    detectDragGesturesAfterLongPress(
                        onDragStart = {
                            draggedItemIndex = index
                            draggedOverIndex = index
                        },
                        onDragEnd = {
                            // Sürükleme bittiğinde listeyi güncelle ve değişikliği bildir
                            draggedItemIndex?.let { start ->
                                draggedOverIndex?.let { target ->
                                    if (start != target) {
                                        val item = categories.removeAt(start)
                                        categories.add(target, item)
                                        // Yeni sıralamayı düzenleyip ViewModel'e/üst bileşene gönder
                                        onOrderChanged(categories.mapIndexed { i, cat -> cat.copy(orderIndex = i) })
                                    }
                                }
                            }
                            // Durumları sıfırla
                            draggedItemIndex = null
                            draggedOverIndex = null
                            draggedItemOffset = 0f
                        },
                        onDragCancel = {
                            draggedItemIndex = null
                            draggedOverIndex = null
                            draggedItemOffset = 0f
                        },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            draggedItemOffset += dragAmount.y
                            // Kaçıncı öğenin üzerine gelindiğini yaklaşık olarak hesapla
                            draggedItemIndex?.let { start ->
                                draggedOverIndex = (start + (draggedItemOffset / 90.dp.toPx()).roundToInt())
                                    .coerceIn(0, categories.size - 1)
                            }
                        }
                    )
                },
                onEditClick = { onEditClick(category) },
                onDeleteClick = { onDeleteClick(category) },
                // Kahvehane kategorisi sistem için kritik olduğundan silme butonunu gizle
                showDelete = !category.name.equals("Kahvehane")
            )
        }
    }
}
