package com.shubhcalendar.ui.home.festival

data class DataShowFestival(
    val `data`: List<Data?>? = null,
    val result: String? = null
) {
    data class Data(
        val about: String? = null,
        val all_falst: List<AllFalst?>? = null,
        val date: String? = null,
        val day: String? = null,
        val devotional_songs: String? = null,
        val hindu_calender_date: String? = null,
        val id: String? = null,
        val month: String? = null,
        val muhurat: String? = null,
        val observances: String? = null,
        val path: String? = null,
        val religion: String? = null,
        val title: String? = null,
        val year: String? = null
    ) {
        data class AllFalst(
            val about: String? = null,
            val date: String? = null,
            val day: String? = null,
            val devotional_songs: String? = null,
            val hindu_calender_date: String? = null,
            val id: String? = null,
            val month: String? = null,
            val muhurat: String? = null,
            val observances: String? = null,
            val religion: String? = null,
            val title: String? = null,
            val year: String? = null
        )
    }
}