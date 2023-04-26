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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tilicho.simplechat.navigation.SetUpNavGraph
import com.tilicho.simplechat.ui.theme.SimpleChatTheme
import com.tilicho.simplechat.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController
    private lateinit var authViewModel: AuthViewModel

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        super.onCreate(savedInstanceState)
        authViewModel = ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application))[AuthViewModel::class.java]
        setContent {
            SimpleChatTheme {
                var uid by remember {
                    mutableStateOf(String())
                }
                navController = rememberNavController()

                LaunchedEffect(key1 = true) {
                    authViewModel.getUidFromPreferences().collect {
                        uid = it
                    }.toString()
                }
                if (uid.isNotEmpty()) {
                    // TODO("Navigate directly to the chats screen")
                } else {
                    SetUpNavGraph(context = this@MainActivity,
                        navController = navController,
                        authViewModel)
                }
            }
        }
    }
}