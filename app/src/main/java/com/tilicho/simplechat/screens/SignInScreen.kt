package com.tilicho.simplechat.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tilicho.simplechat.viewmodel.AuthViewModel

@Composable
fun SignInScreen(context: Context, authViewModel: AuthViewModel) {
    var userName by remember {
        mutableStateOf(String())
    }
    var password by remember {
        mutableStateOf(String())
    }
    Scaffold {
        Column(
            modifier = Modifier
                .padding(it)
                .background(Color(0xFF121212))
                .fillMaxSize()
                .padding(top = 29.dp, start = 24.dp, end = 24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.clickable(onClick = {

                })
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Spacer(modifier = Modifier.height(41.dp))
                Text(text = "SignIn", fontSize = 32.sp, color = Color(0xdeffffff))
                Spacer(modifier = Modifier.height(53.dp))

                TitleAndEditTextField(
                    title = "Email",
                    placeHolder = "Enter your email",
                    textFieldColour = Color(0xff1d1d1d),
                    keyboardType = KeyboardType.Text
                ) {
                    userName = it
                }

                Spacer(modifier = Modifier.height(25.dp))

                TitleAndEditTextField(
                    title = "Password",
                    placeHolder = "• • • • • • • • • • ",
                    textFieldColour = Color(0xff1d1d1d),
                    keyboardType = KeyboardType.Password
                ) {
                    password = it
                }

                Spacer(modifier = Modifier.height(60.dp))
                Button(
                    onClick = {
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xff8875ff))
                ) {
                    Text(text = "Login", color = Color(0xffffffff), fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.height(35.dp))

                /*CustomDivider()

                Spacer(modifier = Modifier.height(35.dp))

                LoginTypes()*/

                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp), horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Don’t have an account?",
                        color = Color(0xFF979797),
                        fontSize = 12.sp
                    )
                    Text(text = "Register", color = Color(0xdeffffff), fontSize = 12.sp)
                }
            }
        }
    }
}