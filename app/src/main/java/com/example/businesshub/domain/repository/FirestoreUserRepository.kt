package com.example.businesshub.domain.repository

import com.example.businesshub.data.data_source.DTO.PersonDTO
import com.example.businesshub.domain.model.Person
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.Deferred

interface FirestoreUserRepository {

    suspend fun getProfileInfo(uid: String): Deferred<DocumentSnapshot>

    suspend fun setProfileInfo(uid:String, person: PersonDTO): Deferred<Void>

}