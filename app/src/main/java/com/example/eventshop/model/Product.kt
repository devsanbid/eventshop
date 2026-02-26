package com.example.eventshop.model

data class Product(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val imageUrl: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
