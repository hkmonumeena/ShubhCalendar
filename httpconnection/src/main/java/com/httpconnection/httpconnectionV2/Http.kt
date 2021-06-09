package com.httpconnection.httpconnectionV2

import android.util.Base64
import com.google.gson.Gson
import com.httpconnection.httpconnectionV2.requestMethods.Execute
import com.httpconnection.httpconnectionV2.requestMethods.Get
import com.httpconnection.httpconnectionV2.requestMethods.Upload
import org.json.JSONObject

class Http {
    companion object {
        val post = Post(null)
        val get = Get()
        val upload = Upload()
    }

    inline fun <reified T : Any> createModelFromClass(json: String): T {
        return Gson().fromJson(json, T::class.java)
    }
    data class Post(
        private var mUrl: String?,
        private var connectTimeout: Int? = 15000,
        private var readTimeout: Int? = 30000,
        private var contentType2: String? = "application/x-www-form-urlencoded",
        private var jsonBody: JSONObject = JSONObject(),
        private var bodyparams: JSONObject = JSONObject(),
        private var getHeaderParameter: MutableMap<String, String> = mutableMapOf(),
        private var checkBodytype: String? = null
    ) {

        fun connectTimeout(time: Int) = apply {
            connectTimeout = time
        }


        fun readTimeout(time: Int) = apply {
            readTimeout = time
        }

        fun setContentType(setTypeOfHttp: String?) = apply {
            contentType2 = setTypeOfHttp
        }

        fun jsonBody(jsonObject: JSONObject) = apply {
            checkBodytype = "jsonBody"
            jsonBody = jsonObject
        }

        fun authenticatHeader(username: String, password: String) = apply {
            var getAuthenticatHeader: String? = null
            getAuthenticatHeader = username.plus(password)
            val encodeAuth: String?
            val auth = "$username:$password"
            val data = auth.toByteArray()
            encodeAuth = "Basic " + Base64.encodeToString(data, Base64.DEFAULT)
            getHeaderParameter["Authorization"] = encodeAuth
        }

        fun headerParameter(listHeader: MutableMap<String, String>) = apply {
            getHeaderParameter = listHeader
        }

        fun bodyParameter(listBody: MutableMap<String, String>) = apply {
            checkBodytype = "bodyParameter"
            for ((key, value) in listBody) {
                bodyparams.put(key, value)

            }
        }

        fun build() = Execute(
            mUrl,
            connectTimeout,
            readTimeout,
            contentType2,
            jsonBody,
            bodyparams,
            getHeaderParameter,
            checkBodytype
        )

    }
}