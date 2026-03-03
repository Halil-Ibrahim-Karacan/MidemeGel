package com.example.midemegel.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.midemegel.data.model.CategoryModel
import kotlinx.coroutines.flow.Flow

/**
 * Kategoriler tablosu için Veri Erişim Nesnesi (DAO).
 * Room veritabanı üzerinden kategori ekleme, silme, güncelleme ve sorgulama işlemlerini tanımlar.
 */
@Dao
interface CategoriesDAO {
    /**
     * Tüm kategorileri sıralama indeksine göre artan sırada getirir.
     */
    @Query("SELECT * FROM categories ORDER BY category_orderIndex ASC")
    fun allCategories() : Flow<List<CategoryModel>>

    /**
     * Yeni bir kategori ekler veya çakışma durumunda mevcut olanın üzerine yazar.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCategory(category: CategoryModel)

    /**
     * Mevcut bir kategorinin bilgilerini günceller.
     */
    @Update
    suspend fun updateCategory(category: CategoryModel)

    /**
     * Belirtilen kategoriyi veritabanından siler.
     */
    @Delete
    suspend fun deleteCategory(category: CategoryModel)

    /**
     * ID'ye göre kategori bilgisini getirir.
     */
    @Query("SELECT * FROM categories WHERE category_id = :id")
    suspend fun getCategoryById(id: Int): CategoryModel?

    /**
     * ID'ye göre kategorinin ismini döndürür.
     */
    @Query("SELECT category_name FROM categories WHERE category_id = :categoryId LIMIT 1")
    suspend fun getCategoryNameById(categoryId: Int): String?

    /**
     * ID'ye göre kategorinin animasyon yolunu döndürür.
     */
    @Query("SELECT category_animation FROM categories WHERE category_id = :categoryId LIMIT 1")
    suspend fun getCategoryAnimationById(categoryId: Int): String?

    /**
     * Kategori ismi içinde arama yapar ve sıralı olarak döndürür.
     */
    @Query("SELECT * FROM categories WHERE category_name LIKE '%' || :search || '%' ORDER BY category_orderIndex ASC")
    fun searchCategories(search:String) : Flow<List<CategoryModel>>

    /**
     * Tüm kategorileri veritabanından temizler.
     */
    @Query("DELETE FROM categories")
    suspend fun clearCategories()

    /**
     * İsmiyle eşleşen tüm kategorileri siler.
     */
    @Query("DELETE FROM categories WHERE category_name = :name")
    suspend fun deleteCetagoriesByName(name: String)

    /**
     * ID'ye göre kategoriyi siler.
     */
    @Query("DELETE FROM categories WHERE category_id = :id")
    suspend fun deleteCategoryById(id: Int)

    /**
     * Verilen kategori listesini toplu olarak günceller.
     */
    @Update
    suspend fun updateCategories(categories: List<CategoryModel>)

    /**
     * Yeni eklenecek kategori için mevcut en büyük indeksi bulup 1 artırarak döndürür.
     */
    @Query("SELECT COALESCE(MAX(category_orderIndex), 0) + 1 FROM categories")
    suspend fun getNextOrderIndex(): Int
}
