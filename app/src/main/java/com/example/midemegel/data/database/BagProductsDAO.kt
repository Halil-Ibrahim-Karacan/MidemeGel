package com.example.midemegel.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.midemegel.data.model.BagProductModel
import kotlinx.coroutines.flow.Flow

/**
 * Alışveriş sepeti (çanta) tablosu için Veri Erişim Nesnesi (DAO).
 * Room veritabanı üzerinden sepet işlemlerini tanımlar.
 */
@Dao
interface BagProductsDAO {
    /**
     * Sepetteki tüm ürünleri getirir.
     */
    @Query("SELECT * FROM bagProducts")
    fun allProducts() : Flow<List<BagProductModel>>

    /**
     * Sepete yeni ürün ekler veya varsa günceller.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addProduct(product: BagProductModel)

    /**
     * Sepetteki bir ürünün bilgilerini günceller.
     */
    @Update
    suspend fun updateProduct(product: BagProductModel)

    /**
     * Ürünü sepetten siler.
     */
    @Delete
    suspend fun deleteProduct(product: BagProductModel)

    /**
     * ID'ye göre sepetteki bir ürünü getirir.
     */
    @Query("SELECT * FROM bagProducts WHERE product_id = :id")
    suspend fun getProductById(id: Int): BagProductModel?

    /**
     * Sepet içinde isim araması yapar.
     */
    @Query("SELECT * FROM bagProducts WHERE product_name LIKE '%' || :search || '%' ")
    fun searchProducts(search:String) : Flow<List<BagProductModel>>

    /**
     * Sepeti tamamen boşaltır.
     */
    @Query("DELETE FROM bagProducts")
    suspend fun clearBag()

    /**
     * İsmiyle eşleşen ürünleri sepetten siler.
     */
    @Query("DELETE FROM bagProducts WHERE product_name = :name")
    suspend fun deleteProductsByName(name: String)

    /**
     * ID'ye göre ürünü sepetten siler.
     */
    @Query("DELETE FROM bagProducts WHERE product_id = :id")
    suspend fun deleteProductById(id: Int)

    /**
     * Belirli bir kategoriye ait tüm ürünleri sepetten siler.
     */
    @Query("DELETE FROM bagProducts WHERE product_category = :categoryId")
    suspend fun deleteBagProductsByCategoryId(categoryId: Int)

    /**
     * Sepet ürünlerini toplu olarak günceller.
     */
    @Update
    suspend fun updateProducts(products: List<BagProductModel>)
}
