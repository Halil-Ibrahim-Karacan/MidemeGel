package com.example.midemegel.data.repository

import com.example.midemegel.data.datasource.CategoriesDataSourceImpl
import com.example.midemegel.data.model.CategoryModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * CategoriesRepository arayüzünün somut uygulaması.
 * Kategori verilerine erişim için CategoriesDataSourceImpl'i kullanır.
 */
class CategoriesRepositoryImpl @Inject constructor(private val categoriesDataSourceImpl: CategoriesDataSourceImpl) :
    CategoriesRepository {
    
    /**
     * Tüm kategorileri getirir.
     */
    override fun getAllCategories(): Flow<List<CategoryModel>> = categoriesDataSourceImpl.getAllCategories()

    /**
     * ID ile kategori getirir.
     */
    override suspend fun getCategoryById(categoryId: Int): CategoryModel? = categoriesDataSourceImpl.getCategoryById(categoryId)

    /**
     * ID ile kategori adını getirir.
     */
    override suspend fun getCategoryNameById(categoryId: Int): String? = categoriesDataSourceImpl.getCategoryNameById(categoryId)

    /**
     * ID ile kategori animasyonunu getirir.
     */
    override suspend fun getCategoryAnimationById(categoryId: Int): String? = categoriesDataSourceImpl.getCategoryAnimationById(categoryId)

    /**
     * Yeni kategori ekler.
     */
    override suspend fun addCategory(category: CategoryModel) = categoriesDataSourceImpl.addCategory(category)

    /**
     * Kategori günceller.
     */
    override suspend fun updateCategory(category: CategoryModel) = categoriesDataSourceImpl.updateCategory(category)

    /**
     * Kategoriyi siler.
     */
    override suspend fun deleteCategory(category: CategoryModel) = categoriesDataSourceImpl.deleteCategory(category)

    /**
     * ID ile kategori siler.
     */
    override suspend fun deleteCategoryById(categoryId: Int) = categoriesDataSourceImpl.deleteCategoryById(categoryId)

    /**
     * İsmiyle kategori siler.
     */
    override suspend fun removeCategoryByName(name: String) = categoriesDataSourceImpl.removeCategoryByName(name)

    /**
     * Kategori araması yapar.
     */
    override fun searchCategories(query: String): Flow<List<CategoryModel>> = categoriesDataSourceImpl.searchCategories(query)

    /**
     * Tüm kategorileri temizler.
     */
    override suspend fun clearCategories() = categoriesDataSourceImpl.clearCategories()
    
    /**
     * Birden fazla kategoriyi günceller.
     */
    override suspend fun updateCategories(categories: List<CategoryModel>) = categoriesDataSourceImpl.updateCategories(categories)
    
    /**
     * Yeni eklenecek kategori için sıradaki indeksi getirir.
     */
    override suspend fun getNextOrderIndex(): Int = categoriesDataSourceImpl.getNextOrderIndex()
}