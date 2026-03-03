package com.example.midemegel.data.repository

import com.example.midemegel.data.model.ProductModel
import kotlinx.coroutines.flow.Flow

/**
 * Ürün verilerine erişim sağlamak için kullanılan repository arayüzü.
 */
interface ProductsRepository {
    /**
     * Tüm ürünleri akış (Flow) olarak döndürür.
     */
    fun getAllProducts(): Flow<List<ProductModel>>

    /**
     * Belirtilen ID'ye sahip ürünü döndürür.
     */
    suspend fun getProductById(productId: Int): ProductModel?

    /**
     * Yeni bir ürün ekler.
     */
    suspend fun addProduct(product: ProductModel)

    /**
     * Mevcut bir ürünü günceller.
     */
    suspend fun updateProduct(product: ProductModel)

    /**
     * Belirtilen ürünü siler.
     */
    suspend fun deleteProduct(product: ProductModel)

    /**
     * Belirtilen ID'ye sahip ürünü siler.
     */
    suspend fun deleteProductById(productId: Int)

    /**
     * İsmiyle eşleşen ürünü siler.
     */
    suspend fun removeProductByName(name: String)

    /**
     * Ürünler arasında arama yapar.
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
     * Belirli bir kategoriye ait tüm ürünleri siler.
     */
    suspend fun deleteProductsByCategoryId(categoryId: Int)

    /**
     * Bir sonraki sıralama indeksini döndürür.
     */
    suspend fun getNextOrderIndex(): Int
}