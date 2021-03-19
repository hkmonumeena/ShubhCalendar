package com.shubhcalendar.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.shubhcalendar.R
import com.shubhcalendar.utills.Craft.putKey
import com.shubhcalendar.utills.Craft.startActivity
import com.shubhcalendar.utills.Keys

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val btnClickMe = findViewById<Button>(R.id.btnClickMe)
        btnClickMe.setOnClickListener {
            putKey(Keys.userID, "")
            finishAffinity()
            startActivity<SplashActivity>()
        }
    }
}