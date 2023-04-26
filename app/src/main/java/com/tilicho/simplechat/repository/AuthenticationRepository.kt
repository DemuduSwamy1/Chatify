package com.tilicho.simplechat.repository

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tilicho.simplechat.constants.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONObject

class AuthenticationRepository(application: Application) {

    object PreferenceKeys {
        val uid = stringPreferencesKey(Constants.UserAttributes.UID)
    }

    private var application: Application
    private var userMutableLiveData: MutableLiveData<FirebaseUser>
    private var auth: FirebaseAuth
    private var jsonObject: JSONObject
    private var isUserRegistered: MutableLiveData<Boolean>

    private val PREFERENCES_NAME_USER =  "sample_datastore_prefs"

    private val Context.prefsDataStore by preferencesDataStore(
        name = PREFERENCES_NAME_USER
    )

    val getFirebaseUserMutableLiveData: MutableLiveData<FirebaseUser>
        get() {
            return userMutableLiveData
        }

    val getRegistrationStatusMutableLiveData: MutableLiveData<Boolean>
        get() {
            return isUserRegistered
        }

    init {
        this.application = application
        userMutableLiveData = MutableLiveData()
        isUserRegistered = MutableLiveData()
        auth = FirebaseAuth.getInstance()
        jsonObject = JSONObject()

        if (auth.currentUser != null) {
            userMutableLiveData.postValue(auth.currentUser)
        }
    }

    fun registerUser(email: String, password: String, name: String) {
        jsonObject.put(Constants.UserAttributes.EMAIL, email)
        jsonObject.put(Constants.UserAttributes.NAME, name)
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                isUserRegistered.postValue(true)
                userMutableLiveData.postValue(auth.currentUser)
            } else {
                Toast.makeText(application, task.exception?.message, Toast.LENGTH_LONG).show()
            }
        }
        writeUserDataToFirebase(jsonObject)
    }

    private fun writeUserDataToFirebase(userData: JSONObject) {
        val userRef: DatabaseReference = FirebaseDatabase.getInstance().reference
        val jsonMap: Map<String, Any> =
            Gson().fromJson(userData.toString(),
                object : TypeToken<HashMap<String?, Any?>?>() {}.type)

        auth.currentUser?.uid?.let {
            userRef.child(Constants.UserAttributes.USERS).child(it)
                .updateChildren(jsonMap)
        }
    }

    fun checkEmailExistsOrNotAndCreateAccount(email: String): Boolean {
        var isEmailAlreadyExists = false
        auth.fetchSignInMethodsForEmail(email).addOnCompleteListener { task ->
            isEmailAlreadyExists = task.result.signInMethods?.size != 0
        }.addOnFailureListener { e -> e.printStackTrace() }
        return isEmailAlreadyExists
    }

    suspend fun saveUserUid(uid: String) {
        application.prefsDataStore.edit { preferences ->
            preferences[PreferenceKeys.uid] = uid
        }
    }

    fun getUserUid(): Flow<String> {
        return application.prefsDataStore.data
            .map { preferences ->
                preferences[PreferenceKeys.uid] ?: ""
            }
    }
}