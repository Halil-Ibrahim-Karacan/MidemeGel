package com.example.midemegel.data.datasource

import com.example.midemegel.data.database.BagProductsDAO
import com.example.midemegel.data.model.BagProductModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * BagProductsDataSource arayüzünün Room veritabanı kullanarak somut uygulaması.
 * Sepet (çanta) işlemlerini IO thread'inde gerçekleştirir.
 */
class BagProductsDataSourceImpl @Inject constructor(
    private val bagProductsDAO: BagProductsDAO
) : BagProductsDataSource {

    /**
     * Sepetteki tüm ürünleri akış olarak döndürür.
     */
    override fun getAllProducts(): Flow<List<BagProductModel>> {
        return bagProductsDAO.allProducts()
    }

    /**
     * ID'ye göre sepetteki bir ürünü getirir.
     */
    override suspend fun getProductById(productId: Int): BagProductModel? = withContext(Dispatchers.IO) {
        bagProductsDAO.getProductById(productId)
    }

    /**
     * Sepete yeni bir ürün ekler.
     */
    override suspend fun addProduct(product: BagProductModel) = withContext(Dispatchers.IO) {
        bagProductsDAO.addProduct(product)
    }

    /**
     * Sepetteki bir ürünün bilgilerini günceller.
     */
    override suspend fun updateProduct(product: BagProductModel) = withContext(Dispatchers.IO) {
        bagProductsDAO.updateProduct(product)
    }

    /**
     * Ürünü sepetten siler.
     */
    override suspend fun deleteProduct(product: BagProductModel) = withContext(Dispatchers.IO) {
        bagProductsDAO.deleteProduct(product)
    }

    /**
     * ID'ye göre ürünü sepetten siler.
     */
    override suspend fun deleteProductById(productId: Int) = withContext(Dispatchers.IO) {
        bagProductsDAO.deleteProductById(productId)
    }

    /**
     * İsmiyle eşleşen ürünleri sepetten kaldırır.
     */
    override suspend fun removeProductByName(name: String) = withContext(Dispatchers.IO) {
        bagProductsDAO.deleteProductsByName(name)
    }

    /**
     * Sepet içindeki ürünlerde isim araması yapar.
     */
    override fun searchProducts(query: String): Flow<List<BagProductModel>> {
        return bagProductsDAO.searchProducts(query)
    }

    /**
     * Sepeti tamamen temizler.
     */
    override suspend fun clearBag() = withContext(Dispatchers.IO) {
        bagProductsDAO.clearBag()
    }

    /**
     * Belirli bir kategoriye ait tüm sepet ürünlerini siler.
     */
    override suspend fun deleteBagProductsByCategoryId(categoryId: Int) = withContext(Dispatchers.IO) {
        bagProductsDAO.deleteBagProductsByCategoryId(categoryId)
    }
}
