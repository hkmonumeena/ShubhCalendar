package com.example.kotlinelearn.roomdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.shubhcalendar.roomdb.*
import com.shubhcalendar.ui.calendar.DataShowFestival

@Database(entities = [ModelDb::class, DbFestivals::class, User::class], version =4, exportSchema = false)
abstract class AppDatabase :RoomDatabase(){
    
    abstract fun addKundali():UserDao
    abstract fun getFestivalsDb(): GetFestivalsDao
    abstract fun getTest(): TestDao

    companion object {
        private var INSTANCE: AppDatabase? = null
        fun getAppDatabase(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java, "room-kotlin-database"
                                               ).build()
            }
            return INSTANCE
        }
        
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}