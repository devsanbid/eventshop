package com.example.eventshop.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventshop.model.Booking
import com.example.eventshop.model.Hall
import com.example.eventshop.repository.BookingRepository
import com.example.eventshop.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BookingViewModel : ViewModel() {

    private val repository = BookingRepository()

    private val _halls = MutableStateFlow<Resource<List<Hall>>>(Resource.Loading)
    val halls: StateFlow<Resource<List<Hall>>> = _halls

    private val _bookingState = MutableStateFlow<Resource<Unit>?>(null)
    val bookingState: StateFlow<Resource<Unit>?> = _bookingState

    private val _userBookings = MutableStateFlow<Resource<List<Booking>>>(Resource.Loading)
    val userBookings: StateFlow<Resource<List<Booking>>> = _userBookings

    init {
        loadHalls()
    }

    private fun loadHalls() {
        viewModelScope.launch {
            repository.getHalls().collect { resource ->
                _halls.value = resource
            }
        }
    }

    fun loadUserBookings() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        viewModelScope.launch {
            repository.getUserBookings(uid).collect { resource ->
                _userBookings.value = resource
            }
        }
    }

    fun submitBooking(hall: Hall, date: String, time: String, numberOfHalls: Int) {
        viewModelScope.launch {
            _bookingState.value = Resource.Loading
            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: run {
                _bookingState.value = Resource.Error("Not logged in")
                return@launch
            }
            val booking = Booking(
                userId = uid,
                hallId = hall.id,
                hallName = hall.name,
                date = date,
                time = time,
                numberOfHalls = numberOfHalls,
                totalPrice = hall.pricing * numberOfHalls
            )
            _bookingState.value = repository.addBooking(booking)
        }
    }

    fun resetBookingState() {
        _bookingState.value = null
    }
}
