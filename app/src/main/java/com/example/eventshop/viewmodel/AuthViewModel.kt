package com.example.eventshop.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventshop.model.User
import com.example.eventshop.repository.AuthRepository
import com.example.eventshop.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val repository = AuthRepository()

    private val _loginState = MutableStateFlow<Resource<User>?>(null)
    val loginState: StateFlow<Resource<User>?> = _loginState

    private val _registerState = MutableStateFlow<Resource<User>?>(null)
    val registerState: StateFlow<Resource<User>?> = _registerState

    private val _currentUser = MutableStateFlow<Resource<User>?>(null)
    val currentUser: StateFlow<Resource<User>?> = _currentUser

    val isLoggedIn get() = repository.currentUser != null

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = Resource.Loading
            _loginState.value = repository.login(email, password)
        }
    }

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _registerState.value = Resource.Loading
            _registerState.value = repository.register(name, email, password)
        }
    }

    fun fetchCurrentUser() {
        viewModelScope.launch {
            _currentUser.value = Resource.Loading
            _currentUser.value = repository.getCurrentUserData()
        }
    }

    fun logout() {
        repository.logout()
        _loginState.value = null
        _registerState.value = null
        _currentUser.value = null
    }

    fun resetLoginState() {
        _loginState.value = null
    }

    fun resetRegisterState() {
        _registerState.value = null
    }
}
