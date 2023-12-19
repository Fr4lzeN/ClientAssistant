package com.example.businesshub.domain.model

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.businesshub.data.data_source.DTO.UserDTO
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class User(
    @PrimaryKey
    val objectId: String,
    val username: String,
    val password: String,
    val email: String,
    val lastSignIn: Long = System.currentTimeMillis(),
): Parcelable{


    fun toUserDTO(): UserDTO{
        return UserDTO(username,password, email)
    }


}

