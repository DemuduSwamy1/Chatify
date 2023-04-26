package com.tilicho.simplechat.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tilicho.simplechat.screens.ChatsScreen
import com.tilicho.simplechat.screens.IndividualChatScreen
import com.tilicho.simplechat.screens.RegisterScreen

@Composable
fun SetUpNavGraph(context: Context, navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.RegisterScreen.route) {
        composable(route = Screen.RegisterScreen.route) {
            RegisterScreen(navController = navController, context = context)
        }
        composable(route = Screen.ChatsScreen.route) {
            ChatsScreen(navController = navController)
        }
        composable(route = Screen.IndividualChatScreen.route) {
            IndividualChatScreen(navController = navController)
        }
    }
}

