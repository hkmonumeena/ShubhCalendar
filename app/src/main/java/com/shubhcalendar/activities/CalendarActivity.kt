package com.shubhcalendar.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.shubhcalendar.R
import com.shubhcalendar.databinding.ActivityCalendarBinding
import com.shubhcalendar.utills.Craft.confirmationDialog
import com.shubhcalendar.utills.Craft.toast
import com.shubhcalendar.utills.GenricAdapter
import com.shubhcalendar.utills.ViewHolder
import kotlinx.android.synthetic.main.calendar.view.*
import java.util.*
import kotlin.collections.ArrayList

class CalendarActivity : AppCompatActivity() {
    lateinit var binding: ActivityCalendarBinding
    private val calendar = Calendar.getInstance()
    private var currentMonth = 0
    var mutableList: MutableList<ModelMonth> = mutableListOf()
    var hour: Int = 0
    var minute: Int = 0
    var seconds: Int = 0
    val monthNumListShow = arrayListOf<String>("January","February","March","April","May","June","July","August","September","October","November","December")
    val monthNumList = arrayListOf<String>("0","1","2","3","4","5","6","7","8","9","10","11")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)
binding.textViewSun.setOnClickListener {
    confirmationDialog("Select Month", monthNumListShow,monthNumList)
    { position, value, id ->
        currentMonth=id.toInt()
        mutableList.clear()
        getAnyDate(id.toInt()).forEach {

            if (com.michalsvec.singlerowcalendar.utils.DateUtils.getDayNumber(it) == "01") {
                if (com.michalsvec.singlerowcalendar.utils.DateUtils.getDay3LettersName(it) == "Sun") {
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
                } else if (com.michalsvec.singlerowcalendar.utils.DateUtils.getDay3LettersName(it) == "Mon") {
                    mutableList.add(
                        ModelMonth(
                            "-",
                            "-",
                            "-",
                            "-",
                            hour.toString(),
                            minute.toString()
                        )
                    )
                    toast("Mon")
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
                } else if (com.michalsvec.singlerowcalendar.utils.DateUtils.getDay3LettersName(it) == "Tue") {

                    for (i in 0 until 2){
                        mutableList.add(
                            ModelMonth(
                                "-",
                                "-",
                                "-",
                                "-",
                                hour.toString(),
                                minute.toString()
                            )
                        )
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
                } else if (com.michalsvec.singlerowcalendar.utils.DateUtils.getDay3LettersName(it) == "Wed") {
                    for (i in 0 until 3){
                        mutableList.add(
                            ModelMonth(
                                "-",
                                "-",
                                "-",
                                "-",
                                hour.toString(),
                                minute.toString()
                            )
                        )
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
                } else if (com.michalsvec.singlerowcalendar.utils.DateUtils.getDay3LettersName(it) == "Thu") {
                    for (i in 0 until 4){
                        mutableList.add(
                            ModelMonth(
                                "-",
                                "-",
                                "-",
                                "-",
                                hour.toString(),
                                minute.toString()
                            )
                        )
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

                } else if (com.michalsvec.singlerowcalendar.utils.DateUtils.getDay3LettersName(it) == "Fri") {

                    for (i in 0 until 5){
                        mutableList.add(
                            ModelMonth(
                                "-",
                                "-",
                                "-",
                                "-",
                                hour.toString(),
                                minute.toString()
                            )
                        )
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

                } else if (com.michalsvec.singlerowcalendar.utils.DateUtils.getDay3LettersName(it) == "Sat") {
                    for (i in 0 until 6){
                        mutableList.add(
                            ModelMonth(
                                "-",
                                "-",
                                "-",
                                "-",
                                hour.toString(),
                                minute.toString()
                            )
                        )
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
            } else {
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

        }

        binding.recyclerDates.layoutManager = GridLayoutManager(this, 7)
        binding.recyclerDates.adapter = AdapterCalendar(mutableList as ArrayList<ModelMonth>)

    }
}
        getFutureDatesOfCurrentMonth().forEach {

            if (com.michalsvec.singlerowcalendar.utils.DateUtils.getDayNumber(it) == "01") {
                if (com.michalsvec.singlerowcalendar.utils.DateUtils.getDay3LettersName(it) == "Sun") {
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
                } else if (com.michalsvec.singlerowcalendar.utils.DateUtils.getDay3LettersName(it) == "Mon") {
                    mutableList.add(
                        ModelMonth(
                            "-",
                            "-",
                            "-",
                            "-",
                            hour.toString(),
                            minute.toString()
                        )
                    )
                    toast("Mon")
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
                } else if (com.michalsvec.singlerowcalendar.utils.DateUtils.getDay3LettersName(it) == "Tue") {

                    for (i in 0 until 2){
                        mutableList.add(
                            ModelMonth(
                                "-",
                                "-",
                                "-",
                                "-",
                                hour.toString(),
                                minute.toString()
                            )
                        )
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
                } else if (com.michalsvec.singlerowcalendar.utils.DateUtils.getDay3LettersName(it) == "Wed") {
                    for (i in 0 until 3){
                        mutableList.add(
                            ModelMonth(
                                "-",
                                "-",
                                "-",
                                "-",
                                hour.toString(),
                                minute.toString()
                            )
                        )
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
                } else if (com.michalsvec.singlerowcalendar.utils.DateUtils.getDay3LettersName(it) == "Thu") {
                    for (i in 0 until 4){
                        mutableList.add(
                            ModelMonth(
                                "-",
                                "-",
                                "-",
                                "-",
                                hour.toString(),
                                minute.toString()
                            )
                        )
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

                } else if (com.michalsvec.singlerowcalendar.utils.DateUtils.getDay3LettersName(it) == "Fri") {

                    for (i in 0 until 5){
                        mutableList.add(
                            ModelMonth(
                                "-",
                                "-",
                                "-",
                                "-",
                                hour.toString(),
                                minute.toString()
                            )
                        )
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

                } else if (com.michalsvec.singlerowcalendar.utils.DateUtils.getDay3LettersName(it) == "Sat") {
                    for (i in 0 until 6){
                        mutableList.add(
                            ModelMonth(
                                "-",
                                "-",
                                "-",
                                "-",
                                hour.toString(),
                                minute.toString()
                            )
                        )
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
            } else {
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


        }
        binding.recyclerDates.layoutManager = GridLayoutManager(this, 7)
        binding.recyclerDates.adapter = AdapterCalendar(mutableList as ArrayList<ModelMonth>)

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


class AdapterCalendar(items: ArrayList<ModelMonth>) : GenricAdapter<ModelMonth>(items) {
    override fun configure(item: ModelMonth, holder: ViewHolder, position: Int) {
        val set = holder.itemView
        set.textViewDateNum.text = item.day
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.calendar
    }

}