package com.example.midemegel.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.midemegel.data.model.CategoryModel
import com.example.midemegel.data.repository.BagProductsRepositoryImpl
import com.example.midemegel.data.repository.CategoriesRepositoryImpl
import com.example.midemegel.data.repository.ProductsRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

/**
 * Ürün kategorilerini yöneten ve UI katmanına sağlayan ViewModel.
 * Kategorilerin listelenmesi, eklenmesi, güncellenmesi ve silinmesi işlemlerini, 
 * ilişkili ürün verileriyle birlikte koordine eder.
 */
@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val categoriesRepositoryImpl: CategoriesRepositoryImpl,
    private val productsRepositoryImpl: ProductsRepositoryImpl,
    private val bagProductsRepositoryImpl: BagProductsRepositoryImpl
) : ViewModel() {

    // Tüm kategorileri tutan akış
    private val _categoriesState = MutableStateFlow<List<CategoryModel>>(emptyList())
    val categoriesState: StateFlow<List<CategoryModel>> = _categoriesState.asStateFlow()

    // Seçili kategoriyi tutan akış
    private val _selectedCategoryState = MutableStateFlow<CategoryModel?>(null)
    val selectedCategoryState: StateFlow<CategoryModel?> = _selectedCategoryState.asStateFlow()

    // Kategori adını tutan akış
    private val _categoryNameState = MutableStateFlow<String?>(null)
    val categoryNameState: StateFlow<String?> = _categoryNameState.asStateFlow()

    init {
        getAllCategories()
    }

    /**
     * Tüm kategorileri veritabanından getirir.
     */
    fun getAllCategories() {
        viewModelScope.launch {
            categoriesRepositoryImpl.getAllCategories().collect { list ->
                _categoriesState.value = list
            }
        }
    }

    /**
     * ID'ye göre kategori bilgisini getirir.
     */
    fun getCategoryById(categoryId: Int) {
        viewModelScope.launch {
            _selectedCategoryState.value = categoriesRepositoryImpl.getCategoryById(categoryId)
        }
    }

    /**
     * ID'ye göre kategori adını döndürür.
     */
    suspend fun getCategoryNameById(categoryId: Int): String {
        return categoriesRepositoryImpl.getCategoryNameById(categoryId) ?: ""
    }

    /**
     * ID'ye göre kategori animasyon yolunu döndürür.
     */
    suspend fun getCategoryAnimationById(categoryId: Int): String {
        return categoriesRepositoryImpl.getCategoryAnimationById(categoryId) ?: ""
    }

    /**
     * Yeni bir kategori ekler.
     */
    fun addCategory(category: CategoryModel) {
        viewModelScope.launch {
            val nextIndex = categoriesRepositoryImpl.getNextOrderIndex()
            val categoryWithIndex = category.copy(orderIndex = nextIndex)
            categoriesRepositoryImpl.addCategory(categoryWithIndex)
        }
    }

    /**
     * Mevcut bir kategoriyi günceller.
     */
    fun updateCategory(category: CategoryModel) {
        viewModelScope.launch {
            categoriesRepositoryImpl.updateCategory(category)
        }
    }

    /**
     * Birden fazla kategoriyi günceller.
     */
    fun updateCategories(categories: List<CategoryModel>) {
        viewModelScope.launch {
            categoriesRepositoryImpl.updateCategories(categories)
        }
    }

    /**
     * Kategoriyi ve kategorinin tüm ürünlerini (resimleriyle birlikte) siler.
     */
    fun deleteCategory(category: CategoryModel) {
        viewModelScope.launch {
            // 1. Kategoriye ait ürünleri getir
            val productsToDelete = productsRepositoryImpl.getProductsByCategoryId(category.id)
            
            // 2. Ürünlerin resimlerini sil
            productsToDelete.forEach { product ->
                if (product.image.isNotEmpty()) {
                    val file = File(product.image)
                    if (file.exists()) {
                        file.delete()
                    }
                }
            }

            // 3. Kategorinin kendi resmini sil
            if (category.image.isNotEmpty()) {
                val categoryImageFile = File(category.image)
                if (categoryImageFile.exists()) {
                    categoryImageFile.delete()
                }
            }

            // 4. Veritabanından silme işlemlerini yap
            productsRepositoryImpl.deleteProductsByCategoryId(category.id)
            bagProductsRepositoryImpl.deleteBagProductsByCategoryId(category.id)
            categoriesRepositoryImpl.deleteCategory(category)
        }
    }

    /**
     * Kategori adına göre silme işlemi yapar.
     */
    fun removeCategory(categoryId: String) {
        viewModelScope.launch {
            categoriesRepositoryImpl.removeCategoryByName(categoryId)
        }
    }

    /**
     * Kategoriler arasında arama yapar.
     */
    fun searchCategories(query: String) {
        viewModelScope.launch {
            categoriesRepositoryImpl.searchCategories(query).collect { filteredList ->
                _categoriesState.value = filteredList
            }
        }
    }

    /**
     * Tüm kategorileri temizler.
     */
    fun clearCategories() {
        viewModelScope.launch {
            categoriesRepositoryImpl.clearCategories()
        }
    }
}