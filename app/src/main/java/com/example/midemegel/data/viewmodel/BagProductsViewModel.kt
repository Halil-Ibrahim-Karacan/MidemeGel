package com.example.midemegel.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.midemegel.data.model.BagProductModel
import com.example.midemegel.data.repository.BagProductsRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Sepet ürünleriyle ilgili işlemleri yöneten ViewModel.
 * @param productsRepositoryImpl Ürün verilerine erişim sağlayan repository.
 */
@HiltViewModel
class BagProductsViewModel @Inject constructor (var productsRepositoryImpl: BagProductsRepositoryImpl) : ViewModel() {

    // Sepetteki ürünlerin listesini tutan ve dışarıya sadece okunabilir bir StateFlow olarak sunan yapı.
    private val _productsState = MutableStateFlow<List<BagProductModel>>(emptyList())
    val productsState: StateFlow<List<BagProductModel>> = _productsState.asStateFlow()

    // Seçilen tek bir ürünü tutan ve dışarıya sadece okunabilir bir StateFlow olarak sunan yapı.
    private val _selectedProductState = MutableStateFlow<BagProductModel?>(null)
    val selectedProductState: StateFlow<BagProductModel?> = _selectedProductState.asStateFlow()

    init {
        getAllProducts()
    }

    /**
     * Veritabanındaki tüm ürünleri getirir ve _productsState'i günceller.
     */
    fun getAllProducts() {
        viewModelScope.launch {
            productsRepositoryImpl.getAllProducts().collect { list ->
                _productsState.value = list
            }
        }
    }

    /**
     * Belirtilen ID'ye sahip ürünü getirir ve _selectedProductState'i günceller.
     * @param productId Getirilecek ürünün ID'si.
     */
    fun getProductById(productId: Int) {
        viewModelScope.launch {
            _selectedProductState.value = productsRepositoryImpl.getProductById(productId)
        }
    }

    /**
     * Sepete yeni bir ürün ekler veya mevcut bir ürünün miktarını günceller.
     * Eğer ürün sepette zaten varsa, miktarı artırılır/azaltılır.
     * Miktar sıfıra düşerse, ürün sepetten kaldırılır.
     * @param product Eklenecek veya güncellenecek ürün.
     * @param countChange Miktardaki değişiklik (pozitif veya negatif).
     */
    fun addOrUpdateProduct(product: BagProductModel, countChange: Int) {
        viewModelScope.launch {
            val existingProduct = productsState.value.find { it.name == product.name }
            if (existingProduct != null) {
                val newCount = existingProduct.count + countChange
                if (newCount > 0) {
                    updateProduct(existingProduct.copy(count = newCount))
                } else {
                    deleteProduct(existingProduct)
                }
            } else if (countChange > 0) {
                addProduct(product.copy(id = 0, count = countChange))
            }
        }
    }

    /**
     * Veritabanına yeni bir ürün ekler.
     * @param product Eklenecek ürün.
     */
    fun addProduct(product: BagProductModel) {
        viewModelScope.launch {
            productsRepositoryImpl.addProduct(product)
        }
    }

    /**
     * Veritabanındaki bir ürünü günceller.
     * @param product Güncellenecek ürün.
     */
    fun updateProduct(product: BagProductModel) {
        viewModelScope.launch {
            productsRepositoryImpl.updateProduct(product)
        }
    }

    /**
     * Veritabanından bir ürünü siler.
     * @param product Silinecek ürün.
     */
    fun deleteProduct(product : BagProductModel) {
        viewModelScope.launch {
            productsRepositoryImpl.deleteProduct(product)
        }
    }

    /**
     * Veritabanından bir ürünü ismine göre siler.
     * @param productId Silinecek ürünün ismi.
     */
    fun removeProduct(productId: String, image: String) {
        viewModelScope.launch {
            productsRepositoryImpl.removeProductByName(productId, image)
        }
    }

    /**
     * Ürünleri isme göre arar ve _productsState'i günceller.
     * @param query Arama sorgusu.
     */
    fun searchProducts(query: String) {
        viewModelScope.launch {
            productsRepositoryImpl.searchProducts(query).collect { filteredList ->
                _productsState.value = filteredList
            }
        }
    }

    /**
     * Sepetteki tüm ürünleri temizler.
     */
    fun clearBag() {
        viewModelScope.launch {
            productsRepositoryImpl.clearBag()
        }
    }
}