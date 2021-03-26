package com.shubhcalendar.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.shubhcalendar.R
import com.shubhcalendar.databinding.ActivityHomeBinding
import com.shubhcalendar.databinding.ActivitySelectPujaBinding
import com.shubhcalendar.utills.Craft.startActivity

class SelectPujaActivity : AppCompatActivity() {

    lateinit var binding: ActivitySelectPujaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySelectPujaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.layoutVidhi.setOnClickListener {
            startActivity<VidhiActivity>()
        }

        binding.layoutKatha.setOnClickListener {
            startActivity<KathasActivity>()
        }
    }
}