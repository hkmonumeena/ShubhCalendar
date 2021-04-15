package com.shubhcalendar.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.shubhcalendar.R
import com.shubhcalendar.databinding.ActivityHomeNewBinding
import com.shubhcalendar.ui.calendar.CalendarFragment
import com.shubhcalendar.ui.childhelps.LangugageSheet
import com.shubhcalendar.ui.holidays.HolidaysFragment
import com.shubhcalendar.ui.home.HomeFragment
import com.shubhcalendar.ui.horoscope.HoroscopeFragment
import com.shubhcalendar.ui.panchang.PanchangFragment
import com.trendyol.medusalib.navigator.MultipleStackNavigator
import com.trendyol.medusalib.navigator.Navigator
import com.trendyol.medusalib.navigator.NavigatorConfiguration
import com.trendyol.medusalib.navigator.transaction.NavigatorTransaction

class HomeNewActivity : AppCompatActivity(), Navigator.NavigatorListener, View.OnClickListener {
    private val rootFragmentProvider: List<() -> Fragment> = listOf(
        { CalendarFragment() },     //0
        { HolidaysFragment() },     //1
        { HomeFragment() },         //2
        { PanchangFragment() },     //3
        { HoroscopeFragment() })     //4
    val multipleStackNavigator: MultipleStackNavigator =
        MultipleStackNavigator(
            supportFragmentManager,
            R.id.frame,
            rootFragmentProvider,
            navigatorListener = this,
            navigatorConfiguration = NavigatorConfiguration(2, true, NavigatorTransaction.SHOW_HIDE)
        )
    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigationCalendar -> {
                    multipleStackNavigator.switchTab(0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigationHolidays -> {

                    multipleStackNavigator.switchTab(1)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigationHome -> {
                    multipleStackNavigator.switchTab(2)

                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigationPanchang -> {
                    multipleStackNavigator.switchTab(3)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigationHoroscope -> {
                    multipleStackNavigator.switchTab(4)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }
    lateinit var binding: ActivityHomeNewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeNewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        multipleStackNavigator.initialize(savedInstanceState)
        binding.bottomNavigation.setOnNavigationItemSelectedListener(
            mOnNavigationItemSelectedListener
        )
        binding.rlLanguage.setOnClickListener(this)

    }

    override fun onBackPressed() {
        if (multipleStackNavigator.canGoBack()) {
            multipleStackNavigator.goBack()
        } else {
            // takeExit()
        }
    }

    override fun onTabChanged(tabIndex: Int) {
        when (tabIndex) {
            0 -> binding.bottomNavigation.selectedItemId = R.id.navigationCalendar
            1 -> binding.bottomNavigation.selectedItemId = R.id.navigationHolidays
            2 -> binding.bottomNavigation.selectedItemId = R.id.navigationHome
            3 -> binding.bottomNavigation.selectedItemId = R.id.navigationPanchang
            4 -> binding.bottomNavigation.selectedItemId = R.id.navigationHoroscope
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        multipleStackNavigator.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.rlLanguage -> {
                val bundle = Bundle()
                bundle.putString("openedFrom", "ShowAddressBottomsheet")
                val bottomSheet = LangugageSheet()
                bottomSheet.arguments = bundle
                bottomSheet.show(supportFragmentManager, "new address")
            }
        }
    }
}