package com.tilicho.simplechat

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tilicho.simplechat.navigation.Screen
import com.tilicho.simplechat.navigation.SetUpNavGraph
import com.tilicho.simplechat.ui.theme.SimpleChatTheme
import com.tilicho.simplechat.viewmodel.AuthViewModel
import com.tilicho.simplechat.viewmodel.ChatViewModel

class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController
    private lateinit var authViewModel: AuthViewModel
    private lateinit var chatViewModel: ChatViewModel

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        chatViewModel = ChatViewModel(application)
        authViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[AuthViewModel::class.java]
        super.onCreate(savedInstanceState)
        setContent {
            SimpleChatTheme {
                val lifecycleOwner = LocalLifecycleOwner.current
                var uid by remember {
                    mutableStateOf(String())
                }
                navController = rememberNavController()
                SetUpNavGraph(
                    context = this@MainActivity,
                    navController = navController,
                    authViewModel = authViewModel,
                    chatViewModel = chatViewModel,
                    lifecycleOwner = lifecycleOwner
                )

                LaunchedEffect(key1 = true) {
                    authViewModel.getUidFromPreferences().collect {
                        uid = it
                    }.toString()
                }
                if (uid.isNotEmpty()) {
                    navController.navigate(Screen.ChatsScreen.route) {
                        popUpTo(Screen.RegisterScreen.route) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }
}