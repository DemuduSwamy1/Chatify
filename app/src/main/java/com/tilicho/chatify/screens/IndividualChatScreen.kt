package com.tilicho.chatify.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Grain
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import com.tilicho.chatify.R
import com.tilicho.chatify.data.Message
import com.tilicho.chatify.navigation.Screen
import com.tilicho.chatify.ui.theme.TextBackground
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
    val messages by remember {
        mutableStateOf(chatViewModel.messages.value)
    }
    Scaffold(modifier = Modifier.padding(5.dp), topBar = {
        Column {
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
        Column(modifier = Modifier.padding(top = dimensionResource(id = R.dimen.spacing_00))) {
            FlexChatBox(
                context = LocalContext.current,
                flexType = Pair(FlexType.CAMERA) {
                },
                textFieldPlaceHolder = stringResource(id = R.string.text_box_placeholder),
                onClickSend = { text ->
                    if (text.isNotEmpty()) {
                        message = Message(
                            message = text.trim(),
                            send_by = chatViewModel.getCurrentUser?.value?.uid.toString(),
                            time = System.currentTimeMillis().toString()
                        )
                        chatViewModel.addMsgToChatId(
                            selectedFriend = chatViewModel.selectedFriend,
                            message = message
                        )
                    }
                    chatViewModel.getMessages(chatViewModel.selectedFriend.uid)
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

            val lazyListState = rememberLazyListState()
            LaunchedEffect(sortedMessageIds.size) {
                if (sortedMessageIds.isNotEmpty()) {
                    lazyListState.animateScrollToItem(sortedMessageIds.size - 1)
                }
            }

            LazyColumn(
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        dimensionResource(id = R.dimen.spacing_10)
                    ), state = lazyListState
            ) {
                items(sortedMessageIds.size) { index ->
                    val _message = sortedMessages[sortedMessageIds[index]]
                    if (sortedMessages[sortedMessageIds[index]]?.send_by == currentUser) {
                        if (_message?.message?.length!! >= 25) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(dimensionResource(id = R.dimen.chat_text_cell_size))
                                        .background(
                                            color = TextBackground,
                                            shape = RoundedCornerShape(
                                                dimensionResource(id = R.dimen.spacing_50),
                                                dimensionResource(id = R.dimen.spacing_50),
                                                0.dp,
                                                dimensionResource(id = R.dimen.spacing_50)
                                            )
                                        ),
                                    contentAlignment = Alignment.BottomEnd
                                ) {
                                    Text(
                                        text = _message.message,
                                        fontSize = 16.sp,
                                        modifier = Modifier
                                            .padding(dimensionResource(id = R.dimen.spacing_10dp)),
                                        color = Color.Black
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacing_10dp)))
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Box(
                                    modifier = Modifier
                                        .background(
                                            color = TextBackground,
                                            shape = RoundedCornerShape(
                                                dimensionResource(id = R.dimen.spacing_50),
                                                dimensionResource(id = R.dimen.spacing_50),
                                                0.dp,
                                                dimensionResource(id = R.dimen.spacing_50)
                                            )
                                        )
                                        .wrapContentWidth(),
                                    contentAlignment = Alignment.BottomEnd
                                ) {
                                    Text(
                                        text = _message.message,
                                        fontSize = 16.sp,
                                        modifier = Modifier
                                            .padding(dimensionResource(id = R.dimen.spacing_10dp)),
                                        color = Color.Black
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacing_10dp)))
                        }
                    } else {
                        if (_message?.message?.length!! >= 25) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Box(
                                    contentAlignment = Alignment.BottomStart,
                                    modifier = Modifier
                                        .width(dimensionResource(id = R.dimen.chat_text_cell_size))
                                        .background(
                                            color = TextBackground,
                                            shape = RoundedCornerShape(
                                                dimensionResource(id = R.dimen.spacing_50),
                                                dimensionResource(id = R.dimen.spacing_50),
                                                0.dp,
                                                dimensionResource(id = R.dimen.spacing_50)
                                            )
                                        )
                                ) {
                                    Text(
                                        text = _message.message,
                                        fontSize = 16.sp,
                                        modifier = Modifier
                                            .padding(dimensionResource(id = R.dimen.spacing_10dp)),
                                        color = Color.Black
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacing_10dp)))
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Box(
                                    contentAlignment = Alignment.BottomStart,
                                    modifier = Modifier
                                        .background(
                                            color = TextBackground,
                                            shape = RoundedCornerShape(
                                                dimensionResource(id = R.dimen.spacing_50),
                                                dimensionResource(id = R.dimen.spacing_50),
                                                0.dp,
                                                dimensionResource(id = R.dimen.spacing_50)
                                            )
                                        )
                                        .wrapContentWidth()
                                ) {
                                    Text(
                                        text = _message.message,
                                        fontSize = 16.sp,
                                        modifier = Modifier
                                            .padding(dimensionResource(id = R.dimen.spacing_10dp)),
                                        color = Color.Black
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacing_10dp)))
                        }
                    }
                }
            }
        }
    }

}