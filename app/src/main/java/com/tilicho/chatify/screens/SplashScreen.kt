package com.tilicho.chatify.screens

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.tilicho.chatify.UserDataStore
import com.tilicho.chatify.navigation.Screen
import com.tilicho.chatify.viewmodel.AuthViewModel
import kotlinx.coroutines.runBlocking

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SplashScreen(context: Context, navController: NavHostController, authViewModel: AuthViewModel) {

    Column(modifier = Modifier.fillMaxSize()) {
        LaunchedEffect(key1 = true) {
            authViewModel.getUidFromPreferences().collect {
                if (it.isNotEmpty()) {
                    navController.navigate(Screen.ChatsScreen.route) {
                        popUpTo(Screen.RegisterScreen.route) {
                            inclusive = true
                        }
                    }
                } else {
                    navController.navigate(Screen.RegisterScreen.route) {
                        popUpTo(Screen.SplashScreen.route) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }
}