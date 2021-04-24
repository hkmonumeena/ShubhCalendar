package com.shubhcalendar.ui.home.panchangmuhurat.childfragments

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.httpconnection.httpconnectionV2.Http
import com.httpconnection.httpconnectionV2.interfaces.IGetResponse
import com.httpconnection.httpconnectionV2.models.Exception
import com.httpconnection.httpconnectionV2.requestMethods.Execute
import com.shubhcalendar.utills.Api
import java.util.ArrayList

class PanchangFragmentViewModel:ViewModel() {
    val getLiveData = MutableLiveData<DataShowMuhurat>()
     fun showMuhurat(mutableMapOf: MutableMap<String, String>, get: Int):MutableLiveData<DataShowMuhurat> {
        Http.Post(Api.show_muhurat)
            .bodyParameter(mutableMapOf)
            .build()
            .executeString(object : IGetResponse {
               override fun onResponse(response: String?) {
                  Log.e("dssdsds","onResponse: $response",)
                  val getData = Execute().createModelFromClass<DataShowMuhurat>(response !!)
                  if (getData.result == "sucessfull") {
                     getLiveData.value = getData
                  }else {
                     getLiveData.value = getData
                  }
               }

               override fun onError(error: Exception?) {
               }
            })
        return  getLiveData
    }


    data class DataShowMuhurat(
        val `data`: List<Data?>?,
        val result: String?
    ) {
        data class Data(
            val date: String?,
            val end_time: String?,
            val id: String?,
            val path: String?,
            val start_time: String?,
            val title: String?
        )
    }
}