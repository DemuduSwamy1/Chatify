package com.tilicho.simplechat.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.tilicho.simplechat.enums.ChatTabs
import com.tilicho.simplechat.R
import com.tilicho.simplechat.navigation.Screen
import com.tilicho.simplechat.viewmodel.AuthViewModel

@Composable
fun ChatsScreen(navController: NavHostController, authViewModel: AuthViewModel) {
    var setFriendDialog by remember {
        mutableStateOf(false)
    }
    var selected by remember {
        mutableStateOf(ChatTabs.CHATS)
    }
    if (setFriendDialog) {
        SelectFriendDialog(navController = navController,friends = mutableListOf(), dialogCallBack = {
            setFriendDialog = it
        })
    }
    Column(modifier = Modifier.padding(15.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(id = R.string.chatify),
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp
            )
            Spacer(modifier = Modifier.weight(1f))
            Image(
                imageVector = Icons.Default.Search,
                contentDescription = null, modifier = Modifier
                    .size(30.dp)
                    .clickable {
                        setFriendDialog = true
                    }
            )
        }
        Spacer(modifier = Modifier.height(15.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.chats),
                fontWeight = FontWeight.Bold,
                color = if(selected == ChatTabs.CHATS)Color(0xFF07DC8A) else Color.Black,
                fontSize = 18.sp,
                modifier = Modifier.clickable {
                    selected = ChatTabs.CHATS
                }
            )
            Text(
                text = stringResource(id = R.string.groups),
                fontWeight = FontWeight.Bold,
                color = if(selected == ChatTabs.GROUPS)Color(0xFF07DC8A) else Color.Black,
                fontSize = 18.sp,
                modifier = Modifier.clickable {
                    selected = ChatTabs.GROUPS
                }
            )
            Text(
                text = stringResource(id = R.string.status),
                fontWeight = FontWeight.Bold,
                color = if(selected == ChatTabs.STATUS)Color(0xFF07DC8A) else Color.Black,
                fontSize = 18.sp,
                modifier = Modifier.clickable {
                    selected = ChatTabs.STATUS
                }
            )
            Text(
                text = stringResource(id = R.string.calls),
                color = if(selected == ChatTabs.CALLS)Color(0xFF07DC8A) else Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.clickable {
                    selected = ChatTabs.CALLS
                }
            )
        }
        Spacer(modifier = Modifier.height(15.dp))
        LazyColumn {
            item {
                for (i in 0..30) {
                    ChatItem(
                        image = R.drawable.ic_launcher_background,
                        name = "Ali Tahir",
                        lastMsg = "maciej.kowalski@email.com",
                        time = "10:43 AM", onClickAction = {
                            navController.navigate(Screen.IndividualChatScreen.route)

                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ChatItem(onClickAction: () -> Unit,image: Int, name: String, lastMsg: String, time: String) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .clickable {
            onClickAction.invoke()
        }) {
        Row(modifier = Modifier.padding(5.dp)) {
            Image(
                imageVector = ImageVector.vectorResource(id = image),
                contentDescription = null,
                modifier = Modifier
                    .clip(shape = CircleShape)
                    .size(50.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(text = name, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = lastMsg, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(text = time, fontSize = 12.sp)
        }
    }
}

@Composable
fun SelectFriendDialog(
    dialogCallBack: (Boolean) -> Unit,
    friends: MutableList<String>,
    navController: NavHostController
) {
    Dialog(properties = DialogProperties(
        dismissOnBackPress = true,
        dismissOnClickOutside = true
    ), onDismissRequest = {
        dialogCallBack(false)
    }) {
        Column(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(12.dp))
                .size(width = 300.dp, height = 500.dp)
                .background(color = Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(10.dp),
                textAlign = TextAlign.Center,
                text = stringResource(id = R.string.my_friends_title),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            LazyColumn(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.Start) {
                item {
                    for (i in 0..30) {
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(Screen.IndividualChatScreen.route)
                            }){
                            Text(text = "My friend")
                        }
                        Divider(modifier = Modifier.padding(vertical = 10.dp))
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(Screen.IndividualChatScreen.route)
                            }){
                            Text(text = "Living Legend")
                        }
                        Divider(modifier = Modifier.padding(vertical = 10.dp))
                    }
                }
            }
        }
    }
}