package com.shubhcalendar.ui.calendar

import android.animation.Animator
import android.animation.ValueAnimator
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import coil.api.load
import com.e.mylibrary.Post
import com.example.kotlinelearn.roomdatabase.AppDatabase
import com.httpconnection.httpconnectionV2.Http
import com.httpconnection.httpconnectionV2.interfaces.IGetResponse
import com.httpconnection.httpconnectionV2.models.Exception
import com.michalsvec.singlerowcalendar.utils.DateUtils
import com.shubhcalendar.R
import com.shubhcalendar.activities.NotificationActivity
import com.shubhcalendar.databinding.FragmentCalendarBinding
import com.shubhcalendar.roomdb.DbFestivals
import com.shubhcalendar.ui.HomeNewActivity
import com.shubhcalendar.utills.Api.SHOW_HOLIDDAYS
import com.shubhcalendar.utills.BaseFragment
import com.shubhcalendar.utills.Craft.startActivity
import com.shubhcalendar.utills.GenricAdapter
import com.shubhcalendar.utills.ViewHolder
import kotlinx.android.synthetic.main.full_screen_calendar.view.*
import kotlinx.android.synthetic.main.items_festivals.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread
import kotlin.coroutines.CoroutineContext

class CalendarFragment : BaseFragment(), View.OnClickListener, CoroutineScope {
    lateinit var binding: FragmentCalendarBinding
    private val calendar = Calendar.getInstance()
    private var currentMonth = 0
    private var mutableMonthDateList: MutableList<ModelCalendarMonth> = mutableListOf()
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
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    lateinit var job: Job
    val concatList = arrayListOf<ModelConcatFestivalAndMonth?>()
    val concatFestivs: ArrayList<DataShowFestival.Data?>? = arrayListOf()
    val listIsFestival: ArrayList<ModelIsFestival?> = arrayListOf()
    val listFromLocalDbFestivals: ArrayList<DbFestivals?> = arrayListOf()
    /// below db variables

    lateinit var locaDb: AppDatabase

