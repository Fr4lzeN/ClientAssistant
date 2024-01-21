package com.example.businesshub.data.data_source

import com.example.businesshub.core.Constants
import com.example.businesshub.data.data_source.DTO.CompanyDTO
import com.example.businesshub.domain.model.Company
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface CompanyApi {



    @Headers(
        "X-Parse-Application-Id: ${Constants.applicationId}",
        "X-Parse-REST-API-Key: ${Constants.restApiKey}",
        "Content-Type: application/json",
    )
    @POST("createCompany")
    suspend fun createCompany(@Header("X-Parse-Session-Token")token: String, @Body company : Company): Response<CompanyDTO>

    @Headers(
        "X-Parse-Application-Id: ${Constants.applicationId}",
        "X-Parse-REST-API-Key: ${Constants.restApiKey}",
        "Content-Type: application/json",
    )
    @POST("updateCompany")
    suspend fun updateCompany(company: Company): Response<CompanyDTO>

    @Headers(
        "X-Parse-Application-Id: ${Constants.applicationId}",
        "X-Parse-REST-API-Key: ${Constants.restApiKey}",
        "Content-Type: application/json",
    )
    @GET("getCompany")
    suspend fun getCompany(companyID: String) : Response<CompanyDTO>

    @Headers(
        "X-Parse-Application-Id: ${Constants.applicationId}",
        "X-Parse-REST-API-Key: ${Constants.restApiKey}",
        "Content-Type: application/json",
    )
    @POST("deleteCompany")
    suspend fun deleteCompany(companyID: String)

}