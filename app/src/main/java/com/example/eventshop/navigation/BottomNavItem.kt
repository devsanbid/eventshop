package com.example.eventshop.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    data object Products : BottomNavItem("products", "Products", Icons.Default.ShoppingBag)
    data object Booking : BottomNavItem("booking", "Booking", Icons.Default.CalendarMonth)
    data object Orders : BottomNavItem("orders", "Orders", Icons.Default.Receipt)
    data object Settings : BottomNavItem("settings", "Settings", Icons.Default.Settings)
}
