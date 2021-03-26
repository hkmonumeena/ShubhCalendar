package com.shubhcalendar.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.shubhcalendar.R
import com.shubhcalendar.databinding.ActivityHomeBinding
import com.shubhcalendar.utills.Craft.confirmationDialog
import com.shubhcalendar.utills.Craft.putKey
import com.shubhcalendar.utills.Craft.startActivity
import com.shubhcalendar.utills.GenricAdapter
import com.shubhcalendar.utills.Keys
import com.shubhcalendar.utills.ViewHolder
import kotlinx.android.synthetic.main.calendar_item.view.*
import java.util.*

class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding
    private val calendar = Calendar.getInstance()
    private var currentMonth = 0
    var mutableList: MutableList<ModelMonth> = mutableListOf()
    val monthNumListShow = arrayListOf<String>("January","February","March","April","May","June","July","August","September","October","November","December")
    val monthNumList = arrayListOf<String>("0","1","2","3","4","5","6","7","8","9","10","11")
    var hour: Int = 0
    var minute: Int = 0
    var seconds: Int = 0

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

        hour = calendar.get(Calendar.HOUR_OF_DAY)
        minute = calendar.get(Calendar.MINUTE)
        seconds = calendar.get(Calendar.SECOND)
        binding.textViewDateYear.setOnClickListener {
            confirmationDialog("Select Month", monthNumListShow,monthNumList)
            { position, value, id ->
                currentMonth=id.toInt()
                mutableList.clear()
                getAnyDate(id.toInt()).forEach {
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

                binding.recyclerMonth.layoutManager =
                    LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                binding.recyclerMonth.adapter = AdapterMonth(mutableList as ArrayList<ModelMonth>)

            }
        }

        getFutureDatesOfCurrentMonth().forEach {
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

        binding.recyclerMonth.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerMonth.adapter = AdapterMonth(mutableList as ArrayList<ModelMonth>)


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
        Log.e("TestActivity", "getFutureDatesOfCurrentMonth: $currentMonth");
        return getDates(mutableListOf())
    }

    private fun getAnyDate(selectedMonth:Int): List<Date> {
        // get all next dates of current month
        currentMonth--
        currentMonth =selectedMonth
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


class AdapterMonth(items: ArrayList<ModelMonth>) : GenricAdapter<ModelMonth>(items) {


    override fun getItemViewType(position: Int): Int {
        return R.layout.calendar_item
    }

    override fun configure(item: ModelMonth, holder: ViewHolder, position: Int) {
        val set = holder.itemView
        set.textViewDayName.text = item.dayname
        set.textViewDate.text = item.day
    }

}

data class ModelMonth(
    val day: String,
    val dayname: String,
    val monthNum: String,
    val year: String,
    val hour: String,
    val minute: String,

    )