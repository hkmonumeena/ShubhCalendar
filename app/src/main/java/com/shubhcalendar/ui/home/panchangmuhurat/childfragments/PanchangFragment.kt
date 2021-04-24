package com.shubhcalendar.ui.home.panchangmuhurat.childfragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.google.android.material.card.MaterialCardView
import com.shubhcalendar.R
import com.shubhcalendar.activities.ModelMonth
import com.shubhcalendar.databinding.ActivityPanchangBinding
import com.shubhcalendar.utills.GenricAdapter
import com.shubhcalendar.utills.ViewHolder
import com.tsongkha.spinnerdatepicker.DatePicker
import com.tsongkha.spinnerdatepicker.DatePickerDialog
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder
import kotlinx.android.synthetic.main.calendar_item.view.*
import java.util.*

interface IPanchangFragment {
   fun calendarBackForwardButtons(position: Int,mutableMapOf: MutableMap<String,String>,dateOfFestivs: String,dayName: String)
}

class PanchangFragment : Fragment(),IPanchangFragment,DatePickerDialog.OnDateSetListener,View.OnClickListener {
   lateinit var binding: ActivityPanchangBinding
   private var currentMonth = 0
   lateinit var viewModel: Panchang2FragmentViewModel
   var mutableList: MutableList<ModelMonth> = mutableListOf()
   val calendar = Calendar.getInstance()
   var dateOfMonth = 0
   var hour: Int = 0
   var minute: Int = 0
   var seconds: Int = 0
   lateinit var layoutManager: LinearLayoutManager
   override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
      binding = ActivityPanchangBinding.inflate(layoutInflater)
      return binding.root
   }

   override fun onViewCreated(view: View,savedInstanceState: Bundle?) {
      super.onViewCreated(view,savedInstanceState)
      viewModel = ViewModelProvider(this).get(Panchang2FragmentViewModel::class.java)
      binding.ivSerach.setOnClickListener(this)
      dateOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

      val snapHelper = PagerSnapHelper()

      layoutManager = LinearLayoutManager(requireActivity(),LinearLayoutManager.HORIZONTAL,false)
      snapHelper.attachToRecyclerView(binding.rvShowMainDate)
      initializeHorizontalCalendar()
      viewModel.showMuhurat(mutableMapOf("date" to "${calendar.get(Calendar.YEAR)}-0${calendar.get(Calendar.MONTH) + 1}-${calendar.get(Calendar.DAY_OF_MONTH)}"),calendar.get(Calendar.DAY_OF_MONTH))

      viewModel.getLiveData.observe(viewLifecycleOwner,{
            if (it.result == "data not present "){

            } else {
               val set = it?.data
               with(binding){
                  txtState.text =set?.sunrise
                  textViewSunset.text =set?.sunset
                  textViewmoonrise.text =set?.moonrise
                  textViewmoonset.text =set?.moonset
                  nakshatra.text =set?.nakshatra
                  yoga.text =set?.yoga
               }
            }

      })

   }

   private fun getFutureDatesOfCurrentMonth(): List<Date> {
      // get all next dates of current month
      currentMonth --
      currentMonth = calendar[Calendar.MONTH]
      Log.e("TestActivity","getFutureDatesOfCurrentMonth: $currentMonth")
      return getDates(mutableListOf())
   }

   private fun initializeHorizontalCalendar() {
      getFutureDatesOfCurrentMonth().forEach {
         mutableList.add(ModelMonth(com.michalsvec.singlerowcalendar.utils.DateUtils.getDayNumber(it),com.michalsvec.singlerowcalendar.utils.DateUtils.getDayName(it),com.michalsvec.singlerowcalendar.utils.DateUtils.getMonthNumber(it),com.michalsvec.singlerowcalendar.utils.DateUtils.getYear(it),hour.toString(),minute.toString()))
      }

      binding.rvShowMainDate.layoutManager = layoutManager
      binding.rvShowMainDate.adapter = RvShowDate(mutableList as ArrayList<ModelMonth>,this,binding.cardViewbackbtn,binding.cardViewforwordbtn)
      val calendar =23
      Log.e("DFsfdsfdf","initializeHorizontalCalendar: $calendar",)
      calendarBackForwardButtons(calendar-1,mutableMapOf("date" to "${mutableList[calendar].year}-${mutableList[calendar].monthNum}-${mutableList[calendar].day}"),mutableList[calendar].day,mutableList[calendar].dayname)

   }

   private fun getDatesOfNextMonth(): List<Date> {
      currentMonth ++ // + because we want next month
      if (currentMonth == 12) {
         // we will switch to january of next year, when we reach last month of year
         calendar.set(Calendar.YEAR,calendar[Calendar.YEAR] + 1)
         currentMonth = 0 // 0 == january
      }
      return getDates(mutableListOf())
   }

   private fun getDatesOfPreviousMonth(): List<Date> {
      currentMonth -- // - because we want previous month
      if (currentMonth == - 1) {
         // we will switch to december of previous year, when we reach first month of year
         calendar.set(Calendar.YEAR,calendar[Calendar.YEAR] - 1)
         currentMonth = 11 // 11 == december
      }
      return getDates(mutableListOf())
   }

   private fun getDates(list: MutableList<Date>): List<Date> {
      calendar.set(Calendar.MONTH,currentMonth)
      calendar.set(Calendar.DAY_OF_MONTH,1)
      list.add(calendar.time)
      while (currentMonth == calendar[Calendar.MONTH]) {
         calendar.add(Calendar.DATE,+ 1)
         if (calendar[Calendar.MONTH] == currentMonth) list.add(calendar.time)
      }
      calendar.add(Calendar.DATE,- 1)

      return list
   }

   override fun calendarBackForwardButtons(position: Int,mutableMapOf: MutableMap<String,String>,dateOfFestivs: String,dayName: String) {
      binding.rvShowMainDate.scrollToPosition(position)
      viewModel.showMuhurat(mutableMapOf,dateOfFestivs.toInt())
   }

   private class RvShowDate(items: ArrayList<ModelMonth>,val iPanchangFragment: IPanchangFragment,val cardViewbackbtn: MaterialCardView,val cardViewforwordbtn: MaterialCardView) : GenricAdapter<ModelMonth>(items) {
      override fun getItemViewType(position: Int): Int {
         return R.layout.rv_single_date
      }

      override fun configure(item: ModelMonth,holder: ViewHolder,position: Int) {
         val set = holder.itemView
         set.textViewDayName.text = item.dayname
         set.textViewDate.text = item.day
         cardViewforwordbtn.setOnClickListener {
            var nextPosition = position + 1
            if (nextPosition <= items.size) {
               iPanchangFragment.calendarBackForwardButtons(nextPosition,mutableMapOf("date" to "${item.year}-${item.monthNum}-${item.day}"),item.day,item.dayname)
            }
            update(items)
         }
         cardViewbackbtn.setOnClickListener {
            var nextPosition = position
            if (position == 0) {
               nextPosition = 0
               iPanchangFragment.calendarBackForwardButtons(nextPosition,mutableMapOf("date" to "${item.year}-${item.monthNum}-${item.day}"),item.day,item.dayname)
            } else {
               iPanchangFragment.calendarBackForwardButtons(nextPosition - 1,mutableMapOf("date" to "${item.year}-${item.monthNum}-${item.day}"),item.day,item.dayname)
            }
         }
      }
   }

   override fun onDateSet(view: DatePicker?,year: Int,monthOfYear: Int,dayOfMonth: Int) {
      dateOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
      //viewModel.showMuhurat(mutableMapOf("date" to "$year-0${monthOfYear + 1}-$dayOfMonth"),calendar.get(Calendar.DAY_OF_MONTH))
   }

   override fun onClick(v: View?) {
      when (v) {
         binding.ivSerach -> {
            SpinnerDatePickerDialogBuilder().context(requireActivity()).callback(this).spinnerTheme(R.style.NumberPickerStyle).showTitle(true).showDaySpinner(true).maxDate(2030,0,1).defaultDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).build().show()
         }
      }
   }
}