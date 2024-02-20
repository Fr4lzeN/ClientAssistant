package com.example.businesshub.data.data_source

import com.example.businesshub.core.Constants
import com.example.businesshub.data.data_source.DTO.CompanyDTO
import com.example.businesshub.data.data_source.DTO.PersonDTO
import com.example.businesshub.data.data_source.DTO.PictureDTO
import com.example.businesshub.data.data_source.DTO.Result
import com.example.businesshub.domain.model.Company
import com.example.businesshub.domain.model.Person
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface PersonApi {

    @Headers(
        "X-Parse-Application-Id: ${Constants.applicationId}",
        "X-Parse-REST-API-Key: ${Constants.restApiKey}",
        "Content-Type: application/json",
    )
    @POST("create_person")
    suspend fun createPerson(
        @Header("X-Parse-Session-Token") token: String,
        @Body person: Person
    ): Response<Result<PersonDTO>>


    @Headers(
        "X-Parse-Application-Id: ${Constants.applicationId}",
        "X-Parse-REST-API-Key: ${Constants.restApiKey}",
        "Content-Type: application/json",
    )
    @POST("set_picture")
    suspend fun setPicture(
        @Header("X-Parse-Session-Token") token: String,
        @Body url: String
    ): Response<Result<PictureDTO>>

}