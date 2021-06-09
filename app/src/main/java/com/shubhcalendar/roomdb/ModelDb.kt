package com.example.kotlinelearn.roomdatabase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
class ModelDb(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var name: String
)