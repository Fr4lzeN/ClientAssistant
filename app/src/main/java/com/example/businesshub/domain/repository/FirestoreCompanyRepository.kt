package com.example.businesshub.domain.repository

import com.example.businesshub.data.data_source.DTO.CompanyDTO
import kotlinx.coroutines.Deferred

interface FirestoreCompanyRepository {

    suspend fun createCompany(ownerUid: String, company: CompanyDTO): Deferred<Void>

}