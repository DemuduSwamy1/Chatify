package com.tilicho.simplechat.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.tilicho.simplechat.R
import com.tilicho.simplechat.data.User
import com.tilicho.simplechat.navigation.Screen
import com.tilicho.simplechat.viewmodel.AuthViewModel
import com.tilicho.simplechat.viewmodel.ChatViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking

@Composable
fun RegisterScreen(
    navController: NavController,
    context: Context,
    authViewModel: AuthViewModel,
    lifecycleOwner: LifecycleOwner,
    chatViewModel: ChatViewModel
) {
    var name by remember {
        mutableStateOf(String())
    }
    var email by remember {
        mutableStateOf(String())
    }
    var password by remember {
        mutableStateOf(String())
    }
    var confirmPassword by remember {
        mutableStateOf(String())
    }
    var isError by remember {
        mutableStateOf(false)
    }
    var toggle by remember {
        mutableStateOf(false)
    }
    var errorMessage by remember {
        mutableStateOf(String())
    }
    var isRegistrationSuccess by remember {
        mutableStateOf(false)
    }
    val registeredUsers by remember {
        mutableStateOf(chatViewModel.getFriendsList())
    }

    val scope: CoroutineScope = rememberCoroutineScope()
    Scaffold { it ->
        LaunchedEffect(key1 = true) {
            if (toggle) {
                isRegistrationSuccess = authViewModel.getUserRegistrationStatus
            }
        }
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .padding(top = 24.dp, start = 24.dp, end = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize()
            ) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(fontSize = 32.sp, text = stringResource(id = R.string.register))
                Spacer(modifier = Modifier.height(23.dp))

                TitleAndEditTextField(
                    title = stringResource(id = R.string.user_name),
                    placeHolder = stringResource(id = R.string.name_placeholder),
                    textFieldColour = Color.White,
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Sentences
                ) {
                    name = it
                    isError = false
                }
                Spacer(modifier = Modifier.height(25.dp))

                TitleAndEditTextField(
                    title = stringResource(id = R.string.email),
                    placeHolder = stringResource(id = R.string.email_placeholder),
                    textFieldColour = Color.White,
                    keyboardType = KeyboardType.Email
                ) {
                    email = it
                    isError = false
                }

                Spacer(modifier = Modifier.height(25.dp))

                TitleAndEditTextField(
                    title = stringResource(id = R.string.password),
                    placeHolder = stringResource(id = R.string.hide_password),
                    textFieldColour = Color.White,
                    keyboardType = KeyboardType.Password
                ) {
                    password = it
                    isError = false
                }

                Spacer(modifier = Modifier.height(25.dp))

                TitleAndEditTextField(
                    title = stringResource(id = R.string.confirm_password),
                    placeHolder = stringResource(id = R.string.hide_password),
                    textFieldColour = Color.White,
                    keyboardType = KeyboardType.Password,
                    isError = isError,
                    error = errorMessage
                ) {
                    confirmPassword = it
                    isError = false
                }

                Spacer(modifier = Modifier.height(33.dp))

                Button(
                    onClick = {
                        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                            if(isAlreadyRegistered(name,lifecycleOwner,registeredUsers)){
                                errorMessage = "This name is already registered!"
                                isError = true
                            }else{
                                errorMessage = "Please fill all the details."
                                isError = true
                            }
                        } else if (!authViewModel.checkEmailExists(email)) {
                            if (validatePasswordFields(password,
                                    confirmPassword).isNotEmpty()
                            ) {
                                errorMessage = validatePasswordFields(password, confirmPassword)
                                isError = true
                            } else {
                                isError = false
                                errorMessage = ""
                                runBlocking {
                                    authViewModel.register(email,
                                        password,
                                        name,
                                        lifecycleOwner,
                                        scope)
                                    toggle = true
                                }
                            }
                        } else {
                            Toast.makeText(context,
                                "This Email already exists",
                                Toast.LENGTH_LONG).show()
                        }

                        if (isRegistrationSuccess) {
                            navController.navigate(Screen.ChatsScreen.route) {
                                popUpTo(Screen.RegisterScreen.route) {
                                    inclusive = true
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(10.dp))
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF49E2A7))
                ) {
                    Text(text = stringResource(id = R.string.register),
                        color = Color(0xffffffff),
                        fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(30.dp))

                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp), horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.donot_have_account),
                        color = Color.Black,
                        fontSize = 12.sp
                    )
                    Text(text = stringResource(id = R.string.register),
                        color = Color(0xFF49E2A7),
                        fontSize = 12.sp)
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
    isError: Boolean = false,
    error: String = "",
    text: (String) -> Unit,
) {
    Text(text = title, fontSize = 16.sp, color = Color.Black)
    Spacer(modifier = Modifier.height(8.dp))

    var textFieldValue by remember { mutableStateOf(TextFieldValue("")) }

    TextField(
        value = textFieldValue,
        onValueChange = { newText ->
            textFieldValue = newText
            text.invoke(textFieldValue.text)
        },
        modifier = Modifier
            .clip(shape = RoundedCornerShape(10.dp))
            .fillMaxWidth()
            .height(52.dp)
            .background(textFieldColour),
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(Color.Black),
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
        },
        isError = isError,
    )
    if (isError) {
        Text(
            text = error,
            color = MaterialTheme.colors.error,
            style = MaterialTheme.typography.caption,
            modifier = Modifier.padding(start = 16.dp, top = 0.dp)
        )
    }
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

fun validatePasswordFields(password: String, confirmPassword: String): String {
    return if (password.length != confirmPassword.length) {
        "Password length did not match."
    } else if (password != confirmPassword) {
        "password did not match."
    } else {
        ""
    }
}


fun isAlreadyRegistered(
    name: String,
    lifecycleOwner: LifecycleOwner,
    registeredUsers: MutableLiveData<MutableList<User>>
): Boolean {
    var isregistered = false
    var friends = mutableListOf<User>()
    registeredUsers.observe(lifecycleOwner){
        friends = it
    }
    friends.forEach {
        if(it.name == name){
           isregistered = true
        }
    }
    return isregistered
}