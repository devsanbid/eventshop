package com.example.eventshop.utils

object Constants {
    // Cloudinary configuration - replace with your own values
    const val CLOUDINARY_CLOUD_NAME = "dktnwa5xl"
    const val CLOUDINARY_UPLOAD_PRESET = "eventshop"
    const val CLOUDINARY_UPLOAD_URL = "https://api.cloudinary.com/v1_1/$CLOUDINARY_CLOUD_NAME/image/upload"

    // Firestore collection names
    const val USERS_COLLECTION = "users"
    const val PRODUCTS_COLLECTION = "products"
    const val HALLS_COLLECTION = "halls"
    const val BOOKINGS_COLLECTION = "bookings"
    const val ORDERS_COLLECTION = "orders"

    // User roles
    const val ROLE_USER = "user"
    const val ROLE_ADMIN = "admin"
}
