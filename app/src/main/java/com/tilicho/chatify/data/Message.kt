package com.tilicho.chatify.data

import com.google.gson.annotations.SerializedName


data class Message(
    val message: String = "",
    val send_by: String = "",
    val time: String = ""
)


data class ChatPair(
    val friend1: String? = "",
    val friend2: String = ""
)


data class ChatData(
    @SerializedName("chat_pair")
    val chat_pair: HashMap<String, String> = hashMapOf(),

    @SerializedName("messages")
    val messages: HashMap<String, Message> = hashMapOf(),

    @SerializedName("last_message")
    val last_message: Message = Message()
)
