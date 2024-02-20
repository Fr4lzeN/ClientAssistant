package com.example.businesshub.domain.repository

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Deferred

interface FirebaseUserRepository {

    suspend fun signIn(email:String, password:String): Deferred<AuthResult>
    suspend fun signUp(email: String, password:String): Deferred<AuthResult>
    suspend fun signOut()
    suspend fun getCurrentUser():FirebaseUser?

}