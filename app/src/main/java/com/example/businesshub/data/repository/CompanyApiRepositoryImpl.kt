package com.example.businesshub.data.repository

import com.example.businesshub.data.data_source.CompanyApi
import com.example.businesshub.data.data_source.DTO.CompanyDTO
import com.example.businesshub.domain.model.Company
import com.example.businesshub.domain.repository.CompanyApiRepository
import retrofit2.Response

class CompanyApiRepositoryImpl(
    private val companyApi: CompanyApi
) : CompanyApiRepository{

    override suspend fun createCompany(token: String, company: Company): Response<CompanyDTO> {
        return companyApi.createCompany(token, company)
    }

    override suspend fun updateCompany(company: Company):Response<CompanyDTO> {
        return companyApi.updateCompany(company)
    }

    override suspend fun getCompany(companyId: String): Response<CompanyDTO> {
        return companyApi.getCompany(companyId)
    }

    override suspend fun deleteCompany(companyId: String) {
        companyApi.deleteCompany(companyId)
    }
}