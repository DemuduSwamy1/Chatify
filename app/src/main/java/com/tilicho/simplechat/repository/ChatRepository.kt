package com.tilicho.simplechat.repository

import android.app.Application
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.unit.Constraints
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import com.tilicho.simplechat.constants.Constants
import com.tilicho.simplechat.data.Message
import com.tilicho.simplechat.data.User
import org.json.JSONObject
import java.util.UUID

class ChatRepository(application: Application) {
    private lateinit var auth: FirebaseAuth
    private val firebaseDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference
    var friends: MutableLiveData<MutableList<User>> = MutableLiveData(mutableListOf())

    fun getFriendsListFromFirebase(): MutableLiveData<MutableList<User>> {
        val databaseReference =
            firebaseDatabase.child("users")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val typeIndicator = object : GenericTypeIndicator<HashMap<String, User>>() {}
                val snapshotOfData = snapshot.getValue(typeIndicator)
                if (snapshotOfData?.values != null) {
                    val usersData = snapshotOfData.values.toMutableStateList().toMutableList()
                    friends.postValue(usersData)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        return friends
    }

    fun writeNewChatId(){
        auth = FirebaseAuth.getInstance()
        val uuid = UUID.randomUUID().toString()
        firebaseDatabase.child(Constants.UserAttributes.CHATS).child(uuid).setValue(Message("","","",))
        firebaseDatabase.child(Constants.UserAttributes.USERS).child(auth.currentUser?.uid.toString()).child("chats").setValue("MyChats")
    }

}