package com.example.eventshop.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventshop.repository.AuthRepository
import com.example.eventshop.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    private val authRepository = AuthRepository()

    private val _updateNameState = MutableStateFlow<Resource<Unit>?>(null)
    val updateNameState: StateFlow<Resource<Unit>?> = _updateNameState

    private val _changePasswordState = MutableStateFlow<Resource<Unit>?>(null)
    val changePasswordState: StateFlow<Resource<Unit>?> = _changePasswordState

    fun updateName(newName: String) {
        viewModelScope.launch {
            _updateNameState.value = Resource.Loading
            _updateNameState.value = authRepository.updateName(newName)
        }
    }

    fun changePassword(currentPassword: String, newPassword: String) {
        viewModelScope.launch {
            _changePasswordState.value = Resource.Loading
            _changePasswordState.value = authRepository.changePassword(currentPassword, newPassword)
        }
    }

    fun resetUpdateNameState() {
        _updateNameState.value = null
    }

    fun resetChangePasswordState() {
        _changePasswordState.value = null
    }
}
