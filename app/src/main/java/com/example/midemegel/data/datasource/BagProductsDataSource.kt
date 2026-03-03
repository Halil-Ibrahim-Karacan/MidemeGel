package com.example.midemegel.data.datasource

import com.example.midemegel.data.model.BagProductModel
import kotlinx.coroutines.flow.Flow

/**
 * Alışveriş sepetindeki (çanta) ürün verilerine erişim için temel veri kaynağı arayüzü.
 */
interface BagProductsDataSource {
    /**
     * Sepetteki tüm ürünleri akış (Flow) olarak döndürür.
     */
    fun getAllProducts(): Flow<List<BagProductModel>>

    /**
     * ID'ye göre sepetteki bir ürünü döndürür.
     */
    suspend fun getProductById(productId: Int): BagProductModel?

    /**
     * Sepete yeni bir ürün ekler.
     */
    suspend fun addProduct(product: BagProductModel)

    /**
     * Sepetteki bir ürünün bilgilerini günceller.
     */
    suspend fun updateProduct(product: BagProductModel)

    /**
     * Ürünü sepetten siler.
     */
    suspend fun deleteProduct(product: BagProductModel)

    /**
     * ID'ye göre ürünü sepetten siler.
     */
    suspend fun deleteProductById(productId: Int)

    /**
     * İsmiyle eşleşen ürünü sepetten kaldırır.
     */
    suspend fun removeProductByName(name: String)

    /**
     * Sepet içindeki ürünlerde arama yapar.
     */
    fun searchProducts(query: String): Flow<List<BagProductModel>>

    /**
     * Sepeti tamamen boşaltır.
     */
    suspend fun clearBag()

    /**
     * Belirli bir kategoriye ait tüm ürünleri sepetten siler.
     */
    suspend fun deleteBagProductsByCategoryId(categoryId: Int)
}
