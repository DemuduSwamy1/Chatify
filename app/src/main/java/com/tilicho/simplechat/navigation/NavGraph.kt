package com.tilicho.simplechat.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tilicho.simplechat.screens.ChatsScreen
import com.tilicho.simplechat.screens.IndividualChatScreen
import com.tilicho.simplechat.screens.RegisterScreen
import com.tilicho.simplechat.viewmodel.AuthViewModel
import com.tilicho.simplechat.viewmodel.ChatViewModel

@Composable
fun SetUpNavGraph(
    context: Context,
    navController: NavHostController,
    authViewModel: AuthViewModel,
    lifecycleOwner: LifecycleOwner,
    chatViewModel: ChatViewModel
) {
    NavHost(navController = navController, startDestination = Screen.RegisterScreen.route) {
        composable(route = Screen.RegisterScreen.route) {
            RegisterScreen(navController = navController,
                context = context,
                authViewModel = authViewModel,
                lifecycleOwner = lifecycleOwner,
                chatViewModel = chatViewModel)
        }
        composable(route = Screen.ChatsScreen.route) {
            ChatsScreen(authViewModel = authViewModel,chatViewModel = chatViewModel,navController = navController,lifecycleOwner = lifecycleOwner,)
        }
        composable(route = Screen.IndividualChatScreen.route) {
            IndividualChatScreen(navController = navController, authViewModel = authViewModel, chatViewModel = chatViewModel)
        }
    }
}

