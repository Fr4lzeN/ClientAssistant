package com.example.businesshub.domain.repository

import android.net.Uri
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.Deferred

interface FirebaseStorageRepository {

    suspend fun uploadProfilePicture(uid:String, pictureUri: Uri): Deferred<UploadTask.TaskSnapshot>

}