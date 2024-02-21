package com.example.businesshub.domain.repository

import com.example.businesshub.data.data_source.DTO.FirebaseCompanyDTO
import com.example.businesshub.data.data_source.DTO.PersonDTO
import com.example.businesshub.domain.model.Person
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.Deferred

interface FirestoreUserRepository {

    suspend fun getProfileInfo(uid: String): Deferred<DocumentSnapshot>

    suspend fun setProfileInfo(uid:String, person: PersonDTO): Deferred<Void>

    suspend fun addCompanyToUser(uid:String, companyUid:String, company: FirebaseCompanyDTO): Deferred<Void>

    suspend fun getUsersCompanies(uid: String, listener: (snapshot: QuerySnapshot?, e: FirebaseFirestoreException?) -> Unit): ListenerRegistration

}