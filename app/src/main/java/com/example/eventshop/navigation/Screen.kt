package com.example.eventshop.navigation

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object UserMain : Screen("user_main")
    data object AdminMain : Screen("admin_main")
    data object ProductDetail : Screen("product_detail/{productId}") {
        fun createRoute(productId: String) = "product_detail/$productId"
    }
    data object AdminUsersList : Screen("admin_users_list")
    data object AdminAddProduct : Screen("admin_add_product")
    data object AdminAddHall : Screen("admin_add_hall")
}
