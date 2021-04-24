package com.shubhcalendar.ui.home.panchangmuhurat.childfragments

data class DataShowPanchang(
    val `data`: Data?,
    val result: String?
) {
    data class Data(
        val abhijit: String?,
        val date: String?,
        val dur_muhurtam: String?,
        val gulikaikal: String?,
        val id: String?,
        val karana: String?,
        val moonrise: String?,
        val moonset: String?,
        val nakshatra: String?,
        val paksha: String?,
        val path: String?,
        val rahukal: String?,
        val sunrise: String?,
        val sunset: String?,
        val tithi: String?,
        val vikram_samvat: String?,
        val weekday: String?,
        val yamaganda: String?,
        val yoga: String?
    )
}