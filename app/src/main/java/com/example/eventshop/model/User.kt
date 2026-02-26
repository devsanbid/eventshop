package com.example.eventshop.model

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val role: String = "user" // "user" or "admin"
)
