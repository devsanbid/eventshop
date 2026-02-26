package com.example.eventshop.model

data class Booking(
    val id: String = "",
    val userId: String = "",
    val hallId: String = "",
    val hallName: String = "",
    val date: String = "",
    val time: String = "",
    val numberOfHalls: Int = 1,
    val totalPrice: Double = 0.0,
    val createdAt: Long = System.currentTimeMillis()
)
