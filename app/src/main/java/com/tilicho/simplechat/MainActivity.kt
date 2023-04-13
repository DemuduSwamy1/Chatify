package com.tilicho.simplechat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tilicho.simplechat.Navigation.SetUpNavGraph
import com.tilicho.simplechat.ui.theme.SimpleChatTheme

class MainActivity : ComponentActivity() {
    lateinit var navController: NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimpleChatTheme {
                navController = rememberNavController()
                SetUpNavGraph(context = this@MainActivity, navController = navController)
            }
        }
    }
}