package com.example.businesshub.domain.repository

import com.example.businesshub.data.data_source.DTO.PersonDTO
import com.example.businesshub.data.data_source.DTO.PictureDTO
import com.example.businesshub.data.data_source.DTO.Result
import com.example.businesshub.domain.model.Person
import retrofit2.Response

interface PersonApiRepository {

    suspend fun createPerson( token: String, person: Person): Response<Result<PersonDTO>>

    suspend fun setPicture(token: String, url: String): Response<Result<PictureDTO>>

}