package com.shubhcalendar.ui.home.panchangmuhurat.childfragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.card.MaterialCardView
import com.shubhcalendar.R
import com.shubhcalendar.activities.ModelMonth
import com.shubhcalendar.databinding.FragmentMuhuratBinding
import com.shubhcalendar.utills.GenricAdapter
import com.shubhcalendar.utills.ViewHolder
import com.tsongkha.spinnerdatepicker.DatePicker
import com.tsongkha.spinnerdatepicker.DatePickerDialog
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder
import kotlinx.android.synthetic.main.calendar_item.view.*
import kotlinx.android.synthetic.main.rv_show_muhurat.view.*
import java.util.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

interface IMuhuratFrag {
    fun calendarBackForwardButtons(
        position: Int,
        mutableMapOf: MutableMap<String, String>,
        dateOfFestivs: String,
        dayName: String
    )

    fun getNextMonth()
    fun getPrivousMonth()
}

class MuhuratFrag : Fragment(), View.OnClickListener, DatePickerDialog.OnDateSetListener,
    IMuhuratFrag {
    lateinit var binding: FragmentMuhuratBinding
    lateinit var viewModel: PanchangFragmentViewModel
    private var currentMonth = 0
    var mutableList: MutableList<ModelMonth> = mutableListOf()
    val calendar = Calendar.getInstance()
    var dateOfMonth = 0
    var hour: Int = 0
    var minute: Int = 0
    var seconds: Int = 0
    var setDateForMuhurat = 0
    lateinit var layoutManager: LinearLayoutManager
    private lateinit var rvShowMuhurat: RvShowMuhurat
    lateinit var rvShowDate: RvShowDate
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMuhuratBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(`PanchangFragmentViewModel`::class.java)
        binding.ivSerach.setOnClickListener(this)
        binding.cardViewbackbtn.setOnClickListener(this)
        binding.textViewDateMain.text = calendar.get(Calendar.DAY_OF_MONTH).toString()
        viewModel.showMuhurat(
            mutableMapOf(
                "date" to "${calendar.get(Calendar.YEAR)}-0${
                    calendar.get(
                        Calendar.MONTH
                    ) + 1
                }-${calendar.get(Calendar.DAY_OF_MONTH)}"
            ), calendar.get(Calendar.DAY_OF_MONTH)
        )
        dateOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        setDateForMuhurat = calendar.get(Calendar.DAY_OF_MONTH)
        val emptyList = arrayListOf<PanchangFragmentViewModel.DataShowMuhurat.Data>()
        rvShowMuhurat = RvShowMuhurat(emptyList, dateOfMonth)
        viewModel.getLiveData.observe(viewLifecycleOwner, {
            if (it != null) {
                binding.rvShowMuhurat.apply {
                    layoutManager = LinearLayoutManager(requireActivity())
                    adapter = rvShowMuhurat
                    if (it.result == "data not present ") rvShowMuhurat.update(emptyList) else rvShowMuhurat.update(
                        it.data as ArrayList<PanchangFragmentViewModel.DataShowMuhurat.Data>
                    )
                }

            } else {
                Toast.makeText(requireContext(), "Data not present", Toast.LENGTH_SHORT).show()
            }
        })
        //  val snapHelper = PagerSnapHelper()
        layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        //  snapHelper.attachToRecyclerView(binding.rvShowMainDate)
        rvShowDate = RvShowDate(
            mutableList as ArrayList<ModelMonth>,
            this,
            binding.cardViewbackbtn,
            binding.cardViewforwordbtn,
            requireActivity(),
            setDateForMuhurat
        )
        initializeHorizontalCalendar()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = MuhuratFrag().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM1, param1)
                putString(ARG_PARAM2, param2)
            }
        }
    }

    private fun getFutureDatesOfCurrentMonth(): List<Date> {
        // get all next dates of current month
        currentMonth--
        currentMonth = calendar[Calendar.MONTH]
        Log.e("TestActivity", "getFutureDatesOfCurrentMonth: $currentMonth")
        return getDates(mutableListOf())
    }

    private fun initializeHorizontalCalendar() {
        var oneTImeShowCurrentMonth = true
        getFutureDatesOfCurrentMonth().forEach {
            if (oneTImeShowCurrentMonth) {
                //   showHolidays(mutableMapOf("month" to com.michalsvec.singlerowcalendar.utils.DateUtils.getMonthNumber(it), "year" to  com.michalsvec.singlerowcalendar.utils.DateUtils.getYear(it)))
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

        binding.rvShowMainDate.layoutManager = layoutManager
        binding.rvShowMainDate.adapter = rvShowDate
        val calendar = dateOfMonth
        Log.e("DFsfdsfdf", "initializeHorizontalCalendar: $calendar")
        calendarBackForwardButtons(
            calendar - 1,
            mutableMapOf("date" to "${mutableList[calendar].year}-${mutableList[calendar].monthNum}-${mutableList[calendar].day}"),
            mutableList[calendar].day,
            mutableList[calendar].dayname
        )
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
            binding.cardViewbackbtn -> {
            }
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        dateOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        viewModel.showMuhurat(
            mutableMapOf("date" to "$year-0${monthOfYear + 1}-$dayOfMonth"),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    override fun calendarBackForwardButtons(
        position: Int,
        mutableMapOf: MutableMap<String, String>,
        dateOfFestivs: String,
        dayName: String
    ) {
        binding.rvShowMainDate.scrollToPosition(position)
        Log.e("flag--", "calendarBackForwardButtons(MuhuratFrag.kt:166)-->>$mutableMapOf")
        rvShowDate.update(mutableList as ArrayList<ModelMonth>)
        if (dateOfFestivs.isEmpty()) {
            viewModel.showMuhurat(mutableMapOf, 0)
        } else {
            viewModel.showMuhurat(mutableMapOf, dateOfFestivs.toInt())
        }

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
}

private class RvShowMuhurat(
    items: ArrayList<PanchangFragmentViewModel.DataShowMuhurat.Data>,
    private val get: Int
) : GenricAdapter<PanchangFragmentViewModel.DataShowMuhurat.Data>(items) {
    override fun configure(
        item: PanchangFragmentViewModel.DataShowMuhurat.Data,
        holder: ViewHolder,
        position: Int
    ) {
        val set = holder.itemView
        set.textViewStartDate.text = item.start_time
        set.textViewEndDate.text = item.end_time
        set.textViewDateDay.text = item.date?.split("-")?.toTypedArray()?.get(2)
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.rv_show_muhurat
    }
}

class RvShowDate(
    items: ArrayList<ModelMonth>,
    val iMuhuratFrag: IMuhuratFrag,
    val cardViewbackbtn: MaterialCardView,
    val cardViewforwordbtn: MaterialCardView,
    val requireActivity: FragmentActivity,
    val pos: Int
) : GenricAdapter<ModelMonth>(items) {
    val calendar = Calendar.getInstance()
    private var moveToNextMonth = false
    private var moveToPreviousMonth = false
    override fun getItemViewType(position: Int): Int {
        return R.layout.rv_single_date
    }

    override fun configure(item: ModelMonth, holder: ViewHolder, position: Int) {
        val set = holder.itemView
        set.textViewDayName.text = item.dayname
        set.textViewDate.text = item.day
        // iMuhuratFrag.calendarBackForwardButtons(pos,mutableMapOf("date" to "${items[position].year}-${items[position].monthNum}-${items[position].day}"),items[position].day,items[position].dayname)

        cardViewforwordbtn.setOnClickListener {
            val nextPosition = if (position == items.size - 1) position else position + 1
            when (moveToNextMonth) {
                true -> {
                    iMuhuratFrag.getNextMonth()
                    moveToNextMonth = false
                }
                false -> {
                    if (nextPosition == items.size - 1) {
                        iMuhuratFrag.calendarBackForwardButtons(
                            nextPosition,
                            mutableMapOf("date" to "${items[nextPosition].year}-${items[nextPosition].monthNum}-${items[nextPosition].day}"),
                            items[nextPosition].day,
                            items[nextPosition].dayname
                        )
                        nextPosition + 1
                        moveToNextMonth = true
                    } else {
                        moveToNextMonth = false
                        iMuhuratFrag.calendarBackForwardButtons(
                            nextPosition,
                            mutableMapOf("date" to "${items[nextPosition].year}-${items[nextPosition].monthNum}-${items[nextPosition].day}"),
                            items[nextPosition].day,
                            items[nextPosition].dayname
                        )

                    }
                }
            }
/*
         if (nextPosition <= items.size) {
            iMuhuratFrag.calendarBackForwardButtons(nextPosition,mutableMapOf("date" to "${items[nextPosition].year}-${items[nextPosition].monthNum}-${items[nextPosition].day}"),items[nextPosition].day,items[nextPosition].dayname)

         }*/
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
                            mutableMapOf("date" to "${items[previousPosition].year}-${items[previousPosition].monthNum}-${items[previousPosition].day}"),
                            items[previousPosition].day,
                            items[previousPosition].dayname
                        )

                        moveToPreviousMonth = true
                    } else {
                        iMuhuratFrag.calendarBackForwardButtons(
                            previousPosition,
                            mutableMapOf("date" to "${items[previousPosition].year}-${items[previousPosition].monthNum}-${items[previousPosition].day}"),
                            items[previousPosition].day,
                            items[previousPosition].dayname
                        )
                        moveToPreviousMonth = false
                    }
                }
            }

/*         if (position == 0) {
            previousPosition = 0
         } else {
            iMuhuratFrag.calendarBackForwardButtons(previousPosition - 1,mutableMapOf("date" to "${items[previousPosition].year}-${items[previousPosition].monthNum}-${items[previousPosition].day}"),items[previousPosition].day,items[previousPosition].dayname)
         }*/
        }
    }


}

