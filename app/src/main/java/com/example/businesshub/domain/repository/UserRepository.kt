package com.example.businesshub.domain.repository

import com.example.businesshub.domain.model.User

interface UserRepository {

    suspend fun insertUser(user: User)

    suspend fun deleteUser(id: String)

    suspend fun getLastUser() : User?

    suspend fun updateUser(user: User)

}