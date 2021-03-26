package com.shubhcalendar.activities

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView
import com.shubhcalendar.R
import com.shubhcalendar.databinding.ActivityHomeBinding
import com.shubhcalendar.databinding.ActivityPanchangBinding
import com.shubhcalendar.utills.Craft.startActivity

class PanchangActivity : AppCompatActivity() {

    lateinit var binding: ActivityPanchangBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityPanchangBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.materialShare.setOnClickListener {
            startActivity<FestivalDetailActivity>()
        }
        binding.layoutMuhurat.setOnClickListener {
            startActivity<MuhuratActivity>()
        }
    }
}