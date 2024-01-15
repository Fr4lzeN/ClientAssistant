package com.example.businesshub.domain.repository

import com.example.businesshub.data.data_source.DTO.CompanyDTO
import com.example.businesshub.domain.model.Company
import retrofit2.Response

interface CompanyApiRepository {

    suspend fun createCompany(token: String, company: Company): Response<CompanyDTO>

    suspend fun updateCompany(company: Company): Response<CompanyDTO>

    suspend fun getCompany(companyId: String): Response<CompanyDTO>

    suspend fun deleteCompany(companyId: String)

}