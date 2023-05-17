package com.tilicho.chatify.data

import com.google.gson.annotations.SerializedName


data class Message(
    val message: String = "",
    val send_by: String = "",
    val time: Long = 0L
)


data class ChatPair(
    val friend1: String? = "",
    val friend2: String = ""
)

data class Chat(
    val chatList: List<ChatData>? = mutableListOf()
)

data class ChatData(
    @SerializedName("chat_pair")
    val chat_pair: HashMap<String, String> = hashMapOf(),

    @SerializedName("messages")
    val messages: HashMap<String, Message> = hashMapOf()
)

class Messages(
    val messageList: MessageList
)

class MessageList(
    val messages: List<Message> = listOf()
)
