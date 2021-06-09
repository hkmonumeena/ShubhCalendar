package com.shubhcalendar.roomdb

import androidx.lifecycle.LiveData
import androidx.room.*
import com.shubhcalendar.roomdb.DbFestivals
import com.shubhcalendar.ui.calendar.DataShowFestival

@Dao
interface TestDao {
    @Insert
    fun insert(modelDb: User)

    @Update
    fun update(modelDb: User)

    @Delete
    fun delete(modelDb: User)

    @Query("SELECT * FROM test")
    fun getAllUsers(): LiveData<List<User>>

    @Query("SELECT * FROM test WHERE id ==:id")
    fun getUserWithId(id: Int): LiveData<List<User>>
}