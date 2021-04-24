package com.httpconnection.httpconnectionV2.models

data class GetResponse(
    val result: Result?,
    val executorException: Exception?,
    val responseCode: Int? = null,
    val responseMessage: String? = null
)
