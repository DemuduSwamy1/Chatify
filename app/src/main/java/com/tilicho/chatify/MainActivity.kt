package com.tilicho.chatify

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tilicho.chatify.navigation.Screen
import com.tilicho.chatify.navigation.SetUpNavGraph
import com.tilicho.chatify.ui.theme.SimpleChatTheme
import com.tilicho.chatify.viewmodel.AuthViewModel
import com.tilicho.chatify.viewmodel.ChatViewModel

class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController
    private lateinit var authViewModel: AuthViewModel
    private lateinit var chatViewModel: ChatViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        val uid = mutableStateOf(String())

        authViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[AuthViewModel::class.java]

        setContent {
            SimpleChatTheme {
                Log.d("setcontent_001", "true")
                val lifecycleOwner = LocalLifecycleOwner.current
                /*val isUserRegistered = UserDataStore(this).getUid().collectAsState(initial = "").value*/

                chatViewModel =
                    ChatViewModel(application = application, lifecycleOwner = lifecycleOwner)
                navController = rememberNavController()
                SetUpNavGraph(
                    context = this@MainActivity,
                    navController = navController,
                    authViewModel = authViewModel,
                    chatViewModel = chatViewModel,
                    lifecycleOwner = lifecycleOwner
                )

                if (UserDataStore(this).getUid()
                        .collectAsState(initial = "").value?.isNotEmpty() == true
                ) {
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