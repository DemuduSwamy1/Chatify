package com.tilicho.simplechat.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tilicho.simplechat.data.User
import com.tilicho.simplechat.repository.ChatRepository
import java.util.UUID

class ChatViewModel(application: Application): ViewModel() {

    private val repository: ChatRepository = ChatRepository(application)


    fun getFriendsList(): MutableLiveData<MutableList<User>> {
        return repository.getFriendsListFromFirebase()
    }

    var selectedFriend  = ""

    fun writeNewChatId(){
        repository.writeNewChatId()
    }
}