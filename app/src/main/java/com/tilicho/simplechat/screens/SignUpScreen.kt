package com.tilicho.simplechat.screens

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tilicho.simplechat.Navigation.Screen

@Composable
fun SignUpScreen(navController: NavController, context: Context) {
    var email by remember {
        mutableStateOf(String())
    }
    var password by remember {
        mutableStateOf(String())
    }
    val confirmPassword by remember {
        mutableStateOf(String())
    }
    Scaffold {
        Column(
            modifier = Modifier
                .padding(it)
                .background(Color(0xFF121212))
                .fillMaxSize()
                .padding(top = 24.dp, start = 24.dp, end = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "SignUp", fontSize = 32.sp, color = Color(0xdeffffff))
                Spacer(modifier = Modifier.height(23.dp))

                TitleAndEditTextField(
                    title = "Email",
                    placeHolder = "Enter your email",
                    textFieldColour = Color(0xff1d1d1d),
                    keyboardType = KeyboardType.Text
                ) {
                    email = it
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

                Spacer(modifier = Modifier.height(25.dp))

                TitleAndEditTextField(
                    title = "Confirm Password",
                    placeHolder = "• • • • • • • • • • ",
                    textFieldColour = Color(0xff1d1d1d), keyboardType = KeyboardType.Password
                ) {
                    val value = it
                }

                Spacer(modifier = Modifier.height(33.dp))

                Button(
                    onClick = {
                        navController.navigate(Screen.SignInScreen.route)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xff8875ff))
                ) {
                    Text(text = "Register", color = Color(0xffffffff), fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(30.dp))

                /*CustomDivider()

                Spacer(modifier = Modifier.height(30.dp))

                LoginTypes()
*/
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

@Composable
fun TitleAndEditTextField(
    title: String,
    placeHolder: String,
    textFieldColour: Color,
    keyboardType: KeyboardType,
    capitalization: KeyboardCapitalization = KeyboardCapitalization.None,
    text: (String) -> Unit
) {
    Text(text = title, fontSize = 16.sp, color = Color(0xdeffffff))
    Spacer(modifier = Modifier.height(8.dp))

    var textFieldValue by remember { mutableStateOf(TextFieldValue("")) }

    TextField(value = textFieldValue, onValueChange = { newText ->
        textFieldValue = newText
        text.invoke(textFieldValue.text)
    }, modifier = Modifier
        .fillMaxWidth()
        .height(52.dp)
        .background(textFieldColour)
        .border(1.dp, color = Color(0xFF979797)),
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(Color(0xffffffff)),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            capitalization = capitalization
        ),
        placeholder = {
            Text(
                text = placeHolder,
                color = Color(0xFF535353),
                fontSize = 16.sp
            )
        })
}

@Composable
fun CustomDivider() {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .height(1.dp)
                .background(Color(0xFF979797))
                .weight(1f),

            )
        Spacer(modifier = Modifier.width(3.dp))
        Text(text = "or", color = Color(0xFF979797), fontSize = 16.sp)
        Spacer(modifier = Modifier.width(3.dp))
        Box(
            modifier = Modifier
                .height(1.dp)
                .background(Color(0xFF979797))
                .weight(1f)
        )
    }
}

@Composable
fun LoginTypes() {
    OutlinedButton(
        onClick = { /*TODO*/ },
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        border = BorderStroke(1.5.dp, Color(0xff8875ff)),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xff121212))
    ) {
        Image(
            imageVector = Icons.Default.Person,
            contentDescription = null,
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = "Login with Google", color = Color(0xdeffffff), fontSize = 16.sp)
    }
    Spacer(modifier = Modifier.height(20.dp))
    OutlinedButton(
        onClick = { /*TODO*/ },
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        border = BorderStroke(1.5.dp, Color(0xff8875ff)),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xff121212))
    ) {
        Row {
            Image(
                imageVector = Icons.Default.Grain,
                contentDescription = null,
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "Login with Apple", color = Color(0xdeffffff), fontSize = 16.sp)
        }
    }
}