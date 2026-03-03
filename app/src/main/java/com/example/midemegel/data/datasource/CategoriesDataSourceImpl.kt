package com.example.midemegel.data.datasource

import com.example.midemegel.data.database.CategoriesDAO
import com.example.midemegel.data.model.CategoryModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * CategoriesDataSource arayüzünün Room veritabanı kullanarak somut uygulaması.
 * Kategori işlemlerini IO thread'inde gerçekleştirir.
 */
class CategoriesDataSourceImpl @Inject constructor(private val categoriesDAO: CategoriesDAO) : CategoriesDataSource {
    
    /**
     * Tüm kategorileri akış olarak döndürür.
     */
    override fun getAllCategories(): Flow<List<CategoryModel>> {
        return categoriesDAO.allCategories()
    }

    /**
     * ID'ye göre kategori getirir.
     */
    override suspend fun getCategoryById(categoryId: Int): CategoryModel? = withContext(Dispatchers.IO) {
        categoriesDAO.getCategoryById(categoryId)
    }

    /**
     * ID'ye göre kategori adını getirir.
     */
    override suspend fun getCategoryNameById(categoryId: Int): String? = withContext(Dispatchers.IO) {
        categoriesDAO.getCategoryNameById(categoryId)
    }

    /**
     * ID'ye göre kategori animasyon yolunu getirir.
     */
    override suspend fun getCategoryAnimationById(categoryId: Int): String? = withContext(Dispatchers.IO) {
        categoriesDAO.getCategoryAnimationById(categoryId)
    }

    /**
     * Yeni bir kategori ekler.
     */
    override suspend fun addCategory(category: CategoryModel) = withContext(Dispatchers.IO) {
        categoriesDAO.addCategory(category)
    }

    /**
     * Kategori bilgilerini günceller.
     */
    override suspend fun updateCategory(category: CategoryModel) = withContext(Dispatchers.IO) {
        categoriesDAO.updateCategory(category)
    }

    /**
     * Kategoriyi siler.
     */
    override suspend fun deleteCategory(category: CategoryModel) = withContext(Dispatchers.IO) {
        categoriesDAO.deleteCategory(category)
    }

    /**
     * ID'ye göre kategori siler.
     */
    override suspend fun deleteCategoryById(categoryId: Int) = withContext(Dispatchers.IO) {
        categoriesDAO.deleteCategoryById(categoryId)
    }

    /**
     * İsmiyle kategori siler.
     */
    override suspend fun removeCategoryByName(name: String) = withContext(Dispatchers.IO) {
        categoriesDAO.deleteCetagoriesByName(name)
    }

    /**
     * Kategori araması yapar.
     */
    override fun searchCategories(query: String): Flow<List<CategoryModel>> {
        return categoriesDAO.searchCategories(query)
    }

    /**
     * Tüm kategorileri siler.
     */
    override suspend fun clearCategories() = withContext(Dispatchers.IO) {
        categoriesDAO.clearCategories()
    }

    /**
     * Birden fazla kategoriyi günceller.
     */
    override suspend fun updateCategories(categories: List<CategoryModel>) = withContext(Dispatchers.IO) {
        categoriesDAO.updateCategories(categories)
    }

    /**
     * Yeni kategori için sıradaki indeksi hesaplar.
     */
    override suspend fun getNextOrderIndex(): Int = withContext(Dispatchers.IO) {
        categoriesDAO.getNextOrderIndex()
    }
}