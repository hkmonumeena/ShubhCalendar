package com.httpconnection.httpconnectionV2.requestMethods

import android.app.Activity
import android.util.Log
import com.google.gson.Gson
import com.httpconnection.httpconnectionV2.interfaces.IGetResponse
import com.httpconnection.httpconnectionV2.models.Exception
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.*
import java.net.SocketTimeoutException
import java.net.URL
import java.net.URLEncoder
import javax.net.ssl.HttpsURLConnection

class Execute constructor(
   private var mUrl: String? = null,
   private   var connectTimeout: Int? = 15000,
   private  var readTimeout: Int? = 30000,
   private   var contentType2: String? = "application/x-www-form-urlencoded",
   private  var jsonBody: JSONObject = JSONObject(),
   private  var bodyparams: JSONObject = JSONObject(),
   private  var getHeaderParameter: MutableMap<String, String> = mutableMapOf(),
   private var checkBodytype:String?=null
) : Activity() {



    data class Builder(
        var constUrl: String? = null
    ) {
        fun url(url: String?) = apply {

            this.constUrl = url
        }


        fun getResult(iGetResponse: IGetResponse) {
            Execute().executeString(object : IGetResponse {
                override fun onResponse(response: String?) {
                    iGetResponse.onResponse(response)
                }

                override fun onError(error: Exception?) {
                    iGetResponse.onError(error)
                }
            })

        }

        fun build() = Execute(constUrl)

    }


     fun executeString(iGetResponse: IGetResponse) {
        var httpURlConnection: HttpsURLConnection? = null
        var writer: BufferedWriter? = null
        var os: OutputStream?
        GlobalScope.launch(Dispatchers.IO) {
            try {
                httpURlConnection = URL(mUrl).openConnection() as HttpsURLConnection
                httpURlConnection?.requestMethod = "POST"
                httpURlConnection?.setRequestProperty("Connection", "Keep-Alive")
                httpURlConnection?.setRequestProperty("Cache-Control", "no-cache")
                httpURlConnection?.setRequestProperty("Content-Type", contentType2)
                httpURlConnection?.setRequestProperty("Accept", "*/*")
                for ((key, value) in getHeaderParameter.entries) {
                    httpURlConnection?.setRequestProperty(key, value)
                }
                httpURlConnection?.readTimeout = readTimeout!!
                httpURlConnection?.connectTimeout = connectTimeout!!
                httpURlConnection?.doInput = true
                httpURlConnection?.doOutput = true
                os = httpURlConnection?.outputStream
                if (checkBodytype == "bodyParameter") {
                    writer = BufferedWriter(OutputStreamWriter(os, "UTF-8"))
                    writer?.write(encodeParams(bodyparams))
                    writer?.flush()
                    writer?.close()
                    os?.close()
                } else if (checkBodytype == "jsonBody") {
                    writer = BufferedWriter(OutputStreamWriter(os, "UTF-8"))
                    writer?.write(jsonBody.toString())
                    writer?.flush()
                    writer?.close()
                    os?.close()
                }
                httpURlConnection?.connect()
                val data = httpURlConnection?.inputStream?.bufferedReader()?.readText()
                httpURlConnection?.disconnect()
                if (httpURlConnection?.responseCode == 200) {
                    runOnUiThread {
                        iGetResponse.onResponse(data)
                    }
                }else {
                    val executorException = Exception(
                        httpURlConnection?.responseCode, data,
                        httpURlConnection?.responseMessage
                    )

                    runOnUiThread { iGetResponse.onError(executorException) }
                }
            } catch (se: SocketTimeoutException) {
                val executorException = Exception(
                    1003, se.localizedMessage,
                    "SocketTimeoutException $se"
                )

                runOnUiThread { iGetResponse.onError(executorException) }

            } catch (e: IOException) {
                if (httpURlConnection?.errorStream == null) {
                    val executorException = Exception(
                        1004, "Path of url was not correct",
                        "IOException $e"
                    )
                    runOnUiThread { iGetResponse.onError(executorException) }
                } else {
                    val `in` = InputStreamReader(httpURlConnection?.errorStream)
                    val stringBuilder = StringBuilder()
                    val bufferedReader = BufferedReader(`in`)
                    var cp: Int
                    while (bufferedReader.read().also { cp = it } != -1) {
                        stringBuilder.append(cp.toChar())
                    }
                    bufferedReader.close()
                    `in`.close()

                    val executorException = Exception(
                        httpURlConnection?.responseCode, httpURlConnection?.responseMessage,
                        "IOException $e"
                    )
                    runOnUiThread { iGetResponse.onError(executorException) }

                }

            } catch (e: java.lang.Exception) {
                if (httpURlConnection?.errorStream == null) {
                    val executorException = Exception(
                        httpURlConnection?.responseCode, httpURlConnection?.responseMessage,
                        " java.lang.Exception $e"
                    )
                    runOnUiThread { iGetResponse.onError(executorException) }
                } else {
                    val `in` = InputStreamReader(httpURlConnection?.errorStream)
                    val stringBuilder = StringBuilder()
                    val bufferedReader = BufferedReader(`in`)
                    var cp: Int
                    while (bufferedReader.read().also { cp = it } != -1) {
                        stringBuilder.append(cp.toChar())
                    }
                    bufferedReader.close()
                    `in`.close()
                    val executorException = Exception(
                        httpURlConnection?.responseCode!!, httpURlConnection?.responseMessage,
                        " java.lang.Exception $e"
                    )
                    runOnUiThread { iGetResponse.onError(executorException) }

                }

            } finally {
                if (httpURlConnection != null) {
                    httpURlConnection?.disconnect()
                }

                if (writer != null) {
                    try {
                        writer?.close()
                    } catch (ex: IOException) {
                        Log.e("Executor writer error", "Executor: $ex")
                    }
                }

            }

        }

    }

    private fun encodeParams(params: JSONObject): String {
        val result = StringBuilder()
        var first = true
        val itr = params.keys()
        while (itr.hasNext()) {
            val key = itr.next()
            val value = params[key]
            if (first) first = false else result.append("&")
            result.append(URLEncoder.encode(key, "UTF-8"))
            result.append("=")
            result.append(URLEncoder.encode(value.toString(), "UTF-8"))
        }
        return result.toString()
    }




    inline fun <reified T : Any> createModelFromClass(json: String): T {
        return Gson().fromJson(json, T::class.java)
    }


}