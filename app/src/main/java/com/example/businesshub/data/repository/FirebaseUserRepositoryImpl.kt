package com.example.businesshub.data.repository

import android.provider.ContactsContract
import com.example.businesshub.domain.repository.FirebaseUserRepository
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.tasks.asDeferred

class FirebaseUserRepositoryImpl(
    val auth : FirebaseAuth
): FirebaseUserRepository {



    override suspend fun signIn(
        email: String,
        password: String
    ): Deferred<AuthResult> {
        return auth.signInWithEmailAndPassword(email,password).asDeferred()
    }

    override suspend fun signUp(
        email: String,
        password: String
    ): Deferred<AuthResult> {
        return auth.createUserWithEmailAndPassword(email,password).asDeferred()
    }

    override suspend fun signOut() {
        auth.signOut()
    }

    override suspend fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }
}