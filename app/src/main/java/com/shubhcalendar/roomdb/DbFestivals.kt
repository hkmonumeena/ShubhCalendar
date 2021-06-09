package com.shubhcalendar.roomdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "festivals")
data class DbFestivals(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String?,
    val states: String?,
    val date: String?,
    val month: String?,
    val year: String?,
    val newdate: String?,
    val image: String?,
    val path: String?
)