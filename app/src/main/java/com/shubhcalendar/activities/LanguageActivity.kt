package com.shubhcalendar.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.shubhcalendar.R
import com.shubhcalendar.databinding.ActivityLanguageBinding
import com.shubhcalendar.utills.Craft.startActivity

class LanguageActivity : AppCompatActivity() {
    lateinit var binding: ActivityLanguageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.cardViewHindi.setOnClickListener {
            startActivity<FestivalActivity>()
        }
        binding.cardViewEnglish.setOnClickListener {
            startActivity<FestivalDetailsActivity>()
        }

        binding.cardViewBengali.setOnClickListener {
            startActivity<ProfileActivity>()
        }

        binding.cardViewMarathi.setOnClickListener {
            startActivity<PaymentSuccessActivity>()
        }
        binding.cardViewTamil.setOnClickListener {
            startActivity<SelectPaymentActivity>()
        }

        binding.cardViewTelugu.setOnClickListener {
            startActivity<RashiActivity>()
        }
    }
}