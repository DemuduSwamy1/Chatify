package com.tilicho.chatify.repository

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
import com.tilicho.chatify.constants.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.json.JSONObject


class AuthenticationRepository(application: Application) {
    object PreferenceKeys {
        val uid = stringPreferencesKey(Constants.UserAttributes.UID)
    }

    private var application: Application
    private var userMutableLiveData: MutableLiveData<FirebaseUser>
    private var auth: FirebaseAuth
    private var jsonObject: JSONObject
    private var isUserRegistered: Boolean

    private val PREFERENCES_NAME_USER =  "sample_datastore_prefs"

    private val userRef: DatabaseReference = FirebaseDatabase.getInstance().reference

    private val Context.prefsDataStore by preferencesDataStore(
        name = PREFERENCES_NAME_USER
    )

    val getFirebaseUserMutableLiveData: MutableLiveData<FirebaseUser>
        get() {
            return userMutableLiveData
        }

    val getRegistrationStatusMutableLiveData: Boolean
        get() {
            return isUserRegistered
        }

    init {
        this.application = application
        userMutableLiveData = MutableLiveData()
        isUserRegistered = false
        auth = FirebaseAuth.getInstance()
        jsonObject = JSONObject()

        if (auth.currentUser != null) {
            userMutableLiveData.postValue(auth.currentUser)
        }
    }

    fun registerUser(email: String, password: String, name: String, scope: CoroutineScope, isUserRegistered: (Boolean) -> Unit) {
        jsonObject.put(Constants.UserAttributes.EMAIL, email)
        jsonObject.put(Constants.UserAttributes.NAME, name)
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                userMutableLiveData.postValue(auth.currentUser)
                jsonObject.put(Constants.UserAttributes.UID, auth.currentUser?.uid)
                auth.currentUser?.uid?.let {
                    scope.launch {
                        saveUserUid(it, isUserRegistered = {
                            isUserRegistered(it)
                        })
                    }
                }
                writeUserDataToFirebase(jsonObject)
            } else {
                Toast.makeText(application, task.exception?.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun writeUserDataToFirebase(userData: JSONObject) {
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

    private suspend fun saveUserUid(uid: String, isUserRegistered: (Boolean) -> Unit) {
        application.prefsDataStore.edit { preferences ->
            preferences[PreferenceKeys.uid] = uid
            isUserRegistered(true)
        }
    }

    fun getUserUid(): Flow<String> {
        return application.prefsDataStore.data
            .map { preferences ->
                preferences[PreferenceKeys.uid] ?: ""
            }
    }

    fun login(
        email: String,
        password: String,
        scope: CoroutineScope,
        isUserRegistered: (Boolean) -> Unit,
    ) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                userMutableLiveData.postValue(auth.currentUser)
                auth.currentUser?.uid?.let {
                    scope.launch {
                        saveUserUid(it, isUserRegistered = {
                            isUserRegistered(it)
                        })
                    }
                }
            } else {
                Toast.makeText(application, task.exception?.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun logout(scope: CoroutineScope, loggedOut: () -> Unit) {
        auth.signOut()
        scope.launch {
            application.prefsDataStore.edit {
                it.clear()
                loggedOut()
            }
        }
    }
}