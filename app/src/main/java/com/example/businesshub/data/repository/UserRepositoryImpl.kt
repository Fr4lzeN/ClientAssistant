package com.example.businesshub.data.repository

import com.example.businesshub.data.data_source.UserDAO
import com.example.businesshub.domain.model.User
import com.example.businesshub.domain.repository.UserRepository

class UserRepositoryImpl(
    private val dao: UserDAO
) : UserRepository {

    override suspend fun insertUser(user: User) {
        dao.insertUser(user)
    }

    override suspend fun deleteUser(id: String) {
        dao.deleteUser(id)
    }

    override suspend fun getLastUser(): User? {
        return dao.getLastUser()
    }

    override suspend fun updateUser(user: User) {
        dao.updateUser(user)
    }
}