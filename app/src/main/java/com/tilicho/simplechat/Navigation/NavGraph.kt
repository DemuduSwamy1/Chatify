package com.tilicho.simplechat.Navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tilicho.simplechat.screens.SignInScreen
import com.tilicho.simplechat.screens.SignUpScreen

@Composable
fun SetUpNavGraph(context: Context, navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.SignUpScreen.route) {
        composable(route = Screen.SignUpScreen.route) {
            SignUpScreen(navController = navController, context = context)
        }
        composable(route = Screen.SignInScreen.route) {
            SignInScreen(context = context)
        }
    }
}