package com.tilicho.simplechat.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.tilicho.simplechat.data.ChatData
import com.tilicho.simplechat.data.Message
import com.tilicho.simplechat.data.User
import com.tilicho.simplechat.repository.ChatRepository

class ChatViewModel(lifecycleOwner: LifecycleOwner, application: Application) :
    AndroidViewModel(application) {


    var selectedFriend = User()

    private val repository: ChatRepository =
        ChatRepository(application = application, lifecycleOwner = lifecycleOwner)
    private var currentUser = repository.currentUser

    var messages: MutableLiveData<MutableList<Message>> = MutableLiveData(mutableListOf())

    val getCurrentUser: MutableLiveData<User>
        get() {
            return currentUser
        }

    val myChatFriendsIds: MutableLiveData<MutableList<String>>
        get() {
            return repository.myChatIds
        }

    val getFriendsList: MutableLiveData<MutableList<User>>
        get() {
            return repository.registeredFriends
        }

    val getMyChatFriendsInfo: MutableLiveData<MutableList<User>>
        get() {
            return repository.myChatFriendsDetails
        }

    private val getTotalChats: MutableMap<String, ChatData>?
        get() {
            return repository.chatData.value
        }

    fun getUserDetails(uid: String): User {
        return repository.getUserDetails(uid = uid)
    }

    fun addMsgToChatId(selectedFriend: User, message: Message) {
        repository.addMessageToChatId(selectedFriend = selectedFriend, message = message)
    }

    fun createNewChat(selectedFriend: User) {
        repository.createNewChat(selectedFriend)
    }

    fun getMessages(uid: String) {
        messages.value = repository.gerMessages(uid = uid)
    }
}