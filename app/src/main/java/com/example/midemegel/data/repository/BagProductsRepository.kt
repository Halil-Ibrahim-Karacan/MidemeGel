package com.example.midemegel.data.repository

import com.example.midemegel.data.model.BagProductModel
import kotlinx.coroutines.flow.Flow

/**
 * Alışveriş sepetindeki (çanta) ürün verilerine erişim sağlamak için kullanılan repository arayüzü.
 */
interface BagProductsRepository {
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
     * Sepetteki bir ürünün bilgilerini (örneğin adet) günceller.
     */
    suspend fun updateProduct(product: BagProductModel)

    /**
     * Belirtilen ürünü sepetten siler.
     */
    suspend fun deleteProduct(product: BagProductModel)

    /**
     * ID'ye göre ürünü sepetten siler.
     */
    suspend fun deleteProductById(productId: Int)

    /**
     * İsmi ve resim yoluyla eşleşen ürünü sepetten kaldırır.
     */
    suspend fun removeProductByName(name: String, image: String)

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