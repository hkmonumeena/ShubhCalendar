package com.shubhcalendar.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView
import com.shubhcalendar.R
import com.shubhcalendar.utills.Craft.startActivity

class GetHoroscopeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_horoscope)
        val cardViewArrow = findViewById<CardView>(R.id.cardViewArrow)
        cardViewArrow.setOnClickListener {

        }
    }
}