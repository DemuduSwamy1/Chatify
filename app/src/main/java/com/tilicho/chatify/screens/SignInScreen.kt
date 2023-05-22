package com.tilicho.chatify.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tilicho.chatify.R
import com.tilicho.chatify.navigation.Screen
import com.tilicho.chatify.viewmodel.AuthViewModel
import com.tilicho.chatify.viewmodel.ChatViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun SignInScreen(
    authViewModel: AuthViewModel,
    scope: CoroutineScope,
    navController: NavController,
    chatViewModel: ChatViewModel
) {
    var userName by remember {
        mutableStateOf(String())
    }
    var password by remember {
        mutableStateOf(String())
    }
    var isError by remember {
        mutableStateOf(false)
    }
    var errorMessage by remember {
        mutableStateOf(String())
    }
    Scaffold {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .padding(top = 24.dp, start = 24.dp, end = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Spacer(modifier = Modifier.height(41.dp))
                Text(fontSize = 32.sp, text = stringResource(id = R.string.login))
                Spacer(modifier = Modifier.height(53.dp))

                TitleAndEditTextField(
                    title = stringResource(id = R.string.user_name),
                    placeHolder = stringResource(id = R.string.email_placeholder),
                    textFieldColour = Color.White,
                    keyboardType = KeyboardType.Email
                ) { value ->
                    userName = value
                    isError = false
                }

                Spacer(modifier = Modifier.height(25.dp))

                TitleAndEditTextField(
                    title = stringResource(id = R.string.password),
                    placeHolder = stringResource(id = R.string.hide_password),
                    textFieldColour = Color.White,
                    keyboardType = KeyboardType.Password,
                    error = errorMessage,
                    isError = isError
                ) { value ->
                    password = value
                    isError = false
                }

                Spacer(modifier = Modifier.height(60.dp))
                Button(
                    onClick = {
                        if (userName.isNotEmpty() && password.isNotEmpty() && isValidEmail(
                                emailString = userName)) {
                            authViewModel.login(userName,
                                password,
                                scope = scope,
                                loggedIn = { loggedIn ->
                                    if (loggedIn) {
                                        chatViewModel.initViewModel()
                                        navController.navigate(Screen.ChatsScreen.route) {
                                            popUpTo(Screen.SignInScreen.route) {
                                                inclusive = true
                                            }
                                        }
                                    }
                                })
                        } else {
                            errorMessage = "Please fill all the details."
                            isError = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.green))
                ) {
                    Text(text = "Login", color = Color(0xffffffff), fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.height(35.dp))

                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp), horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.do_not_have_account),
                        color = colorResource(id = R.color.grey),
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.spacing_10)))
                    Text(text = stringResource(id = R.string.register),
                        color = colorResource(id = R.color.green),
                        fontSize = 16.sp,
                        modifier = Modifier.clickable {
                            navController.navigate(Screen.RegisterScreen.route) {
                                popUpTo(Screen.SignInScreen.route) {
                                    inclusive = true
                                }
                            }
                        })
                }
            }
        }
    }
}