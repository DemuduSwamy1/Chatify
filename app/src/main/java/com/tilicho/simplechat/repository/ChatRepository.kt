package com.tilicho.simplechat.repository

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
import com.tilicho.simplechat.constants.Constants
import com.tilicho.simplechat.data.ChatData
import com.tilicho.simplechat.data.ChatPair
import com.tilicho.simplechat.data.Message
import com.tilicho.simplechat.data.User
import java.util.UUID


class ChatRepository(val application: Application,val lifecycleOwner: LifecycleOwner) {
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
        registeredFriends.value?.forEach { user ->
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
                TODO("Not yet implemented")
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
        val chatData = chatData.value
        auth = FirebaseAuth.getInstance()
        Log.d("selectedFriend_001","selectedFriend.uid")
        var createNewId = true
        val myChatIds = myChatIds
        Log.d("chatdata_003",myChatIds.value.toString() + this@ChatRepository.toString())
        myChatIds.value?.forEach { chatId ->
            if (chatData?.keys?.contains(chatId) == true) {
                val data = chatData.getValue(chatId)
                if (data.chat_pair.values.contains(selectedFriend.uid) && data.chat_pair.values.contains(
                        auth.uid
                    )
                ) {
                    createNewId = false
                }
            }
        }
        if(createNewId){
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
                    val ids = snapshotOfData.values.toMutableList()
                    myChatIds.value = ids
                    Log.d("chatdata_005",myChatIds.value.toString() + this@ChatRepository.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                /*To Do*/
            }
        })
    }

    fun addMessageToChatId(selectedFriend: User, message: Message) {
        val chatData = chatData.value
        Log.d("chatData_test_001",chatData?.values.toString())
        val myChatIds = myChatIds
        myChatIds.value?.forEach { chatId ->
            if (chatData?.keys?.contains(chatId) == true) {
                val chatPair = chatData.getValue(chatId).chat_pair.values.toMutableList()
                if (chatPair.contains(selectedFriend.uid)) {
                    firebaseDatabase.child(Constants.UserAttributes.CHATS).child(chatId)
                        .child(Constants.UserAttributes.MESSAGES)
                        .push()
                        .setValue(message)
                }
            }
        }
    }

     private fun getTotalChatsFromFirebase() {
        auth = FirebaseAuth.getInstance()
        val databaseReference = firebaseDatabase.child(Constants.UserAttributes.CHATS)
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("chatData_test_002","chatData?.values.toString()")
                val typeIndicator =
                    object : GenericTypeIndicator<HashMap<String, ChatData>>() {}
                val snapshotOfData = snapshot.getValue(typeIndicator)
                if (snapshotOfData?.values != null) {
                    /*chatData.value = snapshotOfData.toMutableMap()*/
                    chatData.postValue(snapshotOfData.toMutableMap())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun getUserDetails(uid:String):User{
        val user = mutableStateOf(User())
        registeredFriends.value?.forEach { item ->
            if (item.uid == uid) {
                user.value = item
            }
        }
        return user.value
    }

    fun setMyChatFriendsInfo(){
        val chatData  = chatData.value
        Log.d("chatdata_001",myChatIds.value.toString())
        myChatIds.value?.forEach { chatId->
            if(chatData?.keys?.contains(chatId) == true){
                val messages = chatData.getValue(chatId).messages.toMutableMap()
                if(messages.isNotEmpty()){
                    val chatPairIds = chatData.getValue(chatId).chat_pair.values
                    var userId = ""
                    chatPairIds.forEach {
                        if(auth.currentUser?.uid != it){
                            userId = it
                        }
                    }
                    myChatFriendsDetails.value?.add(getUserDetails(userId))
                }
            }
        }
    }

    fun gerMessages(uid: String): MutableList<Message> {
        val chatData  = chatData.value
        val chatIds = myChatIds.value
        var messages = mutableListOf<Message>()
        chatIds?.forEach { id->
            val chatInfo = chatData?.getValue(id)
            if(chatInfo?.chat_pair?.values?.contains(uid) == true){
                messages = chatInfo.messages.values.toMutableList()
            }
        }
        return messages
    }
}

