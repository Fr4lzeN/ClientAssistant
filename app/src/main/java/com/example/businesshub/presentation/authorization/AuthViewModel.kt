package com.example.businesshub.presentation.authorization

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.businesshub.core.AuthState
import com.example.businesshub.data.data_source.DTO.PersonDTO
import com.example.businesshub.domain.model.Person
import com.example.businesshub.domain.repository.FirebaseStorageRepository
import com.example.businesshub.domain.repository.FirebaseUserRepository
import com.example.businesshub.domain.repository.FirestoreUserRepository
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestoreException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.asDeferred
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val firebaseUserRepository: FirebaseUserRepository,
    private val firestoreUserRepository: FirestoreUserRepository,
    private val firebaseStorageRepository: FirebaseStorageRepository,
) : ViewModel() {

    private val _authState: MutableSharedFlow<AuthState> = MutableSharedFlow()
    val authState = _authState.asSharedFlow()

    private val _user: MutableStateFlow<FirebaseUser?> = MutableStateFlow(null)
    val user = _user.asStateFlow()

    private val _exceptionState: MutableSharedFlow<String?> = MutableSharedFlow()
    val exceptionState = _exceptionState.asSharedFlow()

    private val _name: MutableStateFlow<String?> = MutableStateFlow(null)
    val name = _name.asStateFlow()
    private val _surname: MutableStateFlow<String?> = MutableStateFlow(null)
    val surname = _surname.asStateFlow()
    private val _phone: MutableStateFlow<String?> = MutableStateFlow(null)
    val phone = _phone.asStateFlow()
    private val _pictureUri: MutableStateFlow<Uri?> = MutableStateFlow(null)
    val pictureUri = _pictureUri.asStateFlow()

    fun getUser() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val user = firebaseUserRepository.getCurrentUser()
                if (user != null) {
                    checkProfileInfo(user)
                } else {
                    Log.d("user", "failture")
                    updateState(AuthState.FAILURE)
                }
            } catch (e: FirebaseAuthInvalidUserException) {
                _exceptionState.emit(
                    when (e.errorCode) {
                        "ERROR_USER_DISABLED" -> "Учетная запись была отключена"
                        "ERROR_USER_NOT_FOUND" -> "Учетная запись не найдена"
                        "ERROR_USER_TOKEN_EXPIRED" -> "Сессия истекла, войдите в аккаунт снова"
                        else -> "Неизвестная ошибка, попробуйте войти в аккаунт снова"
                    }
                )
            }
        }
    }

    private suspend fun checkProfileInfo(user: FirebaseUser) {
        try {
            if (firestoreUserRepository.getProfileInfo(user.uid).await().exists()) {
                updateState(AuthState.SUCCESS, user)
            } else {
                updateState(AuthState.MINIMAL_SUCCESS, user)
            }
        }catch (e: FirebaseFirestoreException){
            updateState(AuthState.MINIMAL_SUCCESS, user)
        }

    }

    fun signUp(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val user = firebaseUserRepository.signUp(email, password).await().user
                if (user != null) {
                    if (user.email==null){
                        user.updateEmail(email).asDeferred().await()
                    }
                    updateState(AuthState.MINIMAL_SUCCESS, user)
                }
            } catch (e: FirebaseAuthUserCollisionException) {
                _exceptionState.emit( "Пользователь с такой почтой уже существует" )
            } catch (e: FirebaseAuthWeakPasswordException) {
                _exceptionState.emit( "Слишком слабый пароль" )
            }
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val user = firebaseUserRepository.signIn(email, password).await().user
                Log.d("signIn", "sign in ${user?.uid}")
                if (user != null) {
                    checkProfileInfo(user)
                } else {
                    Log.d("signIn", "sign in failure")
                    updateState(AuthState.FAILURE)
                }
            } catch (e: FirebaseAuthException) {
                _exceptionState.emit("Ошибка входа, проверьте почту и пароль")
            }

        }
    }

    private suspend fun updateState(state: AuthState, user: FirebaseUser? = null) {
        Log.d("user", "update ${state}")
        if (state != AuthState.FAILURE) {
            _user.update { user }
        }
        _authState.emit( state )

    }

    fun logOut() {
        viewModelScope.launch(Dispatchers.IO) {
            firebaseUserRepository.signOut()
        }
    }

    fun setName(name: String) {
        _name.update { name }
    }

    fun setSurname(surname: String) {
        _surname.update { surname }
    }

    fun setPhone(phoneNumber: String) {
        _phone.update { phoneNumber }
    }

    fun setPictureUri(uri: Uri?) {
        _pictureUri.update { uri }
    }

    fun uploadPersonInfo() {
        viewModelScope.launch(
            Dispatchers.Default + CoroutineExceptionHandler { _: CoroutineContext, t: Throwable ->
                t.printStackTrace()
                Log.e("upload",t.toString())
                viewModelScope.launch {  _exceptionState.emit("Ошибка при загрузке данных, попробуйте позже" ) }
            },
        ) {
            val user = _user.value!!
            val personInfo = PersonDTO(
                _name.value!!,
                _surname.value!!,
                user.email!!,
                _phone.value!!
            )

            val uploadJob = launch {
                launch(Dispatchers.IO) {
                    firestoreUserRepository.setProfileInfo(user.uid, personInfo).await()
                    Log.d("signUp", "person")
                }
                launch(Dispatchers.IO) {
                    firebaseStorageRepository
                        .uploadProfilePicture(user.uid, _pictureUri.value!!).await()
                    Log.d("signUp", "picture")
                }
            }

            uploadJob.join()
            if (uploadJob.isCompleted) {
                Log.d("signUp", "completed")
                _authState.emit( AuthState.SUCCESS )
            }
        }
    }

}

