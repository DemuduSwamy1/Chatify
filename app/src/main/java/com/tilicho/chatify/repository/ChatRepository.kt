package com.tilicho.chatify.repository

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import com.tilicho.chatify.constants.Constants
import com.tilicho.chatify.data.ChatData
import com.tilicho.chatify.data.ChatPair
import com.tilicho.chatify.data.Message
import com.tilicho.chatify.data.User
import java.util.UUID


class ChatRepository(val application: Application, val lifecycleOwner: LifecycleOwner) {
    private lateinit var auth: FirebaseAuth
    private val firebaseDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference
    var registeredFriends: MutableLiveData<MutableList<User>> = MutableLiveData(mutableListOf())
    var myChatIds: MutableLiveData<MutableList<String>> = MutableLiveData(mutableListOf())
    var chatData: MutableLiveData<MutableMap<String, ChatData>> = MutableLiveData(mutableMapOf())
    var currentUser: MutableLiveData<User> = MutableLiveData(User())
    var myChatFriendsDetails: MutableLiveData<MutableList<User>> = MutableLiveData(mutableListOf())


    init {
        getFriendsListFromFirebase()
        getMyChatFriends()
        getTotalChatsFromFirebase()
    }


    private fun setCurrentUser() {
        auth = FirebaseAuth.getInstance()
        var tempRegisterdFriends: MutableList<User> = mutableListOf()
        registeredFriends.observe(lifecycleOwner) {
            tempRegisterdFriends = it
        }
        tempRegisterdFriends.forEach { user ->
            if (user.uid == auth.uid) {
                currentUser.postValue(user)
            }
        }
    }

