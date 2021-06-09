package com.shubhcalendar.ui.calendar

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class DataShowFestival  constructor(
 val id :Int =0,
    val result: String?="",
    @Embedded val data: List<Data?>?= listOf()
) {
    data class Data(
      val id: Int,
        val title: String?,
        val states: String?,
        val date: String?,
        val month: String?,
        val year: String?,
        val newdate: String?,
        val image: String?,
   val all_falst: List<AllFalst?>?,
        val path: String?
    ) {

        data class AllFalst(
            val id: String?,
            val title: String?,
            val states: String?,
            val date: String?,
            val month: String?,
            val year: String?
        )
    }
}
