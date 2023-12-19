package com.example.businesshub.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.businesshub.data.Constants
import com.example.businesshub.data.data_source.DTO.UserDTO
import com.example.businesshub.domain.model.User
import com.example.businesshub.domain.repository.UserApiRepository
import com.example.businesshub.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    val userRepository: UserRepository,
    val userApiRepository: UserApiRepository
) : ViewModel() {

    private val _isSigned: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val isSigned: SharedFlow<Boolean?> = _isSigned

    private val _userData: MutableStateFlow<User?> = MutableStateFlow(null)
    val userData: SharedFlow<User?> = _userData

    fun getUser() {
        viewModelScope.launch(Dispatchers.IO) {
            val user = userRepository.getLastUser()
            if (user != null) {
                if (System.currentTimeMillis() - user.lastSignIn <= Constants.reSignTime) {
                    _userData.update { user }
                    _isSigned.update { true }
                    return@launch
                }else{
                    val response = userApiRepository.signIn(user.username,user.password)
                    if (response.isSuccessful){
                        userRepository.updateUser(user)
                        _userData.update { user }
                        _isSigned.update { true }
                        return@launch
                    }
                }
            }
            _isSigned.update { false }
        }
    }

    fun signUp(username: String, password: String, email: String){
        viewModelScope.launch(Dispatchers.IO) {
            val response = userApiRepository.signUp(UserDTO(username, password, email))
            if (response.isSuccessful){
                val user = User(response.body()!!.objectId,username, password, email)
                userRepository.insertUser(user)
                _userData.update { user }
                _isSigned.update { true }
                return@launch
            }else{
                _isSigned.update { false }
            }
        }
    }

    fun signIn(username: String, password: String){
        viewModelScope.launch {
            val response = userApiRepository.signIn(username, password)
            if (response.isSuccessful){
                val user = User(response.body()!!.objectId,username,password,"123")
                userRepository.insertUser(user)
                _userData.update { user }
                _isSigned.update { true }
                return@launch
            }else{
                _isSigned.update { false }
            }
        }
    }
}

