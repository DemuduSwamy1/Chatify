package com.tilicho.chatify

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tilicho.chatify.navigation.SetUpNavGraph
import com.tilicho.chatify.ui.theme.SimpleChatTheme
import com.tilicho.chatify.viewmodel.AuthViewModel
import com.tilicho.chatify.viewmodel.ChatViewModel

class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController
    private lateinit var authViewModel: AuthViewModel
    private lateinit var chatViewModel: ChatViewModel

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        authViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[AuthViewModel::class.java]

        setContent {
            SimpleChatTheme {
                val lifecycleOwner = LocalLifecycleOwner.current
                chatViewModel =
                    ChatViewModel(application = application, lifecycleOwner = lifecycleOwner)
                chatViewModel.initViewModel()
                navController = rememberNavController()
                SetUpNavGraph(
                    context = this@MainActivity,
                    navController = navController,
                    authViewModel = authViewModel,
                    lifecycleOwner = lifecycleOwner,
                    chatViewModel = chatViewModel,
                    scope = rememberCoroutineScope()
                )
            }
        }
    }
}