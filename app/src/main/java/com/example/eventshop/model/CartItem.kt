package com.example.eventshop.model

data class CartItem(
    val id: String = "",
    val productId: String = "",
    val productName: String = "",
    val productImage: String = "",
    val price: Double = 0.0,
    val quantity: Int = 1
)
