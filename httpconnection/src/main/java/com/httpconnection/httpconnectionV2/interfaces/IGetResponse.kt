package com.httpconnection.httpconnectionV2.interfaces

import com.httpconnection.httpconnectionV2.models.Exception

interface IGetResponse {
    fun onResponse(response:String?)
    fun onError(error:Exception?)
}

