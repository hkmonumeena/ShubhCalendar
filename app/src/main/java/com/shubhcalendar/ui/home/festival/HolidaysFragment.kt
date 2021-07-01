package com.shubhcalendar.ui.home.festival

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import coil.api.load
import com.google.android.material.card.MaterialCardView
import com.httpconnection.httpconnectionV2.Http
import com.httpconnection.httpconnectionV2.interfaces.IGetResponse
import com.httpconnection.httpconnectionV2.models.Exception
import com.shubhcalendar.R
import com.shubhcalendar.activities.ModelMonth
import com.shubhcalendar.databinding.FragmentHolidaysBinding
import com.shubhcalendar.ui.HomeNewActivity
import com.shubhcalendar.ui.profile.ProfileFragment
import com.shubhcalendar.utills.BaseFragment
import com.shubhcalendar.utills.Craft
import com.shubhcalendar.utills.GenricAdapter
import com.shubhcalendar.utills.ViewHolder
import com.trendyol.medusalib.navigator.transitionanimation.TransitionAnimationType
import com.tsongkha.spinnerdatepicker.DatePicker
import com.tsongkha.spinnerdatepicker.DatePickerDialog
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder
import kotlinx.android.synthetic.main.activity_home_new.*
import kotlinx.android.synthetic.main.calendar_item.view.textViewDate
import kotlinx.android.synthetic.main.calendar_item.view.textViewDayName
import kotlinx.android.synthetic.main.rv_show_holidays.view.*
import kotlinx.android.synthetic.main.rv_single_date.view.*
import kotlinx.coroutines.Job
import java.util.*

interface IHolidaysFragment {
    fun calendarBackForwardButtons(
        position: Int,
        mutableMapOf: MutableMap<String, String>,
        dateOfFestivs: String,
        dayName: String
    )

