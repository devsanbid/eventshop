package com.example.eventshop.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.eventshop.view.admin.AdminMainScreen
import com.example.eventshop.view.admin.AddBookingEventScreen
import com.example.eventshop.view.admin.AddProductScreen
import com.example.eventshop.view.admin.UsersListScreen
import com.example.eventshop.view.auth.LoginScreen
import com.example.eventshop.view.auth.RegisterScreen
import com.example.eventshop.view.auth.SplashScreen
import com.example.eventshop.view.user.ProductDetailScreen
import com.example.eventshop.view.user.UserMainScreen
import com.example.eventshop.viewmodel.AdminViewModel
import com.example.eventshop.viewmodel.AuthViewModel
import com.example.eventshop.viewmodel.BookingViewModel
import com.example.eventshop.viewmodel.OrderViewModel
import com.example.eventshop.viewmodel.ProductViewModel
import com.example.eventshop.viewmodel.UserViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    NavHost(navController = navController, startDestination = Screen.Splash.route) {

        composable(Screen.Splash.route) {
            SplashScreen(
                authViewModel = authViewModel,
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToUser = {
                    navController.navigate(Screen.UserMain.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToAdmin = {
                    navController.navigate(Screen.AdminMain.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                authViewModel = authViewModel,
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onLoginSuccess = { user ->
                    val destination = if (user.role == "admin") {
                        Screen.AdminMain.route
                    } else {
                        Screen.UserMain.route
                    }
                    navController.navigate(destination) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                authViewModel = authViewModel,
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onRegisterSuccess = {
                    navController.navigate(Screen.UserMain.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.UserMain.route) {
            val productViewModel: ProductViewModel = viewModel()
            val bookingViewModel: BookingViewModel = viewModel()
            val orderViewModel: OrderViewModel = viewModel()
            val userViewModel: UserViewModel = viewModel()
            UserMainScreen(
                authViewModel = authViewModel,
                productViewModel = productViewModel,
                bookingViewModel = bookingViewModel,
                orderViewModel = orderViewModel,
                userViewModel = userViewModel,
                onProductClick = { productId ->
                    navController.navigate(Screen.ProductDetail.createRoute(productId))
                },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.ProductDetail.route) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            val productViewModel: ProductViewModel = viewModel()
            ProductDetailScreen(
                productId = productId,
                productViewModel = productViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.AdminMain.route) {
            val adminViewModel: AdminViewModel = viewModel()
            AdminMainScreen(
                adminViewModel = adminViewModel,
                onNavigateToUsers = {
                    navController.navigate(Screen.AdminUsersList.route)
                },
                onNavigateToAddProduct = {
                    navController.navigate(Screen.AdminAddProduct.route)
                },
                onNavigateToAddHall = {
                    navController.navigate(Screen.AdminAddHall.route)
                },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.AdminUsersList.route) {
            val adminViewModel: AdminViewModel = viewModel()
            UsersListScreen(
                adminViewModel = adminViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.AdminAddProduct.route) {
            val adminViewModel: AdminViewModel = viewModel()
            AddProductScreen(
                adminViewModel = adminViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.AdminAddHall.route) {
            val adminViewModel: AdminViewModel = viewModel()
            AddBookingEventScreen(
                adminViewModel = adminViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
