package com.example.eventshop.view.user

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.eventshop.navigation.BottomNavItem
import com.example.eventshop.viewmodel.AuthViewModel
import com.example.eventshop.viewmodel.BookingViewModel
import com.example.eventshop.viewmodel.OrderViewModel
import com.example.eventshop.viewmodel.ProductViewModel
import com.example.eventshop.viewmodel.UserViewModel

@Composable
fun UserMainScreen(
    authViewModel: AuthViewModel,
    productViewModel: ProductViewModel,
    bookingViewModel: BookingViewModel,
    orderViewModel: OrderViewModel,
    userViewModel: UserViewModel,
    onProductClick: (String) -> Unit,
    onLogout: () -> Unit
) {
    val bottomNavController = rememberNavController()
    val navItems = listOf(
        BottomNavItem.Products,
        BottomNavItem.Booking,
        BottomNavItem.Orders,
        BottomNavItem.Settings
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                navItems.forEach { item ->
                    val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title
                            )
                        },
                        label = {
                            Text(
                                text = item.title,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        selected = selected,
                        onClick = {
                            bottomNavController.navigate(item.route) {
                                popUpTo(bottomNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = bottomNavController,
            startDestination = BottomNavItem.Products.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Products.route) {
                ProductListScreen(
                    productViewModel = productViewModel,
                    onProductClick = onProductClick
                )
            }
            composable(BottomNavItem.Booking.route) {
                BookingScreen(bookingViewModel = bookingViewModel)
            }
            composable(BottomNavItem.Orders.route) {
                OrdersScreen(orderViewModel = orderViewModel)
            }
            composable(BottomNavItem.Settings.route) {
                SettingsScreen(
                    authViewModel = authViewModel,
                    userViewModel = userViewModel,
                    onLogout = onLogout
                )
            }
        }
    }
}
