package com.example.businesshub.domain.repository

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.UploadTask

interface PictureRepository {


    fun uploadPicture(pictureUri: Uri, path:String): UploadTask

    fun downloadPicture(path: String): Task<Uri>


}