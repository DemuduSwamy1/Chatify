package com.tilicho.simplechat.Navigation

sealed class Screen(val route: String) {
    object SignUpScreen : Screen(route = "sign_up_screen")
    object SignInScreen : Screen(route = "sign_in_screen")
}
