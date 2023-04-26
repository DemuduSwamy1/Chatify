package com.tilicho.simplechat.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.tilicho.simplechat.R
import com.tilicho.simplechat.navigation.Screen
import com.tilicho.simplechat.viewmodel.AuthViewModel

@Composable
fun IndividualChatScreen(
    profileImage: Int = 0,
    name: String = "",
    messages: List<String> = mutableListOf(),
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    Scaffold(modifier = Modifier.padding(5.dp), topBar = {
        Column() {
            Row(
                verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                    .padding(top = 5.dp)
                    .fillMaxWidth()
            ) {
                Image(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null, modifier = Modifier.clickable {
                        navController.navigate(Screen.ChatsScreen.route){
                            popUpTo(Screen.IndividualChatScreen.route){
                                inclusive = true
                            }
                        }
                    }
                )
                Spacer(modifier = Modifier.width(10.dp))
                Image(
                    imageVector = Icons.Default.Grain,
                    contentDescription = null,
                    modifier = Modifier
                        .border(shape = CircleShape, width = 1.dp, color = Color.Green)
                        .size(50.dp)
                )
                Spacer(modifier = Modifier.width(15.dp))
                Text(text = "Bahubali", fontSize = 17.sp, fontWeight = FontWeight.Bold)
            }
            Divider(modifier = Modifier.padding(vertical = 7.dp))
        }
    }, bottomBar = {
        var textFieldValue by remember {
            mutableStateOf("")
        }
        Row(modifier = Modifier
            .padding(horizontal = 12.dp)
            .fillMaxWidth()) {
            TextField(
                value = textFieldValue,
                onValueChange = {
                    textFieldValue = it
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.text_box_placeholder),
                        fontSize = 18.sp
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(16.dp)),
                singleLine = false,
                maxLines = 4,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                trailingIcon = {
                    Image(imageVector = Icons.Default.Send, contentDescription = null)
                }
            )
        }
    }) {
        Column(modifier = Modifier.padding(it)) {

        }
    }
}