package com.shubhcalendar.ui.home.festival

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.google.android.material.card.MaterialCardView
import com.httpconnection.httpconnectionV2.Http
import com.httpconnection.httpconnectionV2.interfaces.IGetResponse
import com.httpconnection.httpconnectionV2.models.Exception
import com.shubhcalendar.R
import com.shubhcalendar.activities.ModelMonth
import com.shubhcalendar.databinding.FragmentShowFestivalsBinding
import com.shubhcalendar.utills.Api
import com.shubhcalendar.utills.BaseFragment
import com.shubhcalendar.utills.GenricAdapter
import com.shubhcalendar.utills.ViewHolder
import com.tsongkha.spinnerdatepicker.DatePicker
import com.tsongkha.spinnerdatepicker.DatePickerDialog
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder
import kotlinx.android.synthetic.main.calendar_item.view.*
import kotlinx.coroutines.Job
import java.util.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

interface IShowFestival {
    fun calendarBackForwardButtons(
        position: Int,
        mutableMapOf: MutableMap<String, String>,
        dateOfFestivs: String,
        dayName: String
    )

    fun getNextMonth()
    fun getPrivousMonth()
}

class ShowFestivals : BaseFragment(), IShowFestival, DatePickerDialog.OnDateSetListener {
    private var param1: String? = null
    private var param2: String? = null
    private var currentMonth = 0
    var mutableList: MutableList<ModelMonth> = mutableListOf()
    val calendar = Calendar.getInstance()
    var dateOfMonth = 0
    var hour: Int = 0
    var minute: Int = 0
    var seconds: Int = 0
    val job = Job()
    lateinit var layoutManager: LinearLayoutManager
    lateinit var rvShowDate: RvShowDate
    lateinit var binding: FragmentShowFestivalsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShowFestivalsBinding.inflate(layoutInflater)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = ShowFestivals().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM1, param1)
                putString(ARG_PARAM2, param2)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rlDismiss.setOnClickListener{
        if (multipleStackNavigator?.canGoBack() == true) multipleStackNavigator?.goBack()
        }
        binding.ivSerach.setOnClickListener {
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
        showFestival(mutableMapOf("month" to "04", "year" to "2021"))
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }

    private fun showFestival(mutableMap: MutableMap<String, String>) {
        Http.Post(Api.show_festival)
            .bodyParameter(mutableMap)
            .build()
            .executeString(object : IGetResponse {
                override fun onResponse(response: String?) {
                    if (job.isActive) {
                        when {
                            response?.isEmpty() == true -> {
                                Toast.makeText(
                                    requireActivity(),
                                    "No Data Found",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                            response?.isNotEmpty() == true -> {

                                val getData =
                                    Http().createModelFromClass<DataShowFestival>(response)
                                if (getData.result == "sucessfull") {
                                    val setDataToRv = arrayListOf<DataShowFestival.Data.AllFalst>()
                                    var c = 0
                                    getData.data?.forEach {
                                        it?.all_falst?.get(c)?.let { it1 -> setDataToRv.add(it1) }
                                        c++
                                    }
                                    binding.recyclerViewShowFestival.apply {
                                        layoutManager = LinearLayoutManager(requireActivity())
                                        adapter = RvShowFestival(setDataToRv)
                                    }
                                } else {
                                    val emptyList = arrayListOf<DataShowFestival.Data.AllFalst>()
                                    Toast.makeText(
                                        requireActivity(),
                                        "No Data Found",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    binding.recyclerViewShowFestival.apply {
                                        layoutManager = LinearLayoutManager(requireActivity())
                                        adapter = RvShowFestival(emptyList)
                                    }

                                }
                            }
                        }

                    }
                }

                override fun onError(error: Exception?) {

                }
            })
    }


    class RvShowDate(
        items: java.util.ArrayList<ModelMonth>,
        val iMuhuratFrag: IShowFestival,
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


    private class RvShowFestival(items: ArrayList<DataShowFestival.Data.AllFalst>) :
        GenricAdapter<DataShowFestival.Data.AllFalst>(items) {
        override fun configure(
            item: DataShowFestival.Data.AllFalst,
            holder: ViewHolder,
            position: Int
        ) {
            val set = holder.itemView
            set.findViewById<TextView>(R.id.textViewTitle).text = item.title
            set.findViewById<TextView>(R.id.textViewDate).text = item.day
            set.findViewById<TextView>(R.id.textViewReligion).text = "Religion :" + item.religion
        }

        override fun getItemViewType(position: Int): Int {
            return R.layout.item_show_festival
        }
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
                showFestival(
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
        when (calendar[Calendar.MONTH]) {

        }
        val calendar = 23
        calendarBackForwardButtons(
            calendar - 1,
            mutableMapOf("date" to "${mutableList[calendar].year}-${mutableList[calendar].monthNum}-${mutableList[calendar].day}"),
            mutableList[calendar].day,
            mutableList[calendar].dayname
        )
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


    override fun calendarBackForwardButtons(
        position: Int,
        mutableMapOf: MutableMap<String, String>,
        dateOfFestivs: String,
        dayName: String
    ) {
        binding.rvShowMainDate.scrollToPosition(position)
        rvShowDate.update(mutableList as java.util.ArrayList<ModelMonth>)
        showFestival(mutableMapOf)
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

    override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        dateOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        showFestival(mutableMapOf("month" to monthOfYear.toString(), "year" to year.toString()))
    }
}