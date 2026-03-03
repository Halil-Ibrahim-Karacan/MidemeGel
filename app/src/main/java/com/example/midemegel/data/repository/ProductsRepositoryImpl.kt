package com.example.midemegel.data.repository

import com.example.midemegel.data.datasource.ProductsDataSourceImpl
import com.example.midemegel.data.model.ProductModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * ProductsRepository arayüzünün somut uygulaması.
 * Ürün verilerine erişim için ProductsDataSourceImpl'i kullanır.
 */
class ProductsRepositoryImpl @Inject constructor(val productsDataSourceImpl: ProductsDataSourceImpl) : ProductsRepository {
    
    /**
     * Tüm ürünleri getirir.
     */
    override fun getAllProducts(): Flow<List<ProductModel>> = productsDataSourceImpl.getAllProducts()

    /**
     * ID ile ürün getirir.
     */
    override suspend fun getProductById(productId: Int): ProductModel? = productsDataSourceImpl.getProductById(productId)

    /**
     * Yeni ürün ekler.
     */
    override suspend fun addProduct(product: ProductModel) = productsDataSourceImpl.addProduct(product)

    /**
     * Ürün günceller.
     */
    override suspend fun updateProduct(product: ProductModel) = productsDataSourceImpl.updateProduct(product)

    /**
     * Ürün siler.
     */
    override suspend fun deleteProduct(product: ProductModel) = productsDataSourceImpl.deleteProduct(product)

    /**
     * ID ile ürün siler.
     */
    override suspend fun deleteProductById(productId: Int) = productsDataSourceImpl.deleteProductById(productId)

    /**
     * İsmiyle ürün siler.
     */
    override suspend fun removeProductByName(name: String) = productsDataSourceImpl.removeProductByName(name)

    /**
     * Ürün araması yapar.
     */
    override fun searchProducts(query: String): Flow<List<ProductModel>> = productsDataSourceImpl.searchProducts(query)

    /**
     * Listelenen ürünleri günceller.
     */
    override suspend fun updateProducts(products: List<ProductModel>) = productsDataSourceImpl.updateProducts(products)

    /**
     * Tüm ürünleri siler.
     */
    override suspend fun clearProducts() = productsDataSourceImpl.clearProducts()
    
    /**
     * Kategori ID'sine göre ürünleri siler.
     */
    override suspend fun deleteProductsByCategoryId(categoryId: Int) = productsDataSourceImpl.deleteProductsByCategoryId(categoryId)
    
    /**
     * Yeni eklenecek ürün için sıradaki indeksi döndürür.
     */
    override suspend fun getNextOrderIndex(): Int = productsDataSourceImpl.getNextOrderIndex()

    /**
     * Belirli bir kategoriye ait ürün listesini getirir.
     */
    suspend fun getProductsByCategoryId(categoryId: Int): List<ProductModel> = productsDataSourceImpl.getProductsByCategoryId(categoryId)

}
