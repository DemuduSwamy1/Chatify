package com.tilicho.chatify.navigation

sealed class Screen(val route: String) {
    object RegisterScreen : Screen(route = "register_screen")
    object ChatsScreen : Screen(route = "chats_screen")
    object IndividualChatScreen : Screen(route = "individual_chat_screen")
}
