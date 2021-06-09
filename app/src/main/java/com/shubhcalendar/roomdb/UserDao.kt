package com.example.kotlinelearn.roomdatabase

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {
    @Insert
    fun insert(modelDb : ModelDb)
    @Update
    fun update (modelDb : ModelDb)
    @Delete
    fun delete(modelDb : ModelDb)
    @Query("SELECT * FROM USERS")
    fun getAllUsers():LiveData<List<ModelDb>>
    @Query("SELECT * FROM USERS WHERE id ==:id")
    fun getUserWithId(id:Int):ModelDb
}