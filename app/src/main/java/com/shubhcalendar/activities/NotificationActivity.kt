package com.shubhcalendar.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.httpconnection.httpconnectionV2.Http
import com.httpconnection.httpconnectionV2.interfaces.IGetResponse
import com.httpconnection.httpconnectionV2.models.Exception
import com.shubhcalendar.R
import com.shubhcalendar.databinding.ActivityNotificationBinding
import com.shubhcalendar.utills.Api.show_notification
import com.shubhcalendar.utills.Craft.getKey
import com.shubhcalendar.utills.Craft.textView
import com.shubhcalendar.utills.GenricAdapter
import com.shubhcalendar.utills.Keys
import com.shubhcalendar.utills.ViewHolder

class NotificationActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityNotificationBinding
    lateinit var messageReceiver: BroadcastReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        messageReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                var test = intent.extras?.getString("message")
                Log.e("fdgdgfgf", "onReceive: " + test)
                showNotifications()
            }
        }

        showNotifications()

        binding.cardViewBack.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(messageReceiver, IntentFilter("MyData"))
    }

    private fun showNotifications() {
        Http.Post(show_notification)
            .bodyParameter(mutableMapOf("user_id" to getKey(Keys.userID) as String))
            .build()
            .executeString(object : IGetResponse {
                override fun onResponse(response: String?) {
                    if (response?.isNotBlank() == true) {
                        Log.e("flag--", "(NotificationActivity.kt:21)-->>$response")
                        val getData = Http().createModelFromClass<DataShowNotification>(response)
                        if (getData.result == "sucessfull") {
                            if (getData.data?.isNotEmpty() == true) {
                                binding.rvShowNotification.apply {
                                    layoutManager = LinearLayoutManager(this@NotificationActivity)
                                    adapter = RvShowNotifications(
                                        getData.data as ArrayList<DataShowNotification.Data>,
                                        this@NotificationActivity
                                    )
                                }
                            } else {
                                Toast.makeText(
                                    this@NotificationActivity,
                                    "No Notification Available at this moment",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }

                override fun onError(error: Exception?) {
                    Log.e("flag--", "(NotificationActivity.kt:21)-->>")
                }
            })
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.cardViewBack -> {
                finish()
            }
        }
    }

}


private class RvShowNotifications(
    items: ArrayList<DataShowNotification.Data>,
    val notificationActivity: NotificationActivity
) : GenricAdapter<DataShowNotification.Data>(items) {
    override fun configure(item: DataShowNotification.Data, holder: ViewHolder, position: Int) {
        with(holder.itemView) {
            textView(R.id.textViewTitle).text = item.title
            textView(R.id.textViewDescription).text = item.description
            textView(R.id.textViewDate).text = item.date
        }
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.rv_show_notification_items
    }
}


private data class DataShowNotification(
    val `data`: List<Data?>? = null,
    val result: String? = null
) {
    data class Data(
        val date: String? = null,
        val description: String? = null,
        val id: String? = null,
        val image: String? = null,
        val path: String? = null,
        val send_date: String? = null,
        val title: String? = null,
        val user_id: String? = null
    )
}