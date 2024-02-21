package com.example.businesshub.data.repository

import com.example.businesshub.data.data_source.DTO.FirebaseCompanyDTO
import com.example.businesshub.data.data_source.DTO.PersonDTO
import com.example.businesshub.domain.repository.FirestoreUserRepository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.tasks.asDeferred


class FirestoreUserRepositoryImpl(
    private val firestore: CollectionReference
): FirestoreUserRepository {

    override suspend fun getProfileInfo(uid: String): Deferred<DocumentSnapshot> {
        return firestore.document(uid).collection("userInfo").document("info").get().asDeferred()
    }

    override suspend fun setProfileInfo(uid: String, person: PersonDTO): Deferred<Void> {
        return firestore.document(uid).collection("userInfo").document("info").set(person).asDeferred()
    }

    override suspend fun addCompanyToUser(
        uid: String,
        companyUid:String,
        company: FirebaseCompanyDTO
    ): Deferred<Void> {
        return firestore.document(uid).collection("company").document(companyUid).set(company).asDeferred()
    }


    override suspend  fun getUsersCompanies(uid: String, listener: (snapshot: QuerySnapshot?, e: FirebaseFirestoreException?) -> Unit): ListenerRegistration {
        return firestore.document(uid).collection("company").addSnapshotListener(listener)
    }
}