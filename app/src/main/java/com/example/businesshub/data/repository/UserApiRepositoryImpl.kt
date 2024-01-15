package com.example.businesshub.data.repository

import com.example.businesshub.data.data_source.DTO.CurrentUserDTO
import com.example.businesshub.data.data_source.DTO.GetUserDTO
import com.example.businesshub.data.data_source.DTO.SignUpDTO
import com.example.businesshub.data.data_source.DTO.UserDTO
import com.example.businesshub.data.data_source.UserApi
import com.example.businesshub.domain.repository.UserApiRepository
import retrofit2.Response

class UserApiRepositoryImpl(
    private val userApi: UserApi
):UserApiRepository {
    override suspend fun signUp(user: UserDTO): Response<SignUpDTO> {
        return userApi.signUp(user)
    }

    override suspend fun signIn(username: String, password: String): Response<CurrentUserDTO> {
        return userApi.signIn(username, password)
    }

    override suspend fun getUser(userId: String): Response<GetUserDTO> {
        return userApi.getUser(userId)
    }

    override suspend fun logOut(token: String) {
        userApi.logOut(token)
    }
}