    fun getNextMonth()
    fun getPrivousMonth()
}

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HolidaysFragment : BaseFragment(), DatePickerDialog.OnDateSetListener, IHolidaysFragment,
    View.OnClickListener {
    lateinit var binding: FragmentHolidaysBinding
    private var currentMonth = 0
    var mutableList: MutableList<ModelMonth> = mutableListOf()
    val calendar = Calendar.getInstance()
    var dateOfMonth = 0
    var hour: Int = 0
    var minute: Int = 0
    var seconds: Int = 0
    lateinit var layoutManager: LinearLayoutManager
    lateinit var rvShowDate: RvShowDate
lateinit var job: Job
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHolidaysBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        job = Job()
        dateOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        binding.ivSerach.setOnClickListener(this)
        binding.relativeLayoutMenu.setOnClickListener(this)
        binding.rlDismiss.setOnClickListener(this)
        binding.cardViewProfile.setOnClickListener(this)

        binding.rvRecyclerView.apply {

            layoutManager = LinearLayoutManager(requireActivity())
        }
        layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)

        rvShowDate = RvShowDate(
            mutableList as ArrayList<ModelMonth>,
            this,
            binding.cardViewbackbtn,
            binding.cardViewforwordbtn,
            requireActivity(),
            4
        )
        initializeHorizontalCalendar()
    }

    private fun getFutureDatesOfCurrentMonth(): List<Date> {
        // get all next dates of current month
        currentMonth--
        currentMonth = calendar[Calendar.MONTH]
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

    private fun getDatesOfNextMonth(): List<Date> {
        currentMonth++ // + because we want next month
        if (currentMonth == 12) {
            // we will switch to january of next year, when we reach last month of year
            calendar.set(Calendar.YEAR, calendar[Calendar.YEAR] + 1)
            currentMonth = 0 // 0 == january
        }
        return getDates(mutableListOf())
    }


    private fun initializeHorizontalCalendar() {
        var oneTImeShowCurrentMonth = true
        getFutureDatesOfCurrentMonth().forEach {
            if (oneTImeShowCurrentMonth) {
                showHolidays(
                    mutableMapOf(
                        "month" to com.michalsvec.singlerowcalendar.utils.DateUtils.getMonthNumber(
                            it
                        ), "year" to com.michalsvec.singlerowcalendar.utils.DateUtils.getYear(it)
                    )
                )
                oneTImeShowCurrentMonth = false
            }
            mutableList.add(
                ModelMonth(
                    com.michalsvec.singlerowcalendar.utils.DateUtils.getDayNumber(
                        it
                    ),
                    com.michalsvec.singlerowcalendar.utils.DateUtils.getDayName(it),
                    com.michalsvec.singlerowcalendar.utils.DateUtils.getMonthNumber(it),
                    com.michalsvec.singlerowcalendar.utils.DateUtils.getYear(it),
                    hour.toString(),
                    minute.toString()
                )
            )
        }
        val snapHelper = PagerSnapHelper()

        snapHelper.attachToRecyclerView(binding.rvShowMainDate)
        binding.rvShowMainDate.layoutManager = layoutManager
        binding.rvShowMainDate.adapter = rvShowDate
        val calendar =  dateOfMonth

        binding.rvShowMainDate.scrollToPosition(calendar-1)
        rvShowDate.update(mutableList as java.util.ArrayList<ModelMonth>)
       /* calendarBackForwardButtons(
            calendar - 1,
            mutableMapOf("date" to "${mutableList[calendar].year}-${mutableList[calendar].monthNum}-${mutableList[calendar].day}"),
            mutableList[calendar].day,
            mutableList[calendar].dayname
        )*/
    }

    private fun getDates(list: MutableList<Date>): List<Date> {
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

    override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        dateOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        showHolidays(mutableMapOf("month" to monthOfYear.toString(), "year" to year.toString()))
    }

    override fun calendarBackForwardButtons(
        position: Int,
        mutableMapOf: MutableMap<String, String>,
        dateOfFestivs: String,
        dayName: String
    ) {
        binding.rvShowMainDate.scrollToPosition(position)
        rvShowDate.update(mutableList as ArrayList<ModelMonth>)
        showHolidays(mutableMapOf)
    }

    override fun getNextMonth() {
        mutableList.clear()
        getDatesOfNextMonth().forEach {
            mutableList.add(
                ModelMonth(
                    com.michalsvec.singlerowcalendar.utils.DateUtils.getDayNumber(
                        it
                    ),
                    com.michalsvec.singlerowcalendar.utils.DateUtils.getDayName(it),
                    com.michalsvec.singlerowcalendar.utils.DateUtils.getMonthNumber(it),
                    com.michalsvec.singlerowcalendar.utils.DateUtils.getYear(it),
                    hour.toString(),
                    minute.toString()
                )
            )
        }
        binding.rvShowMainDate.adapter = rvShowDate
    }

    override fun getPrivousMonth() {
        mutableList.clear()
        getDatesOfPreviousMonth().forEach {
            mutableList.add(
                ModelMonth(
                    com.michalsvec.singlerowcalendar.utils.DateUtils.getDayNumber(
                        it
                    ),
                    com.michalsvec.singlerowcalendar.utils.DateUtils.getDayName(it),
                    com.michalsvec.singlerowcalendar.utils.DateUtils.getMonthNumber(it),
                    com.michalsvec.singlerowcalendar.utils.DateUtils.getYear(it),
                    hour.toString(),
                    minute.toString()
                )
            )
        }
        binding.rvShowMainDate.adapter = rvShowDate
    }

    fun showHolidays(mutableMapOf: MutableMap<String, String>) {
        Http.Post("https://maestrosinfotech.org/shubh_calendar/appservice/process.php?action=show_holidays")
            .bodyParameter(mutableMapOf).build().executeString(object : IGetResponse {
            override fun onResponse(response: String?) {
                Log.e("flag--", "onResponse(HolidaysFragment.kt:244)-->>$response")
                if (job.isActive) {
                    when {
                        response?.isEmpty() == true -> {
                        }
                        response?.isNotEmpty() == true -> {
                            val getData = Http().createModelFromClass<DataShowHolidays>(response)
                            if (getData.result == "sucessfull") {

                                val dataList = arrayListOf<DataShowHolidays.Data.AllFalst>()

                                getData.data?.forEach {
                                    data ->
                                    data?.all_falst?.forEach {
                                        if (it != null) {
                                            dataList.add(it)
                                        }
                                    }
                                }

                                binding.rvRecyclerView.apply {
                                    layoutManager = LinearLayoutManager(requireActivity())
                                    adapter = RvShowHolidays(dataList)
                                }
                            } else {
                                val emptyList = arrayListOf<DataShowHolidays.Data.AllFalst>()
                                binding.rvRecyclerView.apply {
                                    layoutManager = LinearLayoutManager(requireActivity())
                                    adapter = RvShowHolidays(emptyList)
                                }
                                Toast.makeText(
                                    requireActivity(),
                                    "NO Data Available for ${mutableMapOf["month"]} - ${mutableMapOf["year"]}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                    Log.e("flag--", "onResponse(HolidaysFragment.kt:116)-->>$response")
                }
            }
            override fun onError(error: Exception?) {
            }
        })
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.ivSerach -> {
                SpinnerDatePickerDialogBuilder().context(requireActivity()).callback(this)
                    .spinnerTheme(R.style.NumberPickerStyle).showTitle(true).showDaySpinner(true)
                    .maxDate(2030, 0, 1).defaultDate(
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    )
                    //   .minDate(2000, 0, 1)
                    .build().show()
            }
            binding.rlDismiss -> {
                if (multipleStackNavigator?.canGoBack() == true) {
                    multipleStackNavigator?.goBack()
                }
            }
            binding.relativeLayoutMenu -> {
                (activity as HomeNewActivity).drawer.openDrawer(GravityCompat.END)
            }
            binding.cardViewProfile -> {
               multipleStackNavigator?.start(ProfileFragment(),TransitionAnimationType.RIGHT_TO_LEFT)
            }


        }
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }
}

class RvShowDate(
    items: ArrayList<ModelMonth>,
    val iMuhuratFrag: IHolidaysFragment,
    val cardViewbackbtn: MaterialCardView,
    val cardViewforwordbtn: MaterialCardView,
    val requireActivity: FragmentActivity,
    val pos: Int
) : GenricAdapter<ModelMonth>(items) {
    val calendar: Calendar = Calendar.getInstance()
    private var moveToNextMonth = false
    private var moveToPreviousMonth = false
    override fun getItemViewType(position: Int): Int {
        return R.layout.rv_single_date
    }

    override fun configure(item: ModelMonth, holder: ViewHolder, position: Int) {
        val set = holder.itemView
        set.textViewDayName.text = item.dayname
        set.textViewDate.text = item.day
        set.textViewMonthAndYear.text = """Month-${item.monthNum} Year-${item.year}"""
        cardViewforwordbtn.setOnClickListener {
            var nextPosition = if (position == items.size - 1) position else position + 1
            when (moveToNextMonth) {
                true -> {
                    iMuhuratFrag.getNextMonth()
                    moveToNextMonth = false
                }
                false -> {
                    if (nextPosition == items.size - 1) {
                        iMuhuratFrag.calendarBackForwardButtons(
                            nextPosition,
                            mutableMapOf(
                                "month" to items[nextPosition].monthNum,
                                "year" to items[nextPosition].year
                            ),
                            "",
                            ""
                        )
                        nextPosition + 1
                        moveToNextMonth = true
                    } else {
                        moveToNextMonth = false
                        iMuhuratFrag.calendarBackForwardButtons(
                            nextPosition,
                            mutableMapOf(
                                "month" to items[nextPosition].monthNum,
                                "year" to items[nextPosition].year
                            ),
                            "",
                            ""
                        )
                    }
                }
            }

            update(items)
        }
        cardViewbackbtn.setOnClickListener {
            var previousPosition = if (position == 0) {
                position
            } else {
                position - 1
            }
            when (moveToPreviousMonth) {
                true -> {
                    iMuhuratFrag.getPrivousMonth()
                    moveToPreviousMonth = false
                }
                false -> {
                    if (previousPosition == 0) {
                        iMuhuratFrag.calendarBackForwardButtons(
                            previousPosition,
                            mutableMapOf(
                                "month" to items[previousPosition].monthNum,
                                "year" to items[previousPosition].year
                            ),
                            "",
                            ""
                        )
                        moveToPreviousMonth = true
                    } else {
                        iMuhuratFrag.calendarBackForwardButtons(
                            previousPosition,
                            mutableMapOf(
                                "month" to items[previousPosition].monthNum,
                                "year" to items[previousPosition].year
                            ),
                            "",
                            ""
                        )
                        moveToPreviousMonth = false
                    }
                }
            }
            update(items)
        }
    }
}

private class RvShowHolidays(items: ArrayList<DataShowHolidays.Data.AllFalst>) :
    GenricAdapter<DataShowHolidays.Data.AllFalst>(items) {
    override fun configure(item: DataShowHolidays.Data.AllFalst, holder: ViewHolder, position: Int) {
        holder.itemView.textViewTitle.text = item.title
        holder.itemView.textViewState.text = item.states
        holder.itemView.textViewDate.text = item.date
        holder.itemView.holidayImage.load("https://maestrosinfotech.org/shubh_calendar/image/"+item.image)

    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.rv_show_holidays
    }
}

data class DataShowHolidays(val `data`: List<Data?>? = null, val result: String? = null) {
    data class Data(
        val all_falst: List<AllFalst?>? = null,
        val date: String? = null,
        val id: String? = null,
        val month: String? = null,
        val newdate: String? = null,
        val path: String? = null,
        val states: String? = null,
        val title: String? = null,
        val year: String? = null
    ) {
        data class AllFalst(
            val date: String? = null,
            val id: String? = null,
            val month: String? = null,
            val newdate: String? = null,
            val states: String? = null,
            val title: String? = null,
            val year: String? = null,
            val image: String? = null
        )
    }
}
