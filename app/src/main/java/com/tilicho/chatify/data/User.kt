package com.tilicho.chatify.data

import com.google.gson.annotations.SerializedName


data class User(
    @SerializedName("name")
    val name: String = "",
    @SerializedName("email_id")
    val email: String = "",
    @SerializedName("uid")
    val uid: String = ""
    )
