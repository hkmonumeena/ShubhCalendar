package com.shubhcalendar.roomdb

import androidx.lifecycle.LiveData
import androidx.room.*
import com.shubhcalendar.roomdb.DbFestivals
import com.shubhcalendar.ui.calendar.DataShowFestival

@Dao
interface GetFestivalsDao {
    @Insert
    fun insert(modelDb: DbFestivals)

    @Update
    fun update(modelDb: DbFestivals)

    @Delete
    fun delete(modelDb: DbFestivals)

    @Query("SELECT * FROM FESTIVALS")
    fun getAllUsers(): LiveData<List<DbFestivals>>

    @Query("SELECT * FROM FESTIVALS WHERE month ==:id AND year ==:year")
    fun getUserWithId(id: String,year:String): LiveData<List<DbFestivals>>
}