    private fun getFriendsListFromFirebase() {
        val databaseReference =
            firebaseDatabase.child(Constants.UserAttributes.USERS)
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val typeIndicator = object : GenericTypeIndicator<HashMap<String, User>>() {}
                val snapshotOfData = snapshot.getValue(typeIndicator)
                if (snapshotOfData?.values != null) {
                    val usersData = snapshotOfData.values.toMutableStateList().toMutableList()
                    registeredFriends.value = usersData
                    setCurrentUser()
                    setMyChatFriendsInfo()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                /*To do*/
            }
        })
    }

    private fun writeNewChatId(selectedFriend: User): String {
        auth = FirebaseAuth.getInstance()
        val uuid = UUID.randomUUID().toString()

        firebaseDatabase.child(Constants.UserAttributes.CHATS).child(uuid).child("chat_pair")
            .setValue(ChatPair(auth.currentUser?.uid, selectedFriend.uid))

        firebaseDatabase.child(Constants.UserAttributes.USERS)
            .child(auth.currentUser?.uid.toString()).child(Constants.UserAttributes.CHATS).push()
            .setValue(uuid)

        firebaseDatabase.child(Constants.UserAttributes.USERS).child(selectedFriend.uid)
            .child(Constants.UserAttributes.CHATS).push()
            .setValue(uuid)
        return uuid
    }

    fun createNewChat(selectedFriend: User) {
        var tempChatData: MutableMap<String, ChatData> = mutableMapOf()
        chatData.observe(lifecycleOwner) {
            tempChatData = it
        }
        auth = FirebaseAuth.getInstance()
        var createNewId = true
        var tempMyChatIds: MutableList<String> = mutableListOf()
        myChatIds.observe(lifecycleOwner) {
            tempMyChatIds = it
        }
        tempMyChatIds.forEach { chatId ->
            if (tempChatData.keys.contains(chatId)) {
                val data = tempChatData.getValue(chatId)
                if (data.chat_pair.values.contains(selectedFriend.uid) && data.chat_pair.values.contains(
                        auth.uid
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

    private fun getMyChatFriends() {
        auth = FirebaseAuth.getInstance()
        val databaseReference = firebaseDatabase.child(Constants.UserAttributes.USERS)
            .child(auth.currentUser?.uid.toString()).child(Constants.UserAttributes.CHATS)

        databaseReference.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val typeIndicator = object : GenericTypeIndicator<HashMap<String, String>>() {}
                val snapshotOfData = snapshot.getValue(typeIndicator)
                if (snapshotOfData?.values != null) {
                    myChatIds.value = snapshotOfData.values.toMutableList()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                /*To Do*/
            }
        })
    }

    fun addMessageToChatId(selectedFriend: User, message: Message) {
        var tempChatData: MutableMap<String, ChatData> = mutableMapOf()
        chatData.observe(lifecycleOwner) {
            tempChatData = it
        }
        var tempMyChatIds: MutableList<String> = mutableListOf()
        myChatIds.observe(lifecycleOwner) {
            tempMyChatIds = it
        }
        tempMyChatIds.forEach { chatId ->
            if (tempChatData.keys.contains(chatId)) {
                val chatPair = tempChatData.getValue(chatId).chat_pair.values.toMutableList()
                if (chatPair.contains(selectedFriend.uid)) {
                    firebaseDatabase.child(Constants.UserAttributes.CHATS).child(chatId)
                        .child(Constants.UserAttributes.MESSAGES)
                        .push()
                        .setValue(message)
                    firebaseDatabase.child(Constants.UserAttributes.CHATS).child(chatId)
                        .child("last_message").setValue(message)
                }
            }
        }
    }

    private fun getTotalChatsFromFirebase() {
        auth = FirebaseAuth.getInstance()
        val databaseReference = firebaseDatabase.child(Constants.UserAttributes.CHATS)
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val typeIndicator =
                    object : GenericTypeIndicator<HashMap<String, ChatData>>() {}
                val snapshotOfData = snapshot.getValue(typeIndicator)
                if (snapshotOfData?.values != null) {
                    /*chatData.postValue(snapshotOfData.toMutableMap())*/
                    chatData.value = snapshotOfData.toMutableMap()
                    Log.d("chatData_001", chatData.value.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                /* To do*/
            }

        })
    }

    fun getUserDetails(uid: String): User {
        var tempRegisteredFriends: MutableList<User> = mutableListOf()
        val user = mutableStateOf(User())
        registeredFriends.observe(lifecycleOwner) {
            tempRegisteredFriends = it
        }
        tempRegisteredFriends.forEach { item ->
            if (item.uid == uid) {
                user.value = item
            }
        }
        return user.value
    }

    fun setMyChatFriendsInfo() {
        var tempChatData = chatData.value
        chatData.observe(lifecycleOwner) {
            tempChatData = it
        }
        var tempMyChatIds = myChatIds.value
        myChatIds.observe(lifecycleOwner) {
            tempMyChatIds = it
        }
        tempMyChatIds?.forEach { chatId ->
            if (tempChatData?.keys?.contains(chatId) == true) {
                val messages = tempChatData?.getValue(chatId)?.messages?.toMutableMap()
                if (messages?.isNotEmpty() == true) {
                    val chatPairIds = tempChatData?.getValue(chatId)?.chat_pair?.values
                    var userId = ""
                    chatPairIds?.forEach {
                        if (auth.currentUser?.uid != it) {
                            userId = it
                        }
                    }
                    myChatFriendsDetails.value?.add(getUserDetails(userId))
                }
            }
        }
    }

    fun getMessages(uid: String): MutableList<Message> {
        var tempChatData: MutableMap<String, ChatData> = mutableMapOf()
        chatData.observe(lifecycleOwner) {
            tempChatData = it
        }
        var tempMyChatIds: MutableList<String> = mutableListOf()
        myChatIds.observe(lifecycleOwner) {
            tempMyChatIds = it
        }
        var messages = mutableListOf<Message>()
        tempMyChatIds.forEach { id ->
            val chatInfo = tempChatData.getValue(id)
            if (chatInfo.chat_pair.values.contains(uid)) {
                messages = chatInfo.messages.values.toMutableList()
            }
        }
        return messages
    }

    fun getLastMessage(uid: String): Message {
        var tempChatData: MutableMap<String, ChatData> = mutableMapOf()
        chatData.observe(lifecycleOwner) {
            tempChatData = it
        }
        var tempMyChatIds: MutableList<String> = mutableListOf()
        myChatIds.observe(lifecycleOwner) {
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
}

