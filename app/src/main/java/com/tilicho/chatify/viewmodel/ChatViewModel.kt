package com.tilicho.chatify.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.tilicho.chatify.data.ChatData
import com.tilicho.chatify.data.Message
import com.tilicho.chatify.data.User
import com.tilicho.chatify.repository.ChatRepository

class ChatViewModel(lifecycleOwner: LifecycleOwner?, application: Application) :
    AndroidViewModel(application) {

    var selectedFriend = User()

    private val repository: ChatRepository? =
        lifecycleOwner?.let { ChatRepository(lifecycleOwner = it) }

    private var currentUser = repository?.currentUser

    var messages: MutableLiveData<MutableList<Message>> = MutableLiveData(mutableListOf())

    val getCurrentUser: MutableLiveData<User>?
        get() {
            return currentUser
        }

    val myChatFriendsIds: MutableLiveData<MutableList<String>>?
        get() {
            return repository?.myChatIds
        }

    val getFriendsList: MutableLiveData<MutableList<User>>?
        get() {
            return repository?.registeredFriends
        }

    val getMyChatFriendsDetails: MutableLiveData<MutableList<User>>?
        get() {
            return repository?.myChatFriendsDetails
        }

    val getTotalChats: MutableMap<String, ChatData>?
        get() {
            return repository?.chatData?.value
        }

    fun getUserDetails(uid: String): User? {
        return repository?.getUserDetails(uid = uid)
    }

    fun addMsgToChatId(selectedFriend: User, message: Message) {
        repository?.addMessageToChatId(selectedFriend = selectedFriend, message = message)
    }

    fun createNewChat(selectedFriend: User) {
        repository?.createNewChat(selectedFriend)
    }

    fun getMessages(uid: String) {
        messages.value = repository?.getMessages(uid = uid)
    }

    fun getLastMessage(uid: String): Message? {
        return repository?.getLastMessage(uid)
    }
    fun initViewModel() {
        repository?.getFriendsListFromFirebase()
        repository?.getMyChatFriends()
        repository?.getTotalChatsFromFirebase()
    }

    fun clear() {
        repository?.chatData?.value = mutableMapOf()
        repository?.myChatIds?.value = mutableListOf()
        repository?.registeredFriends?.value = mutableListOf()
        repository?.myChatFriendsDetails?.value = mutableListOf()
    }
}
