package com.example.businesshub.presentation.create_company

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.businesshub.domain.model.Company
import com.example.businesshub.domain.model.User
import com.example.businesshub.domain.repository.CompanyApiRepository
import com.example.businesshub.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.GregorianCalendar
import javax.inject.Inject

@HiltViewModel
class CreateCompanyViewModel @Inject constructor(
    private val companyApiRepository: CompanyApiRepository,
    private val userRepository: UserRepository,
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
    private val _date: MutableStateFlow<GregorianCalendar?> = MutableStateFlow(null)
    val date: StateFlow<GregorianCalendar?> = _date

    internal var token: String? = null
    internal var user: User? = null

    private val _result: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val result: StateFlow<Boolean?> = _result


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
            val company = Company(
                name.value!!,
                desc.value!!,
                addr.value!!,
                ogrn.value!!.toLong(),
                inn.value!!.toInt(),
                kpp.value!!.toInt(),
                date.value!!.timeInMillis
            )
            val response = companyApiRepository.createCompany(token!!, company)
            if (response.isSuccessful) {
                user!!.companyId = response.body()!!.result.objectId
                Log.d("company", response.body()!!.result.objectId)
                userRepository.updateUser(user!!)
            }
            _result.update { response.isSuccessful }
        }
    }

    fun setToken(token: String?) {
        this.token = token
    }

    fun setUser(user: User) {
        this.user = user
    }


}