package com.example.businesshub.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.businesshub.domain.model.User
import javax.inject.Singleton

@Singleton
@Database(
    entities = [User::class],
    version = 1,
    exportSchema = false,
)
abstract class UserDatabase: RoomDatabase() {

    abstract val userDAO : UserDAO

    companion object{
        const val DATABASE_NAME = "user"
    }
}