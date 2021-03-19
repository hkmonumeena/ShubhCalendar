package com.shubhcalendar.utills

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object Craft {
    private var sharedPreferences: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    private const val MY_PREF = "MY_PREF"

    inline fun Context.toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    inline fun <reified T : Activity> Activity.startActivity() {
        startActivity(createIntent<T>())
    }

    inline fun <reified T : Activity> Context.createIntent() =
        Intent(this, T::class.java)


    fun Context.putKey( Key: String?, Value: String?) {
        sharedPreferences =
          getSharedPreferences(MY_PREF, Context.MODE_PRIVATE)
        editor = sharedPreferences?.edit()
        editor?.putString(Key, Value)
        editor?.apply()
    }

    fun Context.getKey(Key: String?): String? {
        sharedPreferences =
            getSharedPreferences(MY_PREF, Context.MODE_PRIVATE)
        return sharedPreferences?.getString(Key, "")
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun Context.isOnline(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
        return false
    }


    fun Context.confirmationDialog(
        title: String,
        items: ArrayList<String>,
        id: ArrayList<String>,
        proceed: (position: Int, value: String, id: String) -> Unit
    ) {
        lateinit var itemaNamesArray: Array<String>
        lateinit var itemaIdArray: Array<String>

        if (items.size != 0) {
            itemaNamesArray = items.toTypedArray()
            itemaIdArray = id.toTypedArray()
            val checkedItem = 1
            MaterialAlertDialogBuilder(this)
                .setTitle(title)
                .setNegativeButton("Cancel") { dialog, which ->
                    // Respond to neutral button press
                }
                .setPositiveButton("Okay") { dialog, which ->
                    // Respond to positive button press

                    val position = (dialog as androidx.appcompat.app.AlertDialog).listView.checkedItemPosition
                    proceed.invoke(position, itemaNamesArray[position], itemaIdArray[position])
                }
                // Single-choice items (initialized with checked item)
                .setSingleChoiceItems(itemaNamesArray, checkedItem) { dialog, which ->
                    // Respond to item chosen
                }
                .show()
        } else {
            Toast.makeText(this, "No items found", Toast.LENGTH_SHORT).show()
        }


    }

}