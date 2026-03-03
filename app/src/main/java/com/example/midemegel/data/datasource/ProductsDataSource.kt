package com.example.midemegel.data.datasource

import com.example.midemegel.data.model.ProductModel
import kotlinx.coroutines.flow.Flow

/**
 * Ürün verilerine erişim için temel veri kaynağı arayüzü.
 */
interface ProductsDataSource {
    /**
     * Tüm ürünleri akış (Flow) olarak döndürür.
     */
    fun getAllProducts(): Flow<List<ProductModel>>

    /**
     * ID'ye göre ürünü döndürür.
     */
    suspend fun getProductById(productId: Int): ProductModel?

    /**
     * Yeni ürün ekler.
     */
    suspend fun addProduct(product: ProductModel)

    /**
     * Ürün bilgilerini günceller.
     */
    suspend fun updateProduct(product: ProductModel)

    /**
     * Ürünü siler.
     */
    suspend fun deleteProduct(product: ProductModel)

    /**
     * ID'ye göre ürün siler.
     */
    suspend fun deleteProductById(productId: Int)

    /**
     * İsmiyle ürün siler.
     */
    suspend fun removeProductByName(name: String)

    /**
     * Ürünler içinde arama yapar.
     */
    fun searchProducts(query: String): Flow<List<ProductModel>>

    /**
     * Birden fazla ürünü günceller.
     */
    suspend fun updateProducts(products: List<ProductModel>)

    /**
     * Tüm ürünleri temizler.
     */
    suspend fun clearProducts()

    /**
     * Kategori ID'sine göre ürünleri siler.
     */
    suspend fun deleteProductsByCategoryId(categoryId: Int)

    /**
     * Belirli bir kategoriye ait ürün listesini döndürür.
     */
    suspend fun getProductsByCategoryId(categoryId: Int): List<ProductModel>

    /**
     * Bir sonraki sıralama indeksini hesaplar.
     */
    suspend fun getNextOrderIndex(): Int
}
