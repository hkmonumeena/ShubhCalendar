package com.shubhcalendar.utills

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.shubhcalendar.R
import com.shubhcalendar.ui.HomeNewActivity

import org.json.JSONObject
import java.util.*


// Made By : 10/oct/2020$ Monu meena$

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val TAG = "MyFirebaseToken"
    private lateinit var notificationManager: NotificationManager
    private val ADMIN_CHANNEL_ID = "1"
    private var broadcaster: LocalBroadcastManager? = null
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.i(TAG, token)
    }
    @SuppressLint("LongLogTag")
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        remoteMessage?.let { message ->

            message.getData().get("message")?.let { Log.i(TAG, it) }
            Log.e("MyFirebaseMessagingService",
                """rawData: ${remoteMessage.rawData} 
                |from: ${remoteMessage.from}
                |messageType: ${remoteMessage.from}
                |messageType: ${remoteMessage.messageType}
                |notification: ${remoteMessage.notification}
                |messageId: ${remoteMessage.messageId}
                |data: ${remoteMessage.data}
            """.trimMargin())
            if (!remoteMessage.data["data"].isNullOrEmpty()) {
                if (!isForeground(this)) {
                    val jsonObject: JSONObject = JSONObject(remoteMessage.data["data"])
                    notificationManager =
                        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    //Setting up Notification channels for android O and above
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        setupNotificationChannels()
                    }

                    val notificationId = Random().nextInt(60000)
                    val defaultSoundUri =
                        RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
                    val notificationBuilder = NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_new_swastik)  //a resource for your custom small icon
                        .setContentTitle(jsonObject.getString("title")) //the "title" value you sent in your notification
                        .setContentText(jsonObject.getString("message")) //ditto
                        .setAutoCancel(true)  //dismisses the notification on click
                        .setSound(defaultSoundUri)

                    val notifyIntent = Intent(this, HomeNewActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    val notifyPendingIntent = PendingIntent.getActivity(this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)

                    notificationBuilder.setContentIntent(notifyPendingIntent)

                    val notificationManager =
                        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager.notify(
                        notificationId /* ID of notification */,
                        notificationBuilder.build()
                    )

                    handleMessage(remoteMessage)
                } else {
                    handleMessage(remoteMessage)

                }
            }


        }
    }
    override fun onCreate() {
        broadcaster = LocalBroadcastManager.getInstance(this)
    }
    fun handleMessage(remoteMessage: RemoteMessage) {
        //1
        val handler = Handler(Looper.getMainLooper())
        //2
        handler.post(Runnable {
            if (!remoteMessage.data["data"].equals(null)) {
                val jsonObject: JSONObject = JSONObject(remoteMessage.data["data"])
                val intent = Intent("MyData")
                intent.putExtra("message", jsonObject.getString("message"));
                this.sendBroadcast(intent);
            }
        }
        )
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun setupNotificationChannels() {
        val adminChannelName = "Notification"
        val adminChannelDescription = "Description"
        val adminChannel: NotificationChannel
        adminChannel = NotificationChannel(
            ADMIN_CHANNEL_ID,
            adminChannelName,
            NotificationManager.IMPORTANCE_HIGH)
        adminChannel.description = adminChannelDescription
        adminChannel.enableLights(true)
        adminChannel.lightColor = Color.RED
        adminChannel.enableVibration(true)
        adminChannel.shouldVibrate()
        notificationManager.createNotificationChannel(adminChannel)
    }
    private fun isForeground(context: Context): Boolean {
        val am = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val tasks = am.runningAppProcesses
        val packageName = context.packageName
        for (appProcess in tasks) {
            if (RunningAppProcessInfo.IMPORTANCE_FOREGROUND == appProcess.importance && packageName == appProcess.processName) {
                return true
            }
        }
        return false
    }


}