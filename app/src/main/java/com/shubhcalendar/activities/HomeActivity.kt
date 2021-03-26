package com.shubhcalendar.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import com.google.android.material.card.MaterialCardView
import com.shubhcalendar.R
import com.shubhcalendar.databinding.ActivityHomeBinding
import com.shubhcalendar.utills.Craft.putKey
import com.shubhcalendar.utills.Craft.startActivity
import com.shubhcalendar.utills.Keys

class HomeActivity : AppCompatActivity() {

    lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rlFestival.setOnClickListener {
            startActivity<LanguageActivity>()
        }
binding.materialPuja.setOnClickListener {
            startActivity<SelectPujaActivity>()
        }

        binding.materialCardHolidays.setOnClickListener {
            startActivity<HolidaysActivity>()
        }
        binding.rlNotification.setOnClickListener {
            startActivity<NotificationActivity>()
        }

        binding.materialCardPanchang.setOnClickListener {
            startActivity<PanchangActivity>()
        }

        binding.icMenu.setOnClickListener { binding.drawer.openDrawer(GravityCompat.END) }
        binding.ivCross.setOnClickListener { binding.drawer.closeDrawer(GravityCompat.END) }

   val cardViewGetHoroscope = findViewById<CardView>(R.id.cardViewGetHoroscope)
        cardViewGetHoroscope.setOnClickListener {
            putKey(Keys.userID, "")
            finishAffinity()
            startActivity<GetHoroscopeActivity>()
        }

    }
}

