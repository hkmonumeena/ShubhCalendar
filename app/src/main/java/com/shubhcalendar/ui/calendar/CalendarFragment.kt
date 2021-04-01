package com.shubhcalendar.ui.calendar

import android.animation.Animator
import android.animation.ValueAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.shubhcalendar.R
import com.shubhcalendar.activities.ModelMonth
import com.shubhcalendar.databinding.FragmentCalendarBinding
import com.shubhcalendar.ui.HomeNewActivity
import com.shubhcalendar.utills.BaseFragment
import com.shubhcalendar.utills.GenricAdapter
import com.shubhcalendar.utills.ViewHolder
import kotlinx.android.synthetic.main.full_screen_calendar.view.*
import java.util.*

/*
* about this Fragment
* initializeClicks() in this methode initiliazed all the fragment clicks
* initializeFullScreenCalendar() in this methode initiliazed calendar adapter with values //0 for current month, 1 for previous month ,2 for next month
*
* */

class CalendarFragment : BaseFragment(), View.OnClickListener {
    lateinit var binding: FragmentCalendarBinding
    private val calendar = Calendar.getInstance()
    private var currentMonth = 0
    private var mutableMonthDateList: MutableList<ModelMonth> = mutableListOf()
    private var hour: Int = 0
    private var minute: Int = 0
    private var seconds: Int = 0
    private val monthNumListShow = arrayListOf(
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
    private val monthNumList = arrayListOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11")
    private lateinit var adapterMonth: AdapterCalendar
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalendarBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerDates.layoutManager = GridLayoutManager(requireActivity(), 7)
        initializeClicks()
        initializeFullScreenCalendar(0)
    }

