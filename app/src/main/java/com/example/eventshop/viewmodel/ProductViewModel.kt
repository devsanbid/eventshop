package com.example.eventshop.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventshop.model.Order
import com.example.eventshop.model.Product
import com.example.eventshop.repository.OrderRepository
import com.example.eventshop.repository.ProductRepository
import com.example.eventshop.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {

    private val productRepository = ProductRepository()
    private val orderRepository = OrderRepository()

    private val _products = MutableStateFlow<Resource<List<Product>>>(Resource.Loading)
    val products: StateFlow<Resource<List<Product>>> = _products

    private val _selectedProduct = MutableStateFlow<Resource<Product>?>(null)
    val selectedProduct: StateFlow<Resource<Product>?> = _selectedProduct

    private val _buyState = MutableStateFlow<Resource<Unit>?>(null)
    val buyState: StateFlow<Resource<Unit>?> = _buyState

    init {
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            productRepository.getProducts().collect { resource ->
                _products.value = resource
            }
        }
    }

    fun getProductById(productId: String) {
        viewModelScope.launch {
            _selectedProduct.value = Resource.Loading
            _selectedProduct.value = productRepository.getProductById(productId)
        }
    }

    fun buyProduct(product: Product, quantity: Int = 1) {
        viewModelScope.launch {
            _buyState.value = Resource.Loading
            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: run {
                _buyState.value = Resource.Error("Not logged in")
                return@launch
            }
            val order = Order(
                userId = uid,
                productId = product.id,
                productName = product.name,
                productImage = product.imageUrl,
                price = product.price,
                quantity = quantity
            )
            _buyState.value = orderRepository.placeOrder(order)
        }
    }

    fun resetBuyState() {
        _buyState.value = null
    }
}
