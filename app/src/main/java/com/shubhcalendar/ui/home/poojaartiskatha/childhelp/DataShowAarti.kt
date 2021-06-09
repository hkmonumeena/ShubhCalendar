package com.shubhcalendar.ui.home.poojaartiskatha.childhelp

data class DataShowAarti(
    val `data`: List<Data?>?,
    val result: String?
) {
    data class Data(
        val `file`: String?,
        val id: String?,
        val path: String?,
        val puja_id: String?,
        val title: String?
    )
}