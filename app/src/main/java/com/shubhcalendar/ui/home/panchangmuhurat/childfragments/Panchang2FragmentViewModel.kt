package com.shubhcalendar.ui.home.panchangmuhurat.childfragments

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.httpconnection.httpconnectionV2.Http
import com.httpconnection.httpconnectionV2.interfaces.IGetResponse
import com.httpconnection.httpconnectionV2.models.Exception
import com.httpconnection.httpconnectionV2.requestMethods.Execute

class Panchang2FragmentViewModel:ViewModel() {
    val getLiveData = MutableLiveData<DataShowPanchang>()
     fun showPanchang(mutableMapOf: MutableMap<String, String>, get: Int):MutableLiveData<DataShowPanchang> {
        Log.e("flag--", "showPanchang(Panchang2FragmentViewModel.kt:14)-->>$mutableMapOf")
        Http.Post("https://maestrosinfotech.org/shubh_calendar/appservice/process.php?action=show_panchang")
            .bodyParameter(mutableMapOf)
            .build()
            .executeString(object : IGetResponse {
               override fun onResponse(response: String?) {
                  Log.e("fdfdfdsfdsgfdhdg","onResponse: $response",)
                  val getData = Execute().createModelFromClass<DataShowPanchang>(response !!)
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


}