package com.shubhcalendar.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.shubhcalendar.R
import com.shubhcalendar.databinding.ActivitySelectPujaBinding
import com.shubhcalendar.databinding.ActivityVidhiBinding
import com.shubhcalendar.utills.Craft.startActivity

class VidhiActivity : AppCompatActivity() {

    lateinit var binding: ActivityVidhiBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityVidhiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.layoutMuhurat.setOnClickListener {
            startActivity<KathasActivity>()
        }
    }
}