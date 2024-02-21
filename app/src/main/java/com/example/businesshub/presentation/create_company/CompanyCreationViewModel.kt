package com.example.businesshub.presentation.create_company

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.businesshub.data.data_source.DTO.CompanyDTO
import com.example.businesshub.data.data_source.DTO.FirebaseCompanyDTO
import com.example.businesshub.domain.repository.CompanyApiRepository
import com.example.businesshub.domain.repository.FirebaseStorageRepository
import com.example.businesshub.domain.repository.FirebaseUserRepository
import com.example.businesshub.domain.repository.FirestoreCompanyRepository
import com.example.businesshub.domain.repository.FirestoreUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.GregorianCalendar
import javax.inject.Inject

@HiltViewModel
class CompanyCreationViewModel @Inject constructor(
    private val firestoreCompanyRepository: FirestoreCompanyRepository,
    private val firebaseStorageRepository: FirebaseStorageRepository,
    private val firestoreUserRepository: FirestoreUserRepository,
    private val firebaseUserRepository: FirebaseUserRepository,
) : ViewModel() {

    private val _name: MutableStateFlow<String?> = MutableStateFlow(null)
    val name: StateFlow<String?> = _name
    private val _addr: MutableStateFlow<String?> = MutableStateFlow(null)
    val addr: StateFlow<String?> = _addr
    private val _desc: MutableStateFlow<String?> = MutableStateFlow(null)
    val desc: StateFlow<String?> = _desc
    private val _ogrn: MutableStateFlow<String?> = MutableStateFlow(null)
    val ogrn: StateFlow<String?> = _ogrn
    private val _inn: MutableStateFlow<String?> = MutableStateFlow(null)
    val inn: StateFlow<String?> = _inn
    private val _kpp: MutableStateFlow<String?> = MutableStateFlow(null)
    val kpp: StateFlow<String?> = _kpp
    private val _pictureUri: MutableStateFlow<Uri?> = MutableStateFlow(null)
    val pictureUri: StateFlow<Uri?> = _pictureUri
    private val _date: MutableStateFlow<GregorianCalendar?> = MutableStateFlow(null)
    val date: StateFlow<GregorianCalendar?> = _date

    private val _result: MutableSharedFlow<Boolean> = MutableSharedFlow()
    val result = _result.asSharedFlow()


    fun setName(name: String) {
        _name.update { name }
    }

    fun setDesc(desc: String) {
        _desc.update { desc }
    }

    fun setAddr(addr: String) {
        _addr.update { addr }
    }

    fun setOgrp(ogrp: String) {
        _ogrn.update { ogrp }
    }

    fun setInn(inn: String) {
        _inn.update { inn }
    }

    fun setKpp(kpp: String) {
        _kpp.update { kpp }
    }

    fun setDate(date: Long) {
        _date.update {
            val calendar = GregorianCalendar()
            calendar.timeInMillis = date
            calendar
        }
    }

    fun createCompany() {
        viewModelScope.launch(Dispatchers.IO) {

            val user = firebaseUserRepository.getCurrentUser()!!
            Log.w("companyCreation",_name.value?:"no name")
            val company = CompanyDTO(
                _name.value!!,
                _desc.value!!,
                _addr.value!!,
                _ogrn.value!!,
                _inn.value!!,
                _kpp.value!!,
            )
            Log.d("companyCreation",company.toString())
            val usersCompany = FirebaseCompanyDTO(
                user.uid,
                _name.value!!,
                "Владелец",
            )
            Log.d("companyCreation",usersCompany.toString())
            Log.d("companyCreation",_pictureUri.value?.toString()?:"")
            val creationJob = launch {
                launch {
                    _pictureUri.value?.let {
                        firebaseStorageRepository.uploadCompanyProfilePicture(
                            user.uid,
                            it
                        ).await()
                    }

                }
                launch {
                    firestoreCompanyRepository.createCompany(user.uid, company).await()
                }
                launch {
                    firestoreUserRepository.addCompanyToUser(user.uid, user.uid, usersCompany)
                        .await()
                }

            }
            creationJob.join()
            if (creationJob.isCompleted) {
                _result.emit(true)
            } else {
                _result.emit(false)
            }
        }
    }

    fun setPictureUri(uri: Uri?) {
        _pictureUri.update { uri }
    }

}