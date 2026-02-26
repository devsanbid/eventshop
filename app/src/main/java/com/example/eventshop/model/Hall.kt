package com.example.eventshop.model

data class Hall(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val pricing: Double = 0.0,
    val availability: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
