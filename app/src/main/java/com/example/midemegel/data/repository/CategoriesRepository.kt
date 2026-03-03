package com.example.midemegel.data.repository

import com.example.midemegel.data.model.CategoryModel
import kotlinx.coroutines.flow.Flow

/**
 * Ürün kategorilerine erişim sağlamak için kullanılan repository arayüzü.
 */
interface CategoriesRepository {
    /**
     * Tüm kategorileri akış (Flow) olarak döndürür.
     */
    fun getAllCategories(): Flow<List<CategoryModel>>

    /**
     * ID'ye göre kategori bilgisini getirir.
     */
    suspend fun getCategoryById(categoryId: Int): CategoryModel?

    /**
     * ID'ye göre kategori adını döndürür.
     */
    suspend fun getCategoryNameById(categoryId: Int): String?

    /**
     * ID'ye göre kategori animasyon yolunu döndürür.
     */
    suspend fun getCategoryAnimationById(categoryId: Int): String?

    /**
     * Yeni bir kategori ekler.
     */
    suspend fun addCategory(category: CategoryModel)

    /**
     * Mevcut bir kategoriyi günceller.
     */
    suspend fun updateCategory(category: CategoryModel)

    /**
     * Belirtilen kategoriyi siler.
     */
    suspend fun deleteCategory(category: CategoryModel)

    /**
     * ID'ye göre kategori siler.
     */
    suspend fun deleteCategoryById(categoryId: Int)

    /**
     * İsmiyle kategori siler.
     */
    suspend fun removeCategoryByName(name: String)

    /**
     * Kategoriler arasında arama yapar.
     */
    fun searchCategories(query: String): Flow<List<CategoryModel>>

    /**
     * Tüm kategorileri temizler.
     */
    suspend fun clearCategories()

    /**
     * Birden fazla kategoriyi günceller.
     */
    suspend fun updateCategories(categories: List<CategoryModel>)

    /**
     * Yeni eklenecek kategori için bir sonraki sıralama indeksini döndürür.
     */
    suspend fun getNextOrderIndex(): Int
}
