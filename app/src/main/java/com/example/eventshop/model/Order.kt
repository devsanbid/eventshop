package com.example.eventshop.model

data class Order(
    val id: String = "",
    val userId: String = "",
    val productId: String = "",
    val productName: String = "",
    val productImage: String = "",
    val price: Double = 0.0,
    val quantity: Int = 1,
    val createdAt: Long = System.currentTimeMillis()
)
