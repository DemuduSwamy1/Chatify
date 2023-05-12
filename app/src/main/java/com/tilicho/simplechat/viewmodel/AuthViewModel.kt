package com.tilicho.simplechat.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.tilicho.simplechat.data.User
import com.tilicho.simplechat.repository.AuthenticationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

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

    suspend fun register(
        email: String,
        password: String,
        name: String,
        lifecycleOwner: LifecycleOwner,
        scope: CoroutineScope
    ) {

        repository.registerUser(email, password, name)

        userData.observe(lifecycleOwner) {
            scope.launch {
                repository.saveUserUid(it.uid)
            }
        }
    }

    fun checkEmailExists(email: String): Boolean {
        return repository.checkEmailExistsOrNotAndCreateAccount(email)
    }

    fun getUidFromPreferences(): Flow<String> {
        return repository.getUserUid()
    }
}