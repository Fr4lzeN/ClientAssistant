package com.example.businesshub.data.repository

import com.example.businesshub.data.data_source.DTO.PersonDTO
import com.example.businesshub.domain.model.Person
import com.example.businesshub.domain.repository.FirestoreUserRepository
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.tasks.asDeferred


class FirestoreUserRepositoryImpl(
    val firestore: FirebaseFirestore
): FirestoreUserRepository {

    override suspend fun getProfileInfo(uid: String): Deferred<DocumentSnapshot> {
        return firestore.collection("users").document(uid).get().asDeferred()
    }

    override suspend fun setProfileInfo(uid: String, person: PersonDTO): Deferred<Void> {
        return firestore.collection("users").document(uid).set(person).asDeferred()
    }
}