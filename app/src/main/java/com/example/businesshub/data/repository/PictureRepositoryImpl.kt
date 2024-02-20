package com.example.businesshub.data.repository

import android.net.Uri
import com.example.businesshub.domain.repository.PictureRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

class PictureRepositoryImpl(
    private val reference: StorageReference
) : PictureRepository {
    override fun uploadPicture(pictureUri: Uri, path:String): UploadTask {
        return reference.child(path).putFile(pictureUri)
    }

    override fun downloadPicture(path: String): Task<Uri> {
        return reference.child(path).downloadUrl
    }
}