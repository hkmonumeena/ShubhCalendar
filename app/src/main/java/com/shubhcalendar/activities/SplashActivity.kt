package com.shubhcalendar.activities

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.shubhcalendar.R
import com.shubhcalendar.ui.HomeNewActivity
import com.shubhcalendar.ui.childhelps.LangugageSheet
import com.shubhcalendar.utills.Craft.getKey
import com.shubhcalendar.utills.Craft.startActivity
import com.shubhcalendar.utills.Keys
import com.shubhcalendar.utills.LocaleUtils

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val w = window
        w.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        val Lang = getKey(Keys.Language)
        Log.e("dsjdik", "onCreate: $Lang")
        if (Lang == "en") {
            LocaleUtils.setLocale(this, 1)
        } else if (Lang == "hi") {
            LocaleUtils.setLocale(this, 0)
        } else if (Lang == "bn") {
            LocaleUtils.setLocale(this, 2)

        } else {
            LocaleUtils.setLocale(this, 0)
        }
        Handler().postDelayed({
            Log.e("SplashActivity", "onCreate: ${getKey(Keys.userID)}")
            takeAction()
        }, 2000)

    }

    private fun takeAction() {
        if (getKey(Keys.userID).isNullOrEmpty()) {
            startActivity<RegisterMobileActivity>()
            finish()
        } else {
            if (getKey(Keys.isLanguageSelected)?.isEmpty() == true) {
                val bundle = Bundle()
                bundle.putString("openedFrom", "ShowAddressBottomsheet")
                val bottomSheet = LangugageSheet()
                bottomSheet.arguments = bundle
                bottomSheet.show(supportFragmentManager, "new address")
            } else {
                startActivity<HomeNewActivity>()
                finish()
            }

        }
    }
}