    private fun initializeClicks() {
        binding.icMenu.setOnClickListener(this)
        binding.cardViewBack.setOnClickListener(this)
        binding.cardViewForward.setOnClickListener(this)
    }
    private fun initializeFullScreenCalendar(action: Int) {
        mutableMonthDateList.clear()
        if (action == 0) {
            getFutureDatesOfCurrentMonth().forEach {
                val getMonthName = com.michalsvec.singlerowcalendar.utils.DateUtils.getMonthName(it)
                val getYear = com.michalsvec.singlerowcalendar.utils.DateUtils.getYear(it)
                binding.texcViewDateCenter.text = """$getMonthName - $getYear"""
                if (com.michalsvec.singlerowcalendar.utils.DateUtils.getDayNumber(it) == "01") {
                    if (com.michalsvec.singlerowcalendar.utils.DateUtils.getDay3LettersName(it) == "Sun") {
                        mutableMonthDateList.add(
                            ModelMonth(
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getDayNumber(it),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getDay3LettersName(
                                    it
                                ),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getMonthNumber(it),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getYear(it),
                                hour.toString(),
                                minute.toString()
                            )
                        )
                    } else if (com.michalsvec.singlerowcalendar.utils.DateUtils.getDay3LettersName(
                            it
                        ) == "Mon"
                    ) {
                        mutableMonthDateList.add(
                            ModelMonth(
                                "-",
                                "-",
                                "-",
                                "-",
                                hour.toString(),
                                minute.toString()
                            )
                        )

                        mutableMonthDateList.add(
                            ModelMonth(
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getDayNumber(it),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getDay3LettersName(
                                    it
                                ),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getMonthNumber(it),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getYear(it),
                                hour.toString(),
                                minute.toString()
                            )
                        )
                    } else if (com.michalsvec.singlerowcalendar.utils.DateUtils.getDay3LettersName(
                            it
                        ) == "Tue"
                    ) {

                        for (i in 0 until 2) {
                            mutableMonthDateList.add(
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
                        mutableMonthDateList.add(
                            ModelMonth(
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getDayNumber(it),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getDay3LettersName(
                                    it
                                ),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getMonthNumber(it),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getYear(it),
                                hour.toString(),
                                minute.toString()
                            )
                        )
                    } else if (com.michalsvec.singlerowcalendar.utils.DateUtils.getDay3LettersName(
                            it
                        ) == "Wed"
                    ) {
                        for (i in 0 until 3) {
                            mutableMonthDateList.add(
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
                        mutableMonthDateList.add(
                            ModelMonth(
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getDayNumber(it),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getDay3LettersName(
                                    it
                                ),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getMonthNumber(it),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getYear(it),
                                hour.toString(),
                                minute.toString()
                            )
                        )
                    } else if (com.michalsvec.singlerowcalendar.utils.DateUtils.getDay3LettersName(
                            it
                        ) == "Thu"
                    ) {
                        for (i in 0 until 4) {
                            mutableMonthDateList.add(
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
                        mutableMonthDateList.add(
                            ModelMonth(
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getDayNumber(it),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getDay3LettersName(
                                    it
                                ),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getMonthNumber(it),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getYear(it),
                                hour.toString(),
                                minute.toString()
                            )
                        )

                    } else if (com.michalsvec.singlerowcalendar.utils.DateUtils.getDay3LettersName(
                            it
                        ) == "Fri"
                    ) {

                        for (i in 0 until 5) {
                            mutableMonthDateList.add(
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

                        mutableMonthDateList.add(
                            ModelMonth(
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getDayNumber(it),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getDay3LettersName(
                                    it
                                ),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getMonthNumber(it),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getYear(it),
                                hour.toString(),
                                minute.toString()
                            )
                        )

                    } else if (com.michalsvec.singlerowcalendar.utils.DateUtils.getDay3LettersName(
                            it
                        ) == "Sat"
                    ) {
                        for (i in 0 until 6) {
                            mutableMonthDateList.add(
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

                        mutableMonthDateList.add(
                            ModelMonth(
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getDayNumber(it),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getDay3LettersName(
                                    it
                                ),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getMonthNumber(it),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getYear(it),
                                hour.toString(),
                                minute.toString()
                            )
                        )

                    }
                } else {
                    mutableMonthDateList.add(
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
            binding.recyclerDates.adapter = AdapterCalendar(mutableMonthDateList as ArrayList<ModelMonth>)
        } else if (action == 1) {
            getDatesOfPreviousMonth().forEach {
                val getMonthName = com.michalsvec.singlerowcalendar.utils.DateUtils.getMonthName(it)
                val getYear = com.michalsvec.singlerowcalendar.utils.DateUtils.getYear(it)
                binding.texcViewDateCenter.text = """$getMonthName - $getYear"""
                if (com.michalsvec.singlerowcalendar.utils.DateUtils.getDayNumber(it) == "01") {
                    if (com.michalsvec.singlerowcalendar.utils.DateUtils.getDay3LettersName(it) == "Sun") {
                        mutableMonthDateList.add(
                            ModelMonth(
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getDayNumber(it),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getDay3LettersName(
                                    it
                                ),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getMonthNumber(it),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getYear(it),
                                hour.toString(),
                                minute.toString()
                            )
                        )
                    } else if (com.michalsvec.singlerowcalendar.utils.DateUtils.getDay3LettersName(
                            it
                        ) == "Mon"
                    ) {
                        mutableMonthDateList.add(
                            ModelMonth(
                                "-",
                                "-",
                                "-",
                                "-",
                                hour.toString(),
                                minute.toString()
                            )
                        )

                        mutableMonthDateList.add(
                            ModelMonth(
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getDayNumber(it),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getDay3LettersName(
                                    it
                                ),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getMonthNumber(it),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getYear(it),
                                hour.toString(),
                                minute.toString()
                            )
                        )
                    } else if (com.michalsvec.singlerowcalendar.utils.DateUtils.getDay3LettersName(
                            it
                        ) == "Tue"
                    ) {

                        for (i in 0 until 2) {
                            mutableMonthDateList.add(
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
                        mutableMonthDateList.add(
                            ModelMonth(
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getDayNumber(it),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getDay3LettersName(
                                    it
                                ),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getMonthNumber(it),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getYear(it),
                                hour.toString(),
                                minute.toString()
                            )
                        )
                    } else if (com.michalsvec.singlerowcalendar.utils.DateUtils.getDay3LettersName(
                            it
                        ) == "Wed"
                    ) {
                        for (i in 0 until 3) {
                            mutableMonthDateList.add(
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
                        mutableMonthDateList.add(
                            ModelMonth(
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getDayNumber(it),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getDay3LettersName(
                                    it
                                ),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getMonthNumber(it),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getYear(it),
                                hour.toString(),
                                minute.toString()
                            )
                        )
                    } else if (com.michalsvec.singlerowcalendar.utils.DateUtils.getDay3LettersName(
                            it
                        ) == "Thu"
                    ) {
                        for (i in 0 until 4) {
                            mutableMonthDateList.add(
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
                        mutableMonthDateList.add(
                            ModelMonth(
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getDayNumber(it),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getDay3LettersName(
                                    it
                                ),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getMonthNumber(it),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getYear(it),
                                hour.toString(),
                                minute.toString()
                            )
                        )

                    } else if (com.michalsvec.singlerowcalendar.utils.DateUtils.getDay3LettersName(
                            it
                        ) == "Fri"
                    ) {

                        for (i in 0 until 5) {
                            mutableMonthDateList.add(
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

                        mutableMonthDateList.add(
                            ModelMonth(
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getDayNumber(it),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getDay3LettersName(
                                    it
                                ),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getMonthNumber(it),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getYear(it),
                                hour.toString(),
                                minute.toString()
                            )
                        )

                    } else if (com.michalsvec.singlerowcalendar.utils.DateUtils.getDay3LettersName(
                            it
                        ) == "Sat"
                    ) {
                        for (i in 0 until 6) {
                            mutableMonthDateList.add(
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

                        mutableMonthDateList.add(
                            ModelMonth(
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getDayNumber(it),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getDay3LettersName(
                                    it
                                ),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getMonthNumber(it),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getYear(it),
                                hour.toString(),
                                minute.toString()
                            )
                        )

                    }
                } else {
                    mutableMonthDateList.add(
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
            binding.recyclerDates.adapter = AdapterCalendar(mutableMonthDateList as ArrayList<ModelMonth>)
        } else if (action == 2) {
            getDatesOfNextMonth().forEach {
                val getMonthName = com.michalsvec.singlerowcalendar.utils.DateUtils.getMonthName(it)
                val getYear = com.michalsvec.singlerowcalendar.utils.DateUtils.getYear(it)
                binding.texcViewDateCenter.text = """$getMonthName - $getYear"""
                if (com.michalsvec.singlerowcalendar.utils.DateUtils.getDayNumber(it) == "01") {
                    if (com.michalsvec.singlerowcalendar.utils.DateUtils.getDay3LettersName(it) == "Sun") {
                        mutableMonthDateList.add(
                            ModelMonth(
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getDayNumber(it),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getDay3LettersName(
                                    it
                                ),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getMonthNumber(it),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getYear(it),
                                hour.toString(),
                                minute.toString()
                            )
                        )
                    } else if (com.michalsvec.singlerowcalendar.utils.DateUtils.getDay3LettersName(
                            it
                        ) == "Mon"
                    ) {
                        mutableMonthDateList.add(
                            ModelMonth(
                                "-",
                                "-",
                                "-",
                                "-",
                                hour.toString(),
                                minute.toString()
                            )
                        )

                        mutableMonthDateList.add(
                            ModelMonth(
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getDayNumber(it),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getDay3LettersName(
                                    it
                                ),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getMonthNumber(it),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getYear(it),
                                hour.toString(),
                                minute.toString()
                            )
                        )
                    } else if (com.michalsvec.singlerowcalendar.utils.DateUtils.getDay3LettersName(
                            it
                        ) == "Tue"
                    ) {

                        for (i in 0 until 2) {
                            mutableMonthDateList.add(
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
                        mutableMonthDateList.add(
                            ModelMonth(
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getDayNumber(it),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getDay3LettersName(
                                    it
                                ),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getMonthNumber(it),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getYear(it),
                                hour.toString(),
                                minute.toString()
                            )
                        )
                    } else if (com.michalsvec.singlerowcalendar.utils.DateUtils.getDay3LettersName(
                            it
                        ) == "Wed"
                    ) {
                        for (i in 0 until 3) {
                            mutableMonthDateList.add(
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
                        mutableMonthDateList.add(
                            ModelMonth(
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getDayNumber(it),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getDay3LettersName(
                                    it
                                ),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getMonthNumber(it),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getYear(it),
                                hour.toString(),
                                minute.toString()
                            )
                        )
                    } else if (com.michalsvec.singlerowcalendar.utils.DateUtils.getDay3LettersName(
                            it
                        ) == "Thu"
                    ) {
                        for (i in 0 until 4) {
                            mutableMonthDateList.add(
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
                        mutableMonthDateList.add(
                            ModelMonth(
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getDayNumber(it),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getDay3LettersName(
                                    it
                                ),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getMonthNumber(it),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getYear(it),
                                hour.toString(),
                                minute.toString()
                            )
                        )

                    } else if (com.michalsvec.singlerowcalendar.utils.DateUtils.getDay3LettersName(
                            it
                        ) == "Fri"
                    ) {

                        for (i in 0 until 5) {
                            mutableMonthDateList.add(
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

                        mutableMonthDateList.add(
                            ModelMonth(
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getDayNumber(it),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getDay3LettersName(
                                    it
                                ),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getMonthNumber(it),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getYear(it),
                                hour.toString(),
                                minute.toString()
                            )
                        )

                    } else if (com.michalsvec.singlerowcalendar.utils.DateUtils.getDay3LettersName(
                            it
                        ) == "Sat"
                    ) {
                        for (i in 0 until 6) {
                            mutableMonthDateList.add(
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

                        mutableMonthDateList.add(
                            ModelMonth(
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getDayNumber(it),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getDay3LettersName(
                                    it
                                ),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getMonthNumber(it),
                                com.michalsvec.singlerowcalendar.utils.DateUtils.getYear(it),
                                hour.toString(),
                                minute.toString()
                            )
                        )

                    }
                } else {
                    mutableMonthDateList.add(
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
            binding.recyclerDates.adapter = AdapterCalendar(mutableMonthDateList as ArrayList<ModelMonth>)
        }

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
    fun startAnimation(action: Int) {
        // Intro animation configuration.
        val ANIMATION_DURATION: Long = 2000
        val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
        valueAnimator.addUpdateListener {
            val value = it.animatedValue as Float
            if(action==1){
                binding.cardViewBack.scaleX = value
                binding.cardViewBack.scaleY = value
            }else if (action==2) {
                binding.cardViewForward.scaleX = value
                binding.cardViewForward.scaleY = value
            }

        }
        valueAnimator.interpolator = BounceInterpolator()
        valueAnimator.duration = ANIMATION_DURATION

        // Set animator listener.

        valueAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {}

            override fun onAnimationEnd(p0: Animator?) {
                // Navigate to main activity on navigation end.

            }

            override fun onAnimationCancel(p0: Animator?) {}

            override fun onAnimationStart(p0: Animator?) {}

        })

        // Start animation.
        valueAnimator.start()
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.icMenu -> {
                (activity as HomeNewActivity).binding.drawer.openDrawer(GravityCompat.END)
            }
            binding.cardViewBack -> {
                initializeFullScreenCalendar(1)
                startAnimation(1)
            }
            binding.cardViewForward -> {
                initializeFullScreenCalendar(2)
                startAnimation(2)
            }
        }
    }
    class AdapterCalendar(items: ArrayList<ModelMonth>) : GenricAdapter<ModelMonth>(items) {
        override fun configure(item: ModelMonth, holder: ViewHolder, position: Int) {
            val set = holder.itemView
            if (item.day == "-") {
                set.cardViewCalendar.visibility = View.GONE
            }
            set.textViewDateNum.text = item.day
        }

        override fun getItemViewType(position: Int): Int {
            return R.layout.full_screen_calendar
        }

    }
}


