package com.example.businesshub.data.repository

import com.example.businesshub.data.data_source.DTO.PersonDTO
import com.example.businesshub.data.data_source.DTO.PictureDTO
import com.example.businesshub.data.data_source.DTO.Result
import com.example.businesshub.data.data_source.PersonApi
import com.example.businesshub.domain.model.Person
import com.example.businesshub.domain.repository.PersonApiRepository
import retrofit2.Response

class PersonApiRepositoryImpl(
    private val personApi: PersonApi,
): PersonApiRepository {

    override suspend fun createPerson(token: String, person: Person): Response<Result<PersonDTO>> {
        return personApi.createPerson(token, person)
    }

    override suspend fun setPicture(token: String, url: String): Response<Result<PictureDTO>> {
        return personApi.setPicture(token, url)
    }

}