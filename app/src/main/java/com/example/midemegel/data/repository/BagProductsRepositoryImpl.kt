package com.example.midemegel.data.repository

import com.example.midemegel.data.datasource.BagProductsDataSourceImpl
import com.example.midemegel.data.model.BagProductModel
import kotlinx.coroutines.flow.Flow
import java.io.File
import javax.inject.Inject

/**
 * Sepet ürünleri için veri erişim işlemlerini yöneten Repository implementasyonu.
 * Veri kaynağı (DataSource) ile ViewModel arasındaki köprüyü kurar.
 */
class BagProductsRepositoryImpl @Inject constructor(val bagProductsDataSourceImpl: BagProductsDataSourceImpl) : BagProductsRepository {
    
    /**
     * Tüm sepet ürünlerini Flow olarak getirir.
     */
    override fun getAllProducts(): Flow<List<BagProductModel>> = bagProductsDataSourceImpl.getAllProducts()

    /**
     * ID'sine göre tek bir sepet ürününü getirir.
     */
    override suspend fun getProductById(productId: Int): BagProductModel? = bagProductsDataSourceImpl.getProductById(productId)

    /**
     * Sepete yeni bir ürün ekler.
     */
    override suspend fun addProduct(product: BagProductModel) = bagProductsDataSourceImpl.addProduct(product)

    /**
     * Mevcut bir sepet ürününü günceller.
     */
    override suspend fun updateProduct(product: BagProductModel) = bagProductsDataSourceImpl.updateProduct(product)

    /**
     * Bir ürünü sepetten siler.
     */
    override suspend fun deleteProduct(product: BagProductModel) = bagProductsDataSourceImpl.deleteProduct(product)

    /**
     * Bir ürünü ID'sine göre sepetten siler.
     */
    override suspend fun deleteProductById(productId: Int) = bagProductsDataSourceImpl.deleteProductById(productId)

    /**
     * Bir ürünü ismine göre sepetten siler.
     */
    override suspend fun removeProductByName(name: String, image: String) {
        // Ürünün resmini sil
        if (image.isNotEmpty()) {
            val file = File(image)
            if (file.exists()) {
                file.delete()
            }
        }
        bagProductsDataSourceImpl.removeProductByName(name)
    }

    /**
     * Sepetteki ürünler arasında arama yapar.
     */
    override fun searchProducts(query: String): Flow<List<BagProductModel>> = bagProductsDataSourceImpl.searchProducts(query)

    /**
     * Sepeti tamamen temizler.
     */
    override suspend fun clearBag() = bagProductsDataSourceImpl.clearBag()

    /**
     * Belirli bir kategoriye ait tüm ürünleri sepetten siler.
     */
    override suspend fun deleteBagProductsByCategoryId(categoryId: Int) = bagProductsDataSourceImpl.deleteBagProductsByCategoryId(categoryId)
}
