package com.example.businesshub.data.repository

import com.example.businesshub.data.data_source.DTO.CompanyDTO
import com.example.businesshub.domain.repository.FirestoreCompanyRepository
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.tasks.asDeferred

class FirestoreCompanyRepositoryImpl(
    private val firestore: CollectionReference
) : FirestoreCompanyRepository  {

    override suspend fun createCompany(ownerUid: String, company: CompanyDTO): Deferred<Void> {
        return firestore.document(ownerUid).collection("companyInfo").document("info").set(company).asDeferred()
    }


}