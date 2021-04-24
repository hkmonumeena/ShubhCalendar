package com.httpconnection.httpconnectionV2.interfaces

import org.json.JSONObject

interface IPost {
    fun url(url: String)
    fun connectTimeout(time: Int =15000)
    fun readTimeout(time: Int =30000)
    fun setContentType(setTypeOfHttp:String="application/x-www-form-urlencoded")
    fun jsonBody(jsonObject: JSONObject)
    fun authenticatHeader(username:String, password:String)
    fun headerParameter(listHeader: MutableMap<String, String>)
    fun bodyParameter(listBody: MutableMap<String, String>)
    fun executeString(iGetResponse: IGetResponse)
}