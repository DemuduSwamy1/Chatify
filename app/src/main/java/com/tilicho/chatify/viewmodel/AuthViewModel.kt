package com.tilicho.chatify.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.tilicho.chatify.repository.AuthenticationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AuthenticationRepository = AuthenticationRepository(application)
    private val userData: MutableLiveData<FirebaseUser> = repository.getFirebaseUserMutableLiveData
    private val userRegistrationStatus: Boolean = repository.getRegistrationStatusMutableLiveData

    val getUserData: MutableLiveData<FirebaseUser>
        get() {
            return userData
        }

    val getUserRegistrationStatus: Boolean
        get() {
            return userRegistrationStatus
        }

    fun register(
        email: String,
        password: String,
        name: String,
        lifecycleOwner: LifecycleOwner,
        scope: CoroutineScope,
        isUserRegistered: (Boolean) -> Unit
    ) {
        repository.registerUser(email, password, name, scope, isUserRegistered = {
            isUserRegistered(it)
        })
    }

    fun checkEmailExists(email: String): Boolean {
        return repository.checkEmailExistsOrNotAndCreateAccount(email)
    }

    fun getUidFromPreferences(): Flow<String> {
        return repository.getUserUid()
    }

    fun login(
        email: String,
        password: String,
        scope: CoroutineScope,
        loggedIn: (Boolean) -> Unit
    ) {
        repository.login(email, password, scope, isUserRegistered = {
            loggedIn(it)
        })
    }

    fun logout(scope: CoroutineScope, loggedOut: () -> Unit) {
        repository.logout(scope) {
            loggedOut()
        }
    }
}