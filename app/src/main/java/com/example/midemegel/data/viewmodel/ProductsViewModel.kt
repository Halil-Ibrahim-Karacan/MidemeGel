package com.example.midemegel.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.midemegel.data.model.ProductModel
import com.example.midemegel.data.repository.BagProductsRepositoryImpl
import com.example.midemegel.data.repository.ProductsRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

/**
 * Ürün verilerini yöneten ve UI katmanına sağlayan ViewModel.
 * Ürünlerin listelenmesi, eklenmesi, güncellenmesi ve silinmesi işlemlerini koordine eder.
 */
@HiltViewModel
class ProductsViewModel @Inject constructor (
    var productsRepositoryImpl: ProductsRepositoryImpl,
    var bagProductsRepositoryImpl: BagProductsRepositoryImpl,
) : ViewModel() {

    // Ürün listesini tutan akış (Flow)
    private val _productsState = MutableStateFlow<List<ProductModel>>(emptyList())
    val productsState: StateFlow<List<ProductModel>> = _productsState.asStateFlow()

    // Seçili ürünü tutan akış
    private val _selectedProductState = MutableStateFlow<ProductModel?>(null)
    val selectedProductState: StateFlow<ProductModel?> = _selectedProductState.asStateFlow()

    init {
        getAllProducts()
    }

    /**
     * Tüm ürünleri veritabanından getirir ve akışı günceller.
     */
    fun getAllProducts() {
        viewModelScope.launch {
            productsRepositoryImpl.getAllProducts().collect { list ->
                _productsState.value = list
            }
        }
    }

    /**
     * ID'ye göre ürün getirir.
     */
    fun getProductById(productId: Int) {
        viewModelScope.launch {
            _selectedProductState.value = productsRepositoryImpl.getProductById(productId)
        }
    }

    /**
     * Yeni bir ürün ekler.
     */
    fun addProduct(product: ProductModel) {
        viewModelScope.launch {
            val nextIndex = productsRepositoryImpl.getNextOrderIndex()
            val productWithIndex = product.copy(orderIndex = nextIndex)
            productsRepositoryImpl.addProduct(productWithIndex)
        }
    }

    /**
     * Mevcut bir ürünü günceller.
     */
    fun updateProduct(product: ProductModel) {
        viewModelScope.launch {
            productsRepositoryImpl.updateProduct(product)
        }
    }

    /**
     * Birden fazla ürünü günceller (sıralama vb. için).
     */
    fun updateProducts(products: List<ProductModel>) {
        viewModelScope.launch {
            productsRepositoryImpl.updateProducts(products)
        }
    }

    /**
     * Ürünü siler ve ilişkili resim dosyasını temizler.
     */
    fun deleteProduct(product: ProductModel) {
        viewModelScope.launch {
            // Ürünün resmini sil
            if (product.image.isNotEmpty()) {
                val file = File(product.image)
                if (file.exists()) {
                    file.delete()
                }
            }
            productsRepositoryImpl.deleteProduct(product)
            bagProductsRepositoryImpl.removeProductByName(product.name, product.image)
        }
    }

    /**
     * Ürünler arasında arama yapar.
     */
    fun searchProducts(query: String) {
        viewModelScope.launch {
            productsRepositoryImpl.searchProducts(query).collect { filteredList ->
                _productsState.value = filteredList
            }
        }
    }

    /**
     * Ürünlerin görüntülenme sırasını günceller.
     */
    fun updateProductsOrder(products: List<ProductModel>) {
        viewModelScope.launch {
            productsRepositoryImpl.updateProducts(products)
        }
    }
}