    lateinit var fullRvCal: AdapterCalendar
    var isLocalStoreDataAvailable = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalendarBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        job = Job()
        fullRvCal = AdapterCalendar(concatList)
        locaDb = AppDatabase.getAppDatabase(requireActivity())!!
        binding.recyclerDates.adapter = fullRvCal
        binding.recyclerDates.layoutManager = GridLayoutManager(requireActivity(), 7)
        initializeClicks()
        initializeFullScreenCalendar(0)


    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }

    private fun initializeClicks() {
        binding.icMenu.setOnClickListener(this)
        binding.cardViewBack.setOnClickListener(this)
        binding.cardViewForward.setOnClickListener(this)
        binding.cardViewProfileUser.setOnClickListener(this)
        binding.rlDismiss.setOnClickListener(this)
        binding.imageViewNotification.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v) {
            binding.icMenu -> {
                (activity as HomeNewActivity).binding.drawer.openDrawer(GravityCompat.END)
            }
            binding.rlDismiss -> {
                if (multipleStackNavigator?.canGoBack() == true) multipleStackNavigator?.goBack()

                //   (activity as HomeNewActivity).multipleStackNavigator.switchTab(2)
            }
            binding.cardViewBack -> {
                initializeFullScreenCalendar(1)
                startAnimation(1)
            }
            binding.cardViewForward -> {
                initializeFullScreenCalendar(2)
                startAnimation(2)
            }

            binding.cardViewProfileUser -> {
                /*    multipleStackNavigator?.start(
                        ProfileFragment(),
                        TransitionAnimationType.FADE_IN_OUT
                    )*/

                multipleStackNavigator?.reset(1)
                Handler().postDelayed({
                    multipleStackNavigator?.switchTab(1)
                }, 500)
            }

            binding.imageViewNotification -> {
                requireActivity().startActivity<NotificationActivity>()
            }
        }
    }


    private fun initializeFullScreenCalendar(action: Int) {
        var getMonthNum: String? = null
        var getYearNum: String? = null
        mutableMonthDateList.clear()
        concatList.clear()
        concatFestivs?.clear()
        listIsFestival.clear()
        if (action == 0) {
            getFutureDatesOfCurrentMonth().forEach {
                val getMonthName = DateUtils.getMonthName(it)
                val getYear = DateUtils.getYear(it)
                getMonthNum = DateUtils.getMonthNumber(it)
                getYearNum = DateUtils.getYear(it)
                binding.texcViewDateCenter.text = """$getMonthName - $getYear"""
                if (DateUtils.getDayNumber(it) == "01") {
                    if (DateUtils.getDay3LettersName(it) == "Sun") {
                        mutableMonthDateList.add(
                            ModelCalendarMonth(
                                DateUtils.getDayNumber(it),
                                DateUtils.getDay3LettersName(it),
                                DateUtils.getMonthNumber(it),
                                DateUtils.getYear(it),
                                hour.toString(),
                                minute.toString()
                            )
                        )
                    } else if (DateUtils.getDay3LettersName(it) == "Mon") {
                        mutableMonthDateList.add(
                            ModelCalendarMonth(
                                "-",
                                "-",
                                "-",
                                "-",
                                hour.toString(),
                                minute.toString()
                            )
                        )

                        mutableMonthDateList.add(
                            ModelCalendarMonth(
                                DateUtils.getDayNumber(it),
                                DateUtils.getDay3LettersName(it),
                                DateUtils.getMonthNumber(it),
                                DateUtils.getYear(it),
                                hour.toString(),
                                minute.toString()
                            )
                        )
                    } else if (DateUtils.getDay3LettersName(it) == "Tue") {
                        for (i in 0 until 2) {
                            mutableMonthDateList.add(
                                ModelCalendarMonth(
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
                            ModelCalendarMonth(
                                DateUtils.getDayNumber(it),
                                DateUtils.getDay3LettersName(it),
                                DateUtils.getMonthNumber(it),
                                DateUtils.getYear(it),
                                hour.toString(),
                                minute.toString()
                            )
                        )
                    } else if (DateUtils.getDay3LettersName(it) == "Wed") {
                        for (i in 0 until 3) {
                            mutableMonthDateList.add(
                                ModelCalendarMonth(
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
                            ModelCalendarMonth(
                                DateUtils.getDayNumber(it),
                                DateUtils.getDay3LettersName(it),
                                DateUtils.getMonthNumber(it),
                                DateUtils.getYear(it),
                                hour.toString(),
                                minute.toString()
                            )
                        )
                    } else if (DateUtils.getDay3LettersName(it) == "Thu") {
                        for (i in 0 until 4) {
                            mutableMonthDateList.add(
                                ModelCalendarMonth(
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
                            ModelCalendarMonth(
                                DateUtils.getDayNumber(it),
                                DateUtils.getDay3LettersName(it),
                                DateUtils.getMonthNumber(it),
                                DateUtils.getYear(it),
                                hour.toString(),
                                minute.toString()
                            )
                        )
                    } else if (DateUtils.getDay3LettersName(it) == "Fri") {
                        for (i in 0 until 5) {
                            mutableMonthDateList.add(
                                ModelCalendarMonth(
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
                            ModelCalendarMonth(
                                DateUtils.getDayNumber(it),
                                DateUtils.getDay3LettersName(it),
                                DateUtils.getMonthNumber(it),
                                DateUtils.getYear(it),
                                hour.toString(),
                                minute.toString()
                            )
                        )
                    } else if (DateUtils.getDay3LettersName(it) == "Sat") {
                        for (i in 0 until 6) {
                            mutableMonthDateList.add(
                                ModelCalendarMonth(
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
                            ModelCalendarMonth(
                                DateUtils.getDayNumber(it),
                                DateUtils.getDay3LettersName(it),
                                DateUtils.getMonthNumber(it),
                                DateUtils.getYear(it),
                                hour.toString(),
                                minute.toString()
                            )
                        )
                    }
                } else {
                    mutableMonthDateList.add(
                        ModelCalendarMonth(
                            DateUtils.getDayNumber(it),
                            DateUtils.getDay3LettersName(it),
                            DateUtils.getMonthNumber(it),
                            DateUtils.getYear(it),
                            hour.toString(),
                            minute.toString()
                        )
                    )
                }
            }
            val list = arrayListOf<ModelConcatFestivalAndMonth?>()
            mutableMonthDateList.forEach {
                list.add(ModelConcatFestivalAndMonth(it, null, null))
            }
            listFromLocalDbFestivals.clear()
            if (getMonthNum?.toInt()!! < 10) {
                getMonthNum = getMonthNum?.get(1).toString()
            }

            Toast.makeText(requireContext(), "$getMonthNum", Toast.LENGTH_SHORT).show()
            locaDb.getFestivalsDb().getUserWithId(getMonthNum!!, getYearNum.toString())
                .observe(requireActivity()) {
                    listFromLocalDbFestivals.addAll(it)
                    isLocalStoreDataAvailable = listFromLocalDbFestivals.size != 0
                    val dbJob = job
                    showFestivals(
                        getMonthNum,
                        getYearNum,
                        mutableMonthDateList as ArrayList<ModelCalendarMonth>,
                        dbJob
                    )
                    binding.recyclerDates.adapter = AdapterCalendar(list)

                }

        } else if (action == 1) {
            getDatesOfPreviousMonth().forEach {
                val getMonthName = DateUtils.getMonthName(it)
                val getYear = DateUtils.getYear(it)
                getMonthNum = DateUtils.getMonthNumber(it)
                getYearNum = DateUtils.getYear(it)
                binding.texcViewDateCenter.text = """$getMonthName - $getYear"""
                if (DateUtils.getDayNumber(it) == "01") {
                    if (DateUtils.getDay3LettersName(it) == "Sun") {
                        mutableMonthDateList.add(
                            ModelCalendarMonth(
                                DateUtils.getDayNumber(it),
                                DateUtils.getDay3LettersName(it),
                                DateUtils.getMonthNumber(it),
                                DateUtils.getYear(it),
                                hour.toString(),
                                minute.toString()
                            )
                        )
                    } else if (DateUtils.getDay3LettersName(it) == "Mon") {
                        mutableMonthDateList.add(
                            ModelCalendarMonth(
                                "-",
                                "-",
                                "-",
                                "-",
                                hour.toString(),
                                minute.toString()
                            )
                        )

                        mutableMonthDateList.add(
                            ModelCalendarMonth(
                                DateUtils.getDayNumber(it),
                                DateUtils.getDay3LettersName(it),
                                DateUtils.getMonthNumber(it),
                                DateUtils.getYear(it),
                                hour.toString(),
                                minute.toString()
                            )
                        )
                    } else if (DateUtils.getDay3LettersName(it) == "Tue") {
                        for (i in 0 until 2) {
                            mutableMonthDateList.add(
                                ModelCalendarMonth(
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
                            ModelCalendarMonth(
                                DateUtils.getDayNumber(it),
                                DateUtils.getDay3LettersName(it),
                                DateUtils.getMonthNumber(it),
                                DateUtils.getYear(it),
                                hour.toString(),
                                minute.toString()
                            )
                        )
                    } else if (DateUtils.getDay3LettersName(it) == "Wed") {
                        for (i in 0 until 3) {
                            mutableMonthDateList.add(
                                ModelCalendarMonth(
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
                            ModelCalendarMonth(
                                DateUtils.getDayNumber(it),
                                DateUtils.getDay3LettersName(it),
                                DateUtils.getMonthNumber(it),
                                DateUtils.getYear(it),
                                hour.toString(),
                                minute.toString()
                            )
                        )
                    } else if (DateUtils.getDay3LettersName(it) == "Thu") {
                        for (i in 0 until 4) {
                            mutableMonthDateList.add(
                                ModelCalendarMonth(
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
                            ModelCalendarMonth(
                                DateUtils.getDayNumber(it),
                                DateUtils.getDay3LettersName(it),
                                DateUtils.getMonthNumber(it),
                                DateUtils.getYear(it),
                                hour.toString(),
                                minute.toString()
                            )
                        )
                    } else if (DateUtils.getDay3LettersName(it) == "Fri") {
                        for (i in 0 until 5) {
                            mutableMonthDateList.add(
                                ModelCalendarMonth(
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
                            ModelCalendarMonth(
                                DateUtils.getDayNumber(it),
                                DateUtils.getDay3LettersName(it),
                                DateUtils.getMonthNumber(it),
                                DateUtils.getYear(it),
                                hour.toString(),
                                minute.toString()
                            )
                        )
                    } else if (DateUtils.getDay3LettersName(it) == "Sat") {
                        for (i in 0 until 6) {
                            mutableMonthDateList.add(
                                ModelCalendarMonth(
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
                            ModelCalendarMonth(
                                DateUtils.getDayNumber(it),
                                DateUtils.getDay3LettersName(it),
                                DateUtils.getMonthNumber(it),
                                DateUtils.getYear(it),
                                hour.toString(),
                                minute.toString()
                            )
                        )
                    }
                } else {
                    mutableMonthDateList.add(
                        ModelCalendarMonth(
                            DateUtils.getDayNumber(it),
                            DateUtils.getDay3LettersName(it),
                            DateUtils.getMonthNumber(it),
                            DateUtils.getYear(it),
                            hour.toString(),
                            minute.toString()
                        )
                    )
                }
            }
            val list = arrayListOf<ModelConcatFestivalAndMonth?>()
            mutableMonthDateList.forEach {
                list.add(ModelConcatFestivalAndMonth(it, null, null))
            }
            listFromLocalDbFestivals.clear()
            if (getMonthNum?.toInt()!! < 10) {
                getMonthNum = getMonthNum?.get(1).toString()
            }
            locaDb.getFestivalsDb().getUserWithId(getMonthNum.toString(), getYearNum.toString())
                .observe(requireActivity()) {
                    listFromLocalDbFestivals.addAll(it)
                    binding.recyclerDates.adapter = AdapterCalendar(list)
                    isLocalStoreDataAvailable = listFromLocalDbFestivals.size != 0
                    val dbJob = job
                    showFestivals(
                        getMonthNum,
                        getYearNum,
                        mutableMonthDateList as ArrayList<ModelCalendarMonth>, dbJob
                    )


                }


        } else if (action == 2) {
            getDatesOfNextMonth().forEach {
                val getMonthName = DateUtils.getMonthName(it)
                val getYear = DateUtils.getYear(it)
                getMonthNum = DateUtils.getMonthNumber(it)
                getYearNum = DateUtils.getYear(it)
                binding.texcViewDateCenter.text = """$getMonthName - $getYear"""
                if (DateUtils.getDayNumber(it) == "01") {
                    if (DateUtils.getDay3LettersName(it) == "Sun") {
                        mutableMonthDateList.add(
                            ModelCalendarMonth(
                                DateUtils.getDayNumber(it),
                                DateUtils.getDay3LettersName(it),
                                DateUtils.getMonthNumber(it),
                                DateUtils.getYear(it),
                                hour.toString(),
                                minute.toString()
                            )
                        )
                    } else if (DateUtils.getDay3LettersName(it) == "Mon") {
                        mutableMonthDateList.add(
                            ModelCalendarMonth(
                                "-",
                                "-",
                                "-",
                                "-",
                                hour.toString(),
                                minute.toString()
                            )
                        )

                        mutableMonthDateList.add(
                            ModelCalendarMonth(
                                DateUtils.getDayNumber(it),
                                DateUtils.getDay3LettersName(it),
                                DateUtils.getMonthNumber(it),
                                DateUtils.getYear(it),
                                hour.toString(),
                                minute.toString()
                            )
                        )
                    } else if (DateUtils.getDay3LettersName(it) == "Tue") {
                        for (i in 0 until 2) {
                            mutableMonthDateList.add(
                                ModelCalendarMonth(
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
                            ModelCalendarMonth(
                                DateUtils.getDayNumber(it),
                                DateUtils.getDay3LettersName(it),
                                DateUtils.getMonthNumber(it),
                                DateUtils.getYear(it),
                                hour.toString(),
                                minute.toString()
                            )
                        )
                    } else if (DateUtils.getDay3LettersName(it) == "Wed") {
                        for (i in 0 until 3) {
                            mutableMonthDateList.add(
                                ModelCalendarMonth(
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
                            ModelCalendarMonth(
                                DateUtils.getDayNumber(it),
                                DateUtils.getDay3LettersName(it),
                                DateUtils.getMonthNumber(it),
                                DateUtils.getYear(it),
                                hour.toString(),
                                minute.toString()
                            )
                        )
                    } else if (DateUtils.getDay3LettersName(it) == "Thu") {
                        for (i in 0 until 4) {
                            mutableMonthDateList.add(
                                ModelCalendarMonth(
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
                            ModelCalendarMonth(
                                DateUtils.getDayNumber(it),
                                DateUtils.getDay3LettersName(it),
                                DateUtils.getMonthNumber(it),
                                DateUtils.getYear(it),
                                hour.toString(),
                                minute.toString()
                            )
                        )
                    } else if (DateUtils.getDay3LettersName(it) == "Fri") {
                        for (i in 0 until 5) {
                            mutableMonthDateList.add(
                                ModelCalendarMonth(
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
                            ModelCalendarMonth(
                                DateUtils.getDayNumber(it),
                                DateUtils.getDay3LettersName(it),
                                DateUtils.getMonthNumber(it),
                                DateUtils.getYear(it),
                                hour.toString(),
                                minute.toString()
                            )
                        )
                    } else if (DateUtils.getDay3LettersName(it) == "Sat") {
                        for (i in 0 until 6) {
                            mutableMonthDateList.add(
                                ModelCalendarMonth(
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
                            ModelCalendarMonth(
                                DateUtils.getDayNumber(it),
                                DateUtils.getDay3LettersName(it),
                                DateUtils.getMonthNumber(it),
                                DateUtils.getYear(it),
                                hour.toString(),
                                minute.toString()
                            )
                        )
                    }
                } else {
                    mutableMonthDateList.add(
                        ModelCalendarMonth(
                            DateUtils.getDayNumber(it),
                            DateUtils.getDay3LettersName(it),
                            DateUtils.getMonthNumber(it),
                            DateUtils.getYear(it),
                            hour.toString(),
                            minute.toString()
                        )
                    )
                }
            }
            val list = arrayListOf<ModelConcatFestivalAndMonth?>()
            mutableMonthDateList.forEach {
                list.add(ModelConcatFestivalAndMonth(it, null, null))
            }
            listFromLocalDbFestivals.clear()
            if (getMonthNum?.toInt()!! < 10) {
                getMonthNum = getMonthNum?.get(1).toString()
            }
            locaDb.getFestivalsDb().getUserWithId(getMonthNum.toString(), getYearNum.toString())
                .observe(requireActivity()) {

                    listFromLocalDbFestivals.addAll(it)
                    binding.recyclerDates.adapter = AdapterCalendar(list)
                    isLocalStoreDataAvailable = listFromLocalDbFestivals.size != 0
                    val dbJob = job
                    showFestivals(
                        getMonthNum,
                        getYearNum,
                        mutableMonthDateList as ArrayList<ModelCalendarMonth>,
                        dbJob
                    )


                }


        }
    }

    private fun getDatesOfNextMonth(): List<Date> {
        currentMonth++ // + because we want next month
        if (currentMonth == 12) {
            calendar.set(Calendar.YEAR, calendar[Calendar.YEAR] + 1)
            currentMonth = 0 // 0 == january
        }
        return getDates(mutableListOf())
    }

    private fun getDatesOfPreviousMonth(): List<Date> {
        currentMonth-- // - because we want previous month
        if (currentMonth == -1) {
            calendar.set(Calendar.YEAR, calendar[Calendar.YEAR] - 1)
            currentMonth = 11 // 11 == december
        }
        return getDates(mutableListOf())
    }

    private fun getFutureDatesOfCurrentMonth(): List<Date> {
        // get all next dates of current month
        currentMonth--
        currentMonth = calendar[Calendar.MONTH]

        return getDates(mutableListOf())
    }

    private fun getAnyDate(selectedMonth: Int): List<Date> {
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
            if (calendar[Calendar.MONTH] == currentMonth) list.add(calendar.time)
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
            if (action == 1) {
                binding.cardViewBack.scaleX = value
                binding.cardViewBack.scaleY = value
            } else if (action == 2) {
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


    private fun showFestivals(
        month: String?,
        year: String?,
        mutableMonthDateList: ArrayList<ModelCalendarMonth>,
        dbJob: Job
    ) {
        val emty = arrayListOf<DataShowFestival.Data?>()
        if (isLocalStoreDataAvailable) {
            var count: Boolean
            for (elements in mutableMonthDateList) {
                count = true
                for (i in 0 until listFromLocalDbFestivals.size) {
                    if (elements.day == listFromLocalDbFestivals[i]?.newdate?.split("-")?.get(2)) {
                        val dbvalue = listFromLocalDbFestivals[i]
                        emty.add(null)
                        Log.e(
                            "fdlkhhfioh",
                            "showFestivals(CalendarFragment.kt:783)-->>${
                                listFromLocalDbFestivals[i]?.newdate?.split("-")?.get(2)
                            }"
                        )
                        concatFestivs?.add(
                            DataShowFestival.Data(
                                dbvalue!!.id,
                                dbvalue.title,
                                dbvalue.states,
                                dbvalue.date,
                                dbvalue.month,
                                dbvalue.year,
                                dbvalue.newdate,
                                dbvalue.image,
                                null,
                                dbvalue.path
                            )
                        )
                        listIsFestival.add(ModelIsFestival(true))
                        count = false
                    } else {
                    }

                }
                if (count) {
                    concatFestivs?.add(null)
                    listIsFestival.add(ModelIsFestival(false))
                }


            }
            concatList.clear()
            for (i in 0 until mutableMonthDateList.size) {
                concatList.add(
                    ModelConcatFestivalAndMonth(
                        mutableMonthDateList[i], concatFestivs?.get(i),
                        listIsFestival.get(i)
                    )
                )
            }

            binding.festivalRecyclerView.adapter =
                AdapterShowFestivs(emty, listFromLocalDbFestivals, isLocalStoreDataAvailable)
            binding.festivalRecyclerView.layoutManager =
                LinearLayoutManager(requireActivity())

            binding.recyclerDates.adapter = AdapterCalendar(concatList)
        } else {
            emty.clear()
            binding.festivalRecyclerView.adapter =
                AdapterShowFestivs(emty, listFromLocalDbFestivals, isLocalStoreDataAvailable)
            Http.Post(SHOW_HOLIDDAYS)
                .bodyParameter(mutableMapOf("month" to month!!, "year" to year!!))
                .build()
                .executeString(object : IGetResponse {
                    override fun onResponse(response: String?) {
                        if (job.isActive) {
                            if (response?.isNotBlank() == true) {
                                if (!isLocalStoreDataAvailable) {
                                    val data = Post.createModelFromClass<DataShowFestival>(response)
                                    if (data.result == "sucessfull") {
                                        concatList.clear()
                                        var count: Boolean
                                        data.data?.forEach {
                                            thread {
                                                locaDb.getFestivalsDb().insert(
                                                    DbFestivals(
                                                        it?.id!!.toInt(),
                                                        it.title,
                                                        it.states,
                                                        it.date,
                                                        it.month,
                                                        it.year,
                                                        it.newdate,
                                                        it.image,
                                                        it.path
                                                    )
                                                )

                                            }
                                        }
                                        for (elements in mutableMonthDateList) {
                                            count = true
                                            for (i in 0 until data.data?.size!!) {
                                                if (elements.day == data.data.get(i)?.newdate?.split("-")
                                                        ?.get(2)
                                                ) {
                                                    concatFestivs?.add(data.data.get(i))
                                                    listIsFestival.add(ModelIsFestival(true))
                                                    count = false
                                                } else {
                                                }
                                            }
                                            if (count) {
                                                concatFestivs?.add(null)
                                                listIsFestival.add(ModelIsFestival(false))
                                            }
                                        }
                                        for (i in 0 until mutableMonthDateList.size) {
                                            concatList.add(
                                                ModelConcatFestivalAndMonth(
                                                    mutableMonthDateList[i],
                                                    concatFestivs?.get(i),
                                                    listIsFestival[i],
                                                )
                                            )
                                        }
                                        binding.festivalRecyclerView.adapter =
                                            AdapterShowFestivs(
                                                data.data as ArrayList<DataShowFestival.Data?>,
                                                listFromLocalDbFestivals,
                                                isLocalStoreDataAvailable
                                            )
                                        binding.festivalRecyclerView.layoutManager =
                                            LinearLayoutManager(requireActivity())
                                        binding.recyclerDates.adapter =
                                            AdapterCalendar(concatList)
                                    } else {
                                        val emty = arrayListOf<DataShowFestival.Data?>()
                                        concatList.clear()
                                        for (i in 0 until mutableMonthDateList.size) {
                                            concatList.add(
                                                ModelConcatFestivalAndMonth(
                                                    mutableMonthDateList.get(i),
                                                    null,
                                                    null
                                                )
                                            )
                                        }
                                        binding.recyclerDates.adapter =
                                            AdapterCalendar(concatList)
                                        binding.festivalRecyclerView.adapter =
                                            AdapterShowFestivs(
                                                emty,
                                                listFromLocalDbFestivals,
                                                isLocalStoreDataAvailable
                                            )
                                        binding.festivalRecyclerView.layoutManager =
                                            LinearLayoutManager(requireActivity())
                                    }
                                }
                            }
                        }
                    }

                    override fun onError(error: Exception?) {
                        Log.e("flag--", "showFestivals(CalendarFragment.kt:892)-->>")
                    }
                })

        }


    }

    class AdapterShowFestivs(
        items: ArrayList<DataShowFestival.Data?>,
        val listFromLocalDbFestivals: ArrayList<DbFestivals?>,
        val isLocalStoreDataAvailable: Boolean
    ) :
        GenricAdapter<DataShowFestival.Data?>(items) {
        override fun configure(item: DataShowFestival.Data?, holder: ViewHolder, position: Int) {
            val set = holder.itemView

            if (isLocalStoreDataAvailable) {
                set.textViewName.text = """${listFromLocalDbFestivals.get(position)?.title} (${
                    listFromLocalDbFestivals.get(position)?.newdate
                })"""
            } else {
                set.textViewName.text = """${item?.title} (${item?.newdate})"""
            }

        }

        override fun getItemViewType(position: Int): Int {
            return R.layout.items_festivals
        }
    }

    class AdapterCalendar(items: ArrayList<ModelConcatFestivalAndMonth?>) :
        GenricAdapter<ModelConcatFestivalAndMonth?>(items) {
        private val simpleDateFormat by lazy { SimpleDateFormat("yyyy.MM.dd") }
        private val currentDateAndTime: String = simpleDateFormat.format(Date())
        override fun configure(
            item: ModelConcatFestivalAndMonth?,
            holder: ViewHolder,
            position: Int
        ) {
            val set = holder.itemView
            if (item?.modelCalendarMonth?.day == "-") {
                set.cardViewCalendar.visibility = View.GONE
            }
            if (item?.isFestival?.isFestival == true) {
                set.cardViewIsFestiv.visibility = View.VISIBLE
                set.textViewDateNum.visibility = View.GONE
                set.imgFestival.visibility = View.VISIBLE
                set.imgFestival.load(R.drawable.diwali)
                Log.e("flag--", "configure(CalendarFragment.kt:928)-->>$item")
            }
            if (currentDateAndTime == "${item?.modelCalendarMonth?.year}.${item?.modelCalendarMonth?.monthNum}.${item?.modelCalendarMonth?.day}" && item?.isFestival?.isFestival != true) {
                set.cardViewCurrent.isVisible = true
                set.textViewDateNumCurrent.text = item?.modelCalendarMonth?.day
                set.textViewDateNum.isVisible = false
            }
            set.textViewDateNum.text = item?.modelCalendarMonth?.day
            set.textViewDateNum2.text = item?.modelCalendarMonth?.day
        }

        override fun getItemViewType(position: Int): Int {
            return R.layout.full_screen_calendar
        }
    }
}

data class ModelConcatFestivalAndMonth(
    val modelCalendarMonth: ModelCalendarMonth?,
    val dataShowFestival: DataShowFestival.Data?,
    val isFestival: ModelIsFestival?
)

data class ModelIsFestival(val isFestival: Boolean = false)
data class ModelCalendarMonth(
    val day: String,
    val dayname: String,
    val monthNum: String,
    val year: String,
    val hour: String,
    val minute: String
)


