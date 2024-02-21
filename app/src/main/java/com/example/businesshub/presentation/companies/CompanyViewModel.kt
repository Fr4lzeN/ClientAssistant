package com.example.businesshub.presentation.companies

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.businesshub.data.data_source.DTO.FirebaseCompanyDTO
import com.example.businesshub.domain.model.Company
import com.example.businesshub.domain.repository.FirebaseUserRepository
import com.example.businesshub.domain.repository.FirestoreUserRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.reflect.Array
import javax.inject.Inject

@HiltViewModel
class CompanyViewModel @Inject constructor(
    private val userRepository: FirestoreUserRepository,
    private val firebaseUserRepository: FirebaseUserRepository,
) : ViewModel() {

    private val _companyList = MutableStateFlow<ArrayList<FirebaseCompanyDTO>?>(null)
    val companyList = _companyList.asStateFlow()

    private var companyListener: ListenerRegistration? = null

    fun getCompanyList() {
        viewModelScope.launch(Dispatchers.IO) {
            companyListener =
                userRepository.getUsersCompanies(firebaseUserRepository.getCurrentUser()?.uid!!)
                { snapshot: QuerySnapshot?, e: FirebaseFirestoreException? ->
                    if (e != null) {
                        Log.w("company", e.toString())
                    }
                    if (snapshot != null) {
                        for (document in snapshot.documentChanges) {
                            val company = document.document.toObject<FirebaseCompanyDTO>()
                            when (document.type) {
                                DocumentChange.Type.ADDED -> {
                                    addDocument(company)
                                }

                                DocumentChange.Type.MODIFIED -> {
                                    updateCompany(company)
                                }

                                DocumentChange.Type.REMOVED -> {
                                    removeCompany(company)
                                }
                            }
                        }
                    }
                }
        }
    }

    private fun removeCompany(company: FirebaseCompanyDTO) {
        _companyList.update {
            val list = if (it == null) ArrayList<FirebaseCompanyDTO>()
            else ArrayList(it.toList())
            list.remove(company)
            list
        }
    }

    private fun updateCompany(company: FirebaseCompanyDTO) {
        _companyList.update {
            val list = if (it == null) ArrayList<FirebaseCompanyDTO>()
            else ArrayList(
            )
            for (i in list.indices) {
                if (list[i].id == company.id) {
                    list[i] = company
                    break
                }
            }
            list
        }
    }

    private fun addDocument(company: FirebaseCompanyDTO) {
        _companyList.update {
            val list = if (it == null) ArrayList<FirebaseCompanyDTO>()
            else ArrayList(it.toList())
            list.add(company)
            list
        }
    }

    override fun onCleared() {
        super.onCleared()
        companyListener?.remove()
    }

}