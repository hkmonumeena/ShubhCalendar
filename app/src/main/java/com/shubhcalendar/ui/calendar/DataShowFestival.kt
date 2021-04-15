package com.shubhcalendar.ui.calendar

data class DataShowFestival(
    val result: String?,
    val `data`: List<Data?>?
) {
    data class Data(
        val id: String?,
        val title: String?,
        val states: String?,
        val date: String?,
        val month: String?,
        val year: String?,
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