package com.example.businesshub.domain.model

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.businesshub.data.data_source.DTO.CurrentUserDTO
import com.example.businesshub.data.data_source.DTO.GetUserDTO
import com.example.businesshub.data.data_source.DTO.SignUpDTO
import com.example.businesshub.data.data_source.DTO.UserDTO
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class User(
    @PrimaryKey
    val objectId: String,
    val username: String,
    val password: String,
    var personId: String?=null,
    val lastSignIn: Long = System.currentTimeMillis(),
    var companyId: String? = null,
): Parcelable