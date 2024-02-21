package com.example.businesshub.data.repository

import android.net.Uri
import com.example.businesshub.domain.repository.FirebaseStorageRepository
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.tasks.asDeferred

class FirebaseStorageRepositoryImpl(
    private val storage: StorageReference
): FirebaseStorageRepository {

    override suspend fun uploadProfilePicture(uid: String, pictureUri: Uri): Deferred<UploadTask.TaskSnapshot> {
        return storage.child("profilePicture").child(uid).putFile(pictureUri).asDeferred()
    }

    override suspend fun uploadCompanyProfilePicture(
        companyUid: String,
        pictureUri: Uri
    ): Deferred<UploadTask.TaskSnapshot> {
        return storage.child("company").child(companyUid).child("profile").putFile(pictureUri).asDeferred()
    }
}