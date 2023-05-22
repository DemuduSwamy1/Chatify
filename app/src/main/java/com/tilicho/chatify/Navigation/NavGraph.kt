package com.tilicho.chatify.navigation

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tilicho.chatify.screens.ChatsScreen
import com.tilicho.chatify.screens.IndividualChatScreen
import com.tilicho.chatify.screens.RegisterScreen
import com.tilicho.chatify.screens.SignInScreen
import com.tilicho.chatify.screens.SplashScreen
import com.tilicho.chatify.viewmodel.AuthViewModel
import com.tilicho.chatify.viewmodel.ChatViewModel
import kotlinx.coroutines.CoroutineScope

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun SetUpNavGraph(
    context: Context,
    navController: NavHostController,
    authViewModel: AuthViewModel,
    lifecycleOwner: LifecycleOwner,
    chatViewModel: ChatViewModel,
    scope: CoroutineScope
) {
    NavHost(navController = navController, startDestination = Screen.SplashScreen.route) {
        composable(route = Screen.RegisterScreen.route) {
            RegisterScreen(
                navController = navController,
                context = context,
                authViewModel = authViewModel,
                lifecycleOwner = lifecycleOwner,
                chatViewModel = chatViewModel,
                scope = scope
            )
        }
        composable(route = Screen.ChatsScreen.route) {
            ChatsScreen(
                authViewModel = authViewModel,
                chatViewModel = chatViewModel,
                navController = navController,
                lifecycleOwner = lifecycleOwner,
                scope = scope
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
        composable(route = Screen.SplashScreen.route) {
            SplashScreen(
                context = context,
                authViewModel = authViewModel,
                navController = navController,
            )
        }
        composable(route = Screen.SignInScreen.route) {
            SignInScreen(authViewModel = authViewModel,
                scope = scope,
                navController = navController,
                chatViewModel = chatViewModel)
        }
    }
}

