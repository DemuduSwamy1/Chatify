package com.tilicho.chatify.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Grain
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import com.tilicho.chatify.R
import com.tilicho.chatify.data.Message
import com.tilicho.chatify.navigation.Screen
import com.tilicho.chatify.viewmodel.AuthViewModel
import com.tilicho.chatify.viewmodel.ChatViewModel
import `in`.tilicho.flexchatbox.FlexChatBox
import `in`.tilicho.flexchatbox.enums.FlexType

@SuppressLint("MutableCollectionMutableState")
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun IndividualChatScreen(
    lifecycleOwner: LifecycleOwner,
    navController: NavHostController,
    authViewModel: AuthViewModel,
    chatViewModel: ChatViewModel,
) {
    var message by remember {
        mutableStateOf(Message())
    }
    val currentUser by remember {
        mutableStateOf(chatViewModel.getCurrentUser?.value?.uid)
    }
    var selectedUser by remember {
        mutableStateOf(chatViewModel.selectedFriend.uid)
    }
    val messages by remember {
        mutableStateOf(chatViewModel.messages.value)
    }
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
                        navController.navigate(Screen.ChatsScreen.route) {
                            popUpTo(Screen.IndividualChatScreen.route) {
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
                Text(
                    text = chatViewModel.selectedFriend.name,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Divider(modifier = Modifier.padding(vertical = 7.dp))
        }
    }, bottomBar = {
        Column {
            FlexChatBox(
                context = LocalContext.current,
                flexType = Pair(FlexType.CAMERA) { callback ->

                },
                textFieldPlaceHolder = stringResource(id = R.string.text_box_placeholder),
                onClickSend = { text ->
                    message = Message(
                        message = text,
                        send_by = chatViewModel.getCurrentUser?.value?.uid.toString(),
                        time = System.currentTimeMillis().toString()
                    )
                    chatViewModel.addMsgToChatId(
                        selectedFriend = chatViewModel.selectedFriend,
                        message = message
                    )
                }
            )
        }
    }) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(), verticalArrangement = Arrangement.Bottom
        ) {
            val sortedMessages by remember {
                mutableStateOf(mutableMapOf<String, Message>())
            }
            val sortedMessageIds by remember {
                mutableStateOf(mutableListOf<String>())
            }
            messages?.forEach { message ->
                sortedMessages[message.time] = message
                sortedMessageIds.add(message.time)
            }
            sortedMessageIds.sort()

            LazyColumn(verticalArrangement = Arrangement.Bottom) {
                items(sortedMessageIds.size) { index ->
                    val message = sortedMessages[sortedMessageIds[index].toString()]
                    if (message?.send_by == currentUser) {
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                textAlign = TextAlign.End,
                                text = message?.message.toString(),
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    } else {
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                textAlign = TextAlign.Start,
                                text = message?.message.toString(),
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}