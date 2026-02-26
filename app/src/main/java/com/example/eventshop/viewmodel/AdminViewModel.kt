package com.example.eventshop.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventshop.model.Hall
import com.example.eventshop.model.Product
import com.example.eventshop.model.User
import com.example.eventshop.repository.BookingRepository
import com.example.eventshop.repository.ProductRepository
import com.example.eventshop.repository.UserRepository
import com.example.eventshop.utils.CloudinaryUploader
import com.example.eventshop.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdminViewModel : ViewModel() {

    private val userRepository = UserRepository()
    private val productRepository = ProductRepository()
    private val bookingRepository = BookingRepository()

    private val _users = MutableStateFlow<Resource<List<User>>>(Resource.Loading)
    val users: StateFlow<Resource<List<User>>> = _users

    private val _addProductState = MutableStateFlow<Resource<Unit>?>(null)
    val addProductState: StateFlow<Resource<Unit>?> = _addProductState

    private val _addHallState = MutableStateFlow<Resource<Unit>?>(null)
    val addHallState: StateFlow<Resource<Unit>?> = _addHallState

    private val _uploadState = MutableStateFlow<Resource<String>?>(null)
    val uploadState: StateFlow<Resource<String>?> = _uploadState

    init {
        loadUsers()
    }

    private fun loadUsers() {
        viewModelScope.launch {
            userRepository.getAllUsers().collect { resource ->
                _users.value = resource
            }
        }
    }

    fun uploadImageAndAddProduct(
        context: Context,
        imageUri: Uri,
        name: String,
        description: String,
        price: Double
    ) {
        viewModelScope.launch {
            _addProductState.value = Resource.Loading
            when (val uploadResult = CloudinaryUploader.uploadImage(context, imageUri)) {
                is Resource.Success -> {
                    val product = Product(
                        name = name,
                        description = description,
                        price = price,
                        imageUrl = uploadResult.data
                    )
                    _addProductState.value = productRepository.addProduct(product)
                }
                is Resource.Error -> {
                    _addProductState.value = Resource.Error(uploadResult.message)
                }
                is Resource.Loading -> {}
            }
        }
    }

    fun addHall(name: String, description: String, pricing: Double, availability: String) {
        viewModelScope.launch {
            _addHallState.value = Resource.Loading
            val hall = Hall(
                name = name,
                description = description,
                pricing = pricing,
                availability = availability
            )
            _addHallState.value = bookingRepository.addHall(hall)
        }
    }

    fun resetAddProductState() {
        _addProductState.value = null
    }

    fun resetAddHallState() {
        _addHallState.value = null
    }
}
