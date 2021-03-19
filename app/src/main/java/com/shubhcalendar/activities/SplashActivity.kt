package com.shubhcalendar.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.shubhcalendar.MainActivity
import com.shubhcalendar.R
import com.shubhcalendar.utills.Craft.getKey
import com.shubhcalendar.utills.Craft.startActivity
import com.shubhcalendar.utills.Keys

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val w = window
        w.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        Handler().postDelayed({
            Log.e("SplashActivity", "onCreate: ${getKey(Keys.userID)}");
            takeAction()
        }, 2000)

    }

    private fun takeAction() {
        if (getKey(Keys.userID).isNullOrEmpty()) {
            startActivity<RegisterMobileActivity>()
            finish()
        } else {
            startActivity<HomeActivity>()
            finish()
        }
    }
}