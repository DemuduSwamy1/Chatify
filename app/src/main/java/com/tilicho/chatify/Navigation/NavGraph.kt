package com.tilicho.chatify.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tilicho.chatify.screens.ChatsScreen
import com.tilicho.chatify.screens.IndividualChatScreen
import com.tilicho.chatify.screens.RegisterScreen
import com.tilicho.chatify.viewmodel.AuthViewModel
import com.tilicho.chatify.viewmodel.ChatViewModel

@Composable
fun SetUpNavGraph(
    context: Context,
    navController: NavHostController,
    authViewModel: AuthViewModel,
    lifecycleOwner: LifecycleOwner,
    chatViewModel: ChatViewModel
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    NavHost(navController = navController, startDestination = Screen.RegisterScreen.route) {
        composable(route = Screen.RegisterScreen.route) {
            RegisterScreen(
                navController = navController,
                context = context,
                authViewModel = authViewModel,
                lifecycleOwner = lifecycleOwner,
                chatViewModel = chatViewModel
            )
        }
        composable(route = Screen.ChatsScreen.route) {
            ChatsScreen(
                authViewModel = authViewModel,
                chatViewModel = chatViewModel,
                navController = navController,
                lifecycleOwner = lifecycleOwner,
            )
        }
        composable(route = Screen.IndividualChatScreen.route) {
            IndividualChatScreen(
                lifecycleOwner = lifecycleOwner,
                navController = navController,
                authViewModel = authViewModel,
                chatViewModel = chatViewModel
            )
        }
    }
}

