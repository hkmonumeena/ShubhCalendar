package com.shubhcalendar.ui.home.poojaartiskatha.childhelp

data class DataShowAllPooja(
    val `data`: List<Data?>? = null,
    val result: String? = null
) {
    data class Data(
        val id: String? = null,
        val image: String? = null,
        val path: String? = null,
        val title: String? = null
    )
}