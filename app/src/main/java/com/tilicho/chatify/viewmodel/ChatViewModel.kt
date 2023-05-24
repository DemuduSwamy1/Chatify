package com.tilicho.chatify.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import com.tilicho.chatify.constants.Constants
import com.tilicho.chatify.data.ChatData
import com.tilicho.chatify.data.ChatPair
import com.tilicho.chatify.data.Message
import com.tilicho.chatify.data.User
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.*

class ChatViewModel(lifecycleOwner: LifecycleOwner?, application: Application) :
    AndroidViewModel(application) {

    private lateinit var _lifecycleOwner: LifecycleOwner

    var mDatabase: FirebaseDatabase? = null
    var auth: FirebaseAuth? = null

    init {
        viewModelScope.launch {
            mDatabase = FirebaseDatabase.getInstance()
            auth = FirebaseAuth.getInstance()

            if (lifecycleOwner != null) {
                _lifecycleOwner = lifecycleOwner
            }
            addValueEventListeners()
        }
    }

    suspend fun addValueEventListeners() {
        coroutineScope {
            mDatabase?.getReference(Constants.UserAttributes.CHATS)?.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val typeIndicator =
                        object : GenericTypeIndicator<HashMap<String, ChatData>>() {}
                    val snapshotOfData = snapshot.getValue(typeIndicator)
                    if (snapshotOfData?.values != null) {
                        _chatData.value = snapshotOfData.toMutableMap()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        getApplication(),
                        error.message,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            })

            mDatabase?.getReference(Constants.UserAttributes.USERS)?.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val typeIndicator = object : GenericTypeIndicator<HashMap<String, User>>() {}
                    val snapshotOfData = snapshot.getValue(typeIndicator)
                    if (snapshotOfData?.values != null) {
                        val usersData = snapshotOfData.values.toMutableStateList().toMutableList()
                        _registeredFriends.value = usersData
                        setCurrentUser()
                        setMyChatFriendsInfo()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        getApplication(),
                        error.message,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            })

            mDatabase?.getReference(Constants.UserAttributes.USERS)
                ?.child(auth?.currentUser?.uid.toString())?.child(Constants.UserAttributes.CHATS)
                ?.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    val typeIndicator = object : GenericTypeIndicator<HashMap<String, String>>() {}
                    val snapshotOfData = snapshot.getValue(typeIndicator)
                    if (snapshotOfData?.values != null) {
                        _myChatIds.value = snapshotOfData.values.toMutableList()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        getApplication(),
                        error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }

    var selectedFriend = User()

    var messages: MutableLiveData<MutableList<Message>> = MutableLiveData(mutableListOf())

    fun addMsgToChatId(selectedFriend: User, message: Message) {
        addMessageToChatId(selectedFriend = selectedFriend, message = message)
    }

    fun addMessageToChatId(selectedFriend: User, message: Message) {
        var tempChatData: MutableMap<String, ChatData> = mutableMapOf()
        chatData.observe(_lifecycleOwner) {
            tempChatData = it
        }
        var tempMyChatIds: MutableList<String> = mutableListOf()
        myChatIds.observe(_lifecycleOwner) {
            tempMyChatIds = it
        }
        tempMyChatIds.forEach { chatId ->
            if (tempChatData.keys.contains(chatId)) {
                val chatPair = tempChatData.getValue(chatId).chat_pair.values.toMutableList()
                if (chatPair.contains(selectedFriend.uid)) {
                    mDatabase?.getReference(Constants.UserAttributes.CHATS)?.child(chatId)
                        ?.child(Constants.UserAttributes.MESSAGES)
                        ?.push()
                        ?.setValue(message)
                    mDatabase?.getReference(Constants.UserAttributes.CHATS)?.child(chatId)
                        ?.child("last_message")?.setValue(message)
                }
            }
        }
    }

    fun getMessages(uid: String) {
        var tempChatData: MutableMap<String, ChatData> = mutableMapOf()
        chatData.observe(_lifecycleOwner) {
            tempChatData = it
        }
        var tempMyChatIds: MutableList<String> = mutableListOf()
        myChatIds.observe(_lifecycleOwner) {
            tempMyChatIds = it
        }
        var _messages = mutableListOf<Message>()
        tempMyChatIds.forEach { id ->
            val chatInfo = tempChatData.getValue(id)
            if (chatInfo.chat_pair.values.contains(uid)) {
                _messages = chatInfo.messages.values.toMutableList()
            }
        }
       messages.value = _messages
    }

    fun getLastMessage(uid: String): Message {
        var tempChatData: MutableMap<String, ChatData> = mutableMapOf()
        chatData.observe(_lifecycleOwner) {
            tempChatData = it
        }
        var tempMyChatIds: MutableList<String> = mutableListOf()
        myChatIds.observe(_lifecycleOwner) {
            tempMyChatIds = it
        }
        var message = Message()
        tempMyChatIds.forEach { id ->
            if (tempChatData.keys.contains(id)) {
                val chatData = tempChatData.getValue(id)
                if (chatData.chat_pair.values.contains(uid)) {
                    message = chatData.last_message
                }
            }
        }
        return message
    }

    fun clear() {
        _chatData.value = mutableMapOf()
        _myChatIds.value = mutableListOf()
        _registeredFriends.value = mutableListOf()
        _myChatFriendsDetails.value = mutableListOf()
    }

    fun setCurrentUser() {
        auth = FirebaseAuth.getInstance()
        var tempRegisteredFriends: MutableList<User> = mutableListOf()
        registeredFriends.observe(_lifecycleOwner) {
            tempRegisteredFriends = it
        }
        tempRegisteredFriends.forEach { user ->
            if (user.uid == auth?.uid) {
                _currentUser.postValue(user)
            }
        }
    }

    fun setMyChatFriendsInfo() {
        var tempChatData: MutableMap<String, ChatData>? = mutableMapOf()
        _chatData.observe(_lifecycleOwner) {
            tempChatData = it
        }
        var tempMyChatIds = _myChatIds.value
        _myChatIds.observe(_lifecycleOwner) {
            tempMyChatIds = it
        }
        tempMyChatIds?.forEach { chatId ->
            if (tempChatData?.keys?.contains(chatId) == true) {
                val messages = tempChatData?.getValue(chatId)?.messages?.toMutableMap()
                if (messages?.isNotEmpty() == true) {
                    val chatPairIds = tempChatData?.getValue(chatId)?.chat_pair?.values
                    var userId = ""
                    chatPairIds?.forEach {
                        if (auth?.currentUser?.uid != it) {
                            userId = it
                        }
                    }
                    getUserDetails(userId).let {
                        _myChatFriendsDetails.value?.plus(it)
                    }
                }
            }
        }
    }

    private fun getUserDetails(uid: String): User {
        var tempRegisteredFriends: MutableList<User> = mutableListOf()
        val user = mutableStateOf(User())
        registeredFriends.observe(_lifecycleOwner) {
            tempRegisteredFriends = it
        }
        tempRegisteredFriends.forEach { item ->
            if (item.uid == uid) {
                user.value = item
            }
        }
        return user.value
    }

    fun createNewChat(selectedFriend: User) {
        var tempChatData: MutableMap<String, ChatData> = mutableMapOf()
        chatData.observe(_lifecycleOwner) {
            tempChatData = it
        }
        auth = FirebaseAuth.getInstance()
        var createNewId = true
        var tempMyChatIds: MutableList<String> = mutableListOf()
        myChatIds.observe(_lifecycleOwner) {
            tempMyChatIds = it
        }
        tempMyChatIds.forEach { chatId ->
            if (tempChatData.keys.contains(chatId)) {
                val data = tempChatData.getValue(chatId)
                if (data.chat_pair.values.contains(selectedFriend.uid) && data.chat_pair.values.contains(
                        auth?.uid
                    )
                ) {
                    createNewId = false
                }
            }
        }
        if (createNewId) {
            writeNewChatId(selectedFriend)
        }
    }

    private fun writeNewChatId(selectedFriend: User): String {
        auth = FirebaseAuth.getInstance()
        val uuid = UUID.randomUUID().toString()

        mDatabase?.getReference(Constants.UserAttributes.CHATS)?.child(uuid)?.child("chat_pair")
            ?.setValue(ChatPair(auth?.currentUser?.uid, selectedFriend.uid))

        mDatabase?.getReference(Constants.UserAttributes.USERS)
            ?.child(auth?.currentUser?.uid.toString())?.child(Constants.UserAttributes.CHATS)?.push()
            ?.setValue(uuid)

        mDatabase?.getReference(Constants.UserAttributes.USERS)?.child(selectedFriend.uid)
            ?.child(Constants.UserAttributes.CHATS)?.push()
            ?.setValue(uuid)
        return uuid
    }

    private val _registeredFriends: MutableLiveData<MutableList<User>> =
        MutableLiveData()
    val registeredFriends: LiveData<MutableList<User>>
        get() = _registeredFriends

    private var _myChatIds: MutableLiveData<MutableList<String>> = MutableLiveData()
    val myChatIds: LiveData<MutableList<String>>
        get() = _myChatIds

    private var _chatData: MutableLiveData<MutableMap<String, ChatData>> = MutableLiveData()
    val chatData: MutableLiveData<MutableMap<String, ChatData>>
        get() = _chatData

    private var _myChatFriendsDetails: MutableLiveData<MutableList<User>> = MutableLiveData()
    val myChatFriendsDetails: LiveData<MutableList<User>>
        get() = _myChatFriendsDetails

    private var _currentUser: MutableLiveData<User> = MutableLiveData(User())
    val currentUser: LiveData<User>
        get() = _currentUser
}
