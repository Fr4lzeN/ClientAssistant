package com.example.businesshub.domain.repository

import com.example.businesshub.data.data_source.DTO.CurrentUserDTO
import com.example.businesshub.data.data_source.DTO.GetUserDTO
import com.example.businesshub.data.data_source.DTO.SignUpDTO
import com.example.businesshub.data.data_source.DTO.UserDTO
import retrofit2.Response

interface UserApiRepository {


   suspend fun signUp(user: UserDTO): Response<SignUpDTO>

   suspend fun signIn(username: String, password: String) : Response<CurrentUserDTO>

   suspend fun getUser(userId: String) : Response<GetUserDTO>

   suspend fun logOut(token: String)

}