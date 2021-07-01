package com.shubhcalendar.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.shubhcalendar.activities.AdapterMonth
import com.shubhcalendar.activities.ModelMonth
import com.shubhcalendar.activities.NotificationActivity
import com.shubhcalendar.databinding.FragmentHomeBinding
import com.shubhcalendar.ui.HomeNewActivity
import com.shubhcalendar.ui.calendar.CalendarFragment
import com.shubhcalendar.ui.home.festival.FestivalAndHoliday
import com.shubhcalendar.ui.home.festival.ShowFestivals
import com.shubhcalendar.ui.home.panchangmuhurat.PanchabgMuhurat
import com.shubhcalendar.ui.home.poojaartiskatha.ArtiVidhiKathaFrag
import com.shubhcalendar.ui.horoscope.HoroscopeFragment
import com.shubhcalendar.ui.profile.ProfileFragment
import com.shubhcalendar.utills.BaseFragment
import com.shubhcalendar.utills.Craft.startActivity
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : BaseFragment(), View.OnClickListener {
    lateinit var binding: FragmentHomeBinding
    private val calendar = Calendar.getInstance()
    private var currentMonth = 0
    var mutableList: MutableList<ModelMonth> = mutableListOf()
    val monthNumListShow = arrayListOf(
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December"
    )
    val monthNumList = arrayListOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11")
    var hour: Int = 0
    var minute: Int = 0
    var seconds: Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hour = calendar.get(Calendar.HOUR_OF_DAY)
        minute = calendar.get(Calendar.MINUTE)
        seconds = calendar.get(Calendar.SECOND)
        initializeClickListeners()
        initializeHorizontalCalendar() // for first time show calendar with current month

        MobileAds.initialize(requireActivity(),
            {
                Toast.makeText(
                    requireActivity(),
                    " sucesfull ",
                    Toast.LENGTH_SHORT
                ).show()
            })


        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
    }

    private fun initializeClickListeners() {
        binding.icMenu.setOnClickListener(this)
        binding.cardViewCalendar.setOnClickListener(this)
        binding.llPanchangMuhurat.setOnClickListener(this)
        binding.cardViewAartiPoojaKatha.setOnClickListener(this)
        binding.materialCardHolidays.setOnClickListener(this)
        binding.cardViewProfile.setOnClickListener(this)
        binding.cardViewFestivals.setOnClickListener(this)
        binding.rlDismiss.setOnClickListener(this)
        binding.imageViewNotification.setOnClickListener(this)
        binding.cardViewGetHoroscope.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.icMenu -> {
                (activity as HomeNewActivity).binding.drawer.openDrawer(GravityCompat.END)
            }
            binding.rlDismiss -> {
                (activity as HomeNewActivity).onBackPressed()
            }
            binding.cardViewCalendar -> {
                (activity as HomeNewActivity).multipleStackNavigator.start(CalendarFragment())
            }
            binding.llPanchangMuhurat -> {
                multipleStackNavigator?.start(
                    PanchabgMuhurat(),
                    
                )


            }
            binding.cardViewAartiPoojaKatha -> {
                /* requireActivity().putKey(Keys.isFragBack,"true")
                 (activity as HomeNewActivity).loadFragment(ArtiVidhiKathaFrag())
                 (activity as HomeNewActivity).binding.bottomNavigation.isVisible = false
                 (activity as HomeNewActivity).binding.frame.isVisible = false
                 (activity as HomeNewActivity).binding.frame2.isVisible = true*/
                multipleStackNavigator?.start(
                    ArtiVidhiKathaFrag()
                )
            }
            binding.cardViewProfile -> {
                multipleStackNavigator?.start(
                    ProfileFragment()
                )
            }
            binding.cardViewFestivals -> {
             /*   multipleStackNavigator?.start(
                    HolidaysFragment(),
                    TransitionAnimationType.LEFT_TO_RIGHT
                )*/
            }
            binding.materialCardHolidays -> {
                multipleStackNavigator?.start(
                    FestivalAndHoliday()
                )
            }
binding.cardViewGetHoroscope -> {
                multipleStackNavigator?.start(
                    HoroscopeFragment()
                )
            }

            binding.imageViewNotification -> {
                requireActivity().startActivity<NotificationActivity>()

            }
        }
    }

    private fun initializeHorizontalCalendar() {
        var forOneTime = true
        getFutureDatesOfCurrentMonth().forEach {
            if (forOneTime) {
                binding.textViewDateYear.text =
                    "${com.michalsvec.singlerowcalendar.utils.DateUtils.getMonthName(it)} ${com.michalsvec.singlerowcalendar.utils.DateUtils.getYear(it)}"
                forOneTime = false
            }
            mutableList.add(
                ModelMonth(
                    com.michalsvec.singlerowcalendar.utils.DateUtils.getDayNumber(it),
                    com.michalsvec.singlerowcalendar.utils.DateUtils.getDay3LettersName(it),
                    com.michalsvec.singlerowcalendar.utils.DateUtils.getMonthNumber(it),
                    com.michalsvec.singlerowcalendar.utils.DateUtils.getYear(it),
                    hour.toString(),
                    minute.toString()
                )
            )
        }

        val simpleDateFormat by lazy { SimpleDateFormat("dd") }
        val currentDateAndTime: String = simpleDateFormat.format(Date())


        binding.recyclerMonth.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerMonth.adapter = AdapterMonth(mutableList as ArrayList<ModelMonth>)
        binding.recyclerMonth.scrollToPosition(currentDateAndTime.toInt() - 1)
        binding.recyclerMonth.setItemViewCacheSize(31)
    }

    private fun getDatesOfNextMonth(): List<Date> {
        currentMonth++ // + because we want next month
        if (currentMonth == 12) {
            // we will switch to january of next year, when we reach last month of year
            calendar.set(Calendar.YEAR, calendar[Calendar.YEAR] + 1)
            currentMonth = 0 // 0 == january
        }
        return getDates(mutableListOf())
    }

    private fun getDatesOfPreviousMonth(): List<Date> {
        currentMonth-- // - because we want previous month
        if (currentMonth == -1) {
            // we will switch to december of previous year, when we reach first month of year
            calendar.set(Calendar.YEAR, calendar[Calendar.YEAR] - 1)
            currentMonth = 11 // 11 == december
        }
        return getDates(mutableListOf())
    }

    private fun getFutureDatesOfCurrentMonth(): List<Date> {
        // get all next dates of current month
        currentMonth--
        currentMonth = calendar[Calendar.MONTH]
        Log.e("TestActivity", "getFutureDatesOfCurrentMonth: $currentMonth")
        return getDates(mutableListOf())
    }

    private fun getAnyDate(selectedMonth: Int): List<Date> {
        // get all next dates of current month
        currentMonth--
        currentMonth = selectedMonth
        return getDates(mutableListOf())
    }

    private fun getDates(list: MutableList<Date>): List<Date> {
        // load dates of whole month
        Log.e("pihu", "getDates: " + currentMonth)
        calendar.set(Calendar.MONTH, currentMonth)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        list.add(calendar.time)
        while (currentMonth == calendar[Calendar.MONTH]) {
            calendar.add(Calendar.DATE, +1)
            if (calendar[Calendar.MONTH] == currentMonth)
                list.add(calendar.time)

        }
        calendar.add(Calendar.DATE, -1)

        return list
    }

}