package com.example.eventshop.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventshop.model.Booking
import com.example.eventshop.model.Order
import com.example.eventshop.repository.BookingRepository
import com.example.eventshop.repository.OrderRepository
import com.example.eventshop.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrderViewModel : ViewModel() {

    private val orderRepository = OrderRepository()
    private val bookingRepository = BookingRepository()

    private val _orders = MutableStateFlow<Resource<List<Order>>>(Resource.Loading)
    val orders: StateFlow<Resource<List<Order>>> = _orders

    private val _bookings = MutableStateFlow<Resource<List<Booking>>>(Resource.Loading)
    val bookings: StateFlow<Resource<List<Booking>>> = _bookings

    init {
        loadOrders()
        loadBookings()
    }

    private fun loadOrders() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        viewModelScope.launch {
            orderRepository.getUserOrders(uid).collect { resource ->
                _orders.value = resource
            }
        }
    }

    private fun loadBookings() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        viewModelScope.launch {
            bookingRepository.getUserBookings(uid).collect { resource ->
                _bookings.value = resource
            }
        }
    }
}
