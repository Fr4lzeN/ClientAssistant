package com.example.businesshub.data.data_source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.businesshub.domain.model.User

@Dao
interface UserDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)

    @Query("DELETE FROM user WHERE objectId = :id")
    fun deleteUser(id: String)

    @Query("SELECT * FROM user ORDER BY lastSignIn DESC LIMIT 1")
    fun getLastUser() : User?

    @Update
    fun updateUser(user: User)

}