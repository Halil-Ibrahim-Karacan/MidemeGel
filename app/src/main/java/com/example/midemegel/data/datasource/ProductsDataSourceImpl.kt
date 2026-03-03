package com.example.midemegel.data.datasource

import com.example.midemegel.data.database.ProductsDAO
import com.example.midemegel.data.model.ProductModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * ProductsDataSource arayüzünün Room veritabanı kullanarak somut uygulaması.
 * Dispatchers.IO kullanarak veritabanı işlemlerini arka planda yürütür.
 */
class ProductsDataSourceImpl @Inject constructor(
    private val productsDAO: ProductsDAO
) : ProductsDataSource {

    /**
     * Tüm ürünleri veritabanından akış olarak getirir.
     */
    override fun getAllProducts(): Flow<List<ProductModel>> {
        return productsDAO.allProducts()
    }

    /**
     * ID'ye göre ürün getirir.
     */
    override suspend fun getProductById(productId: Int): ProductModel? = withContext(Dispatchers.IO) {
        productsDAO.getProductById(productId)
    }

    /**
     * Yeni bir ürün ekler.
     */
    override suspend fun addProduct(product: ProductModel) = withContext(Dispatchers.IO) {
        productsDAO.addProduct(product)
    }

    /**
     * Mevcut bir ürünü günceller.
     */
    override suspend fun updateProduct(product: ProductModel) = withContext(Dispatchers.IO) {
        productsDAO.updateProduct(product)
    }

    /**
     * Ürünü siler.
     */
    override suspend fun deleteProduct(product: ProductModel) = withContext(Dispatchers.IO) {
        productsDAO.deleteProduct(product)
    }

    /**
     * ID'ye göre ürün siler.
     */
    override suspend fun deleteProductById(productId: Int) = withContext(Dispatchers.IO) {
        productsDAO.deleteProductById(productId)
    }

    /**
     * İsmiyle eşleşen ürünleri siler.
     */
    override suspend fun removeProductByName(name: String) = withContext(Dispatchers.IO) {
        productsDAO.deleteProductsByName(name)
    }

    /**
     * Ürün ismine göre arama yapar.
     */
    override fun searchProducts(query: String): Flow<List<ProductModel>> {
        return productsDAO.searchProducts(query)
    }

    /**
     * Birden fazla ürünü günceller.
     */
    override suspend fun updateProducts(products: List<ProductModel>) {
        productsDAO.updateProducts(products)
    }

    /**
     * Tüm ürünleri temizler.
     */
    override suspend fun clearProducts() = withContext(Dispatchers.IO) {
        productsDAO.clearProduts()
    }

    /**
     * Belirli bir kategoriye ait ürünleri siler.
     */
    override suspend fun deleteProductsByCategoryId(categoryId: Int) = withContext(Dispatchers.IO) {
        productsDAO.deleteProductsByCategoryId(categoryId)
    }

    /**
     * Belirli bir kategoriye ait ürünleri liste olarak getirir.
     */
    override suspend fun getProductsByCategoryId(categoryId: Int): List<ProductModel> {
        return productsDAO.getProductsByCategoryId(categoryId)
    }

    /**
     * Yeni eklenecek ürün için sıradaki sıralama indeksini hesaplar.
     */
    override suspend fun getNextOrderIndex(): Int = withContext(Dispatchers.IO) {
        productsDAO.getNextOrderIndex()
    }
}
