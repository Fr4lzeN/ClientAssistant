package com.example.businesshub.presentation.create_person

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.businesshub.domain.model.Person
import com.example.businesshub.domain.model.User
import com.example.businesshub.domain.repository.PersonApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonViewModel @Inject constructor(
    private val personApi: PersonApiRepository
) : ViewModel() {

    private val _name: MutableStateFlow<String?> = MutableStateFlow(null)
    val name: StateFlow<String?> = _name
    private val _surname: MutableStateFlow<String?> = MutableStateFlow(null)
    val surname: StateFlow<String?> = _surname
    private val _email: MutableStateFlow<String?> = MutableStateFlow(null)
    val email: StateFlow<String?> = _email
    private val _phone: MutableStateFlow<String?> = MutableStateFlow(null)
    val phone: StateFlow<String?> = _phone

    private val _result: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val result: StateFlow<Boolean?> = _result

    var token: String? = null
    var user: User? = null

    fun setName(name: String) {
        _name.update { name }
    }

    fun setSurname(surname: String) {
        _surname.update { surname }
    }

    fun setEmail(email: String) {
        _email.update { email }
    }

    fun setPhone(phone: String) {
        _phone.update { phone }
    }

    fun createPerson() {
        viewModelScope.launch {
            val response = personApi.createPerson(
                token!!, Person(
                    name.value!!,
                    surname.value!!,
                    email.value!!,
                    phone.value
                )
            )
            if (response.isSuccessful) {
                user?.personId = response.body()!!.result.objectId
            }
            _result.update { response.isSuccessful }
        }
    }
}