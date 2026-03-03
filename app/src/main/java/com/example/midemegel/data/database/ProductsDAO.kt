package com.example.midemegel.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.midemegel.data.model.ProductModel
import kotlinx.coroutines.flow.Flow

/**
 * Ürünler tablosu için Veri Erişim Nesnesi (DAO).
 * Room veritabanı üzerinden ürün ekleme, silme, güncelleme ve sorgulama işlemlerini tanımlar.
 */
@Dao
interface ProductsDAO {
    /**
     * Tüm ürünleri sıralama indeksine göre artan sırada getirir.
     */
    @Query("SELECT * FROM products ORDER BY product_orderIndex ASC")
    fun allProducts() : Flow<List<ProductModel>>

    /**
     * Veritabanına yeni bir ürün ekler veya çakışma durumunda mevcut olanın üzerine yazar.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addProduct(product: ProductModel)

    /**
     * Mevcut bir ürünün bilgilerini günceller.
     */
    @Update
    suspend fun updateProduct(product: ProductModel)

    /**
     * Belirtilen ürünü veritabanından siler.
     */
    @Delete
    suspend fun deleteProduct(product: ProductModel)

    /**
     * ID'ye göre ürün detayını getirir.
     */
    @Query("SELECT * FROM products WHERE product_id = :id")
    suspend fun getProductById(id: Int): ProductModel?

    /**
     * Ürün ismi içerisinde arama yapar ve sıralı olarak döndürür.
     */
    @Query("SELECT * FROM products WHERE product_name LIKE '%' || :search || '%' ORDER BY product_orderIndex ASC")
    fun searchProducts(search:String) : Flow<List<ProductModel>>

    /**
     * Tüm ürünleri veritabanından temizler.
     */
    @Query("DELETE FROM products")
    suspend fun clearProduts()

    /**
     * İsmiyle eşleşen tüm ürünleri siler.
     */
    @Query("DELETE FROM products WHERE product_name = :name")
    suspend fun deleteProductsByName(name: String)

    /**
     * Belirtilen ID'ye sahip ürünü siler.
     */
    @Query("DELETE FROM products WHERE product_id = :id")
    suspend fun deleteProductById(id: Int)

    /**
     * Verilen ürün listesini toplu olarak günceller.
     */
    @Update
    suspend fun updateProducts(products: List<ProductModel>)

    /**
     * Belirli bir kategoriye ait tüm ürünleri siler.
     */
    @Query("DELETE FROM products WHERE product_category = :categoryId")
    suspend fun deleteProductsByCategoryId(categoryId: Int)

    /**
     * Belirli bir kategoriye ait ürünlerin listesini getirir.
     */
    @Query("SELECT * FROM products WHERE product_category = :categoryId")
    suspend fun getProductsByCategoryId(categoryId: Int): List<ProductModel>

    /**
     * Yeni eklenecek ürün için mevcut en büyük indeksi bulup 1 artırarak döndürür.
     */
    @Query("SELECT COALESCE(MAX(product_orderIndex), 0) + 1 FROM products")
    suspend fun getNextOrderIndex(): Int
}
