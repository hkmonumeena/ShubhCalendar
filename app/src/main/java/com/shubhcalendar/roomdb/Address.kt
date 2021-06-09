package com.shubhcalendar.roomdb

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class Address(
    val street: String?,
    val state: String?,
    val city: String?,
    val postCode: Int
)

@Entity(tableName = "test")
data class User(
    @PrimaryKey val id: Int,
    val firstName: String?,
    @Embedded val address: Address?
)
