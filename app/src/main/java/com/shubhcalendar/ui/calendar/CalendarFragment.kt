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
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.e.mylibrary.Fasttrack
import com.michalsvec.singlerowcalendar.utils.DateUtils
import com.shubhcalendar.R
import com.shubhcalendar.databinding.FragmentCalendarBinding
import com.shubhcalendar.ui.HomeNewActivity
import com.shubhcalendar.utills.BaseFragment
import com.shubhcalendar.utills.GenricAdapter
import com.shubhcalendar.utills.ViewHolder
import kotlinx.android.synthetic.main.full_screen_calendar.view.*
import kotlinx.android.synthetic.main.items_festivals.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext

class CalendarFragment : BaseFragment(),View.OnClickListener,CoroutineScope {
   lateinit var binding: FragmentCalendarBinding
   private val calendar = Calendar.getInstance()
   private var currentMonth = 0
   private var mutableMonthDateList: MutableList<ModelCalendarMonth> = mutableListOf()
   private var hour: Int = 0
   private var minute: Int = 0
   private var seconds: Int = 0
   private val monthNumListShow = arrayListOf("January","February","March","April","May","June","July","August","September","October","November","December")
   private val monthNumList = arrayListOf("0","1","2","3","4","5","6","7","8","9","10","11")
   override val coroutineContext: CoroutineContext
      get() = Dispatchers.Main + job
   lateinit var job: Job
   val concatList = arrayListOf<TestConcat?>()
   val concatFestivs: ArrayList<DataShowFestival.Data?>? = arrayListOf()
   val listIsFestival: ArrayList<ModelIsFestival?> = arrayListOf()
   override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
      binding = FragmentCalendarBinding.inflate(layoutInflater)
      return binding.root
   }

   override fun onViewCreated(view: View,savedInstanceState: Bundle?) {
      super.onViewCreated(view,savedInstanceState)
      job = Job()
      binding.recyclerDates.layoutManager = GridLayoutManager(requireActivity(),7)
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
            binding.texcViewDateCenter.text = """$getMonthName - $getYear"""
            if (DateUtils.getDayNumber(it) == "01") {
               if (DateUtils.getDay3LettersName(it) == "Sun") {
                  mutableMonthDateList.add(ModelCalendarMonth(DateUtils.getDayNumber(it),DateUtils.getDay3LettersName(it),DateUtils.getMonthNumber(it),DateUtils.getYear(it),hour.toString(),minute.toString()))
               } else if (DateUtils.getDay3LettersName(it) == "Mon") {
                  mutableMonthDateList.add(ModelCalendarMonth("-","-","-","-",hour.toString(),minute.toString()))

                  mutableMonthDateList.add(ModelCalendarMonth(DateUtils.getDayNumber(it),DateUtils.getDay3LettersName(it),DateUtils.getMonthNumber(it),DateUtils.getYear(it),hour.toString(),minute.toString()))
               } else if (DateUtils.getDay3LettersName(it) == "Tue") {
                  for (i in 0 until 2) {
                     mutableMonthDateList.add(ModelCalendarMonth("-","-","-","-",hour.toString(),minute.toString()))
                  }
                  mutableMonthDateList.add(ModelCalendarMonth(DateUtils.getDayNumber(it),DateUtils.getDay3LettersName(it),DateUtils.getMonthNumber(it),DateUtils.getYear(it),hour.toString(),minute.toString()))
               } else if (DateUtils.getDay3LettersName(it) == "Wed") {
                  for (i in 0 until 3) {
                     mutableMonthDateList.add(ModelCalendarMonth("-","-","-","-",hour.toString(),minute.toString()))
                  }
                  mutableMonthDateList.add(ModelCalendarMonth(DateUtils.getDayNumber(it),DateUtils.getDay3LettersName(it),DateUtils.getMonthNumber(it),DateUtils.getYear(it),hour.toString(),minute.toString()))
               } else if (DateUtils.getDay3LettersName(it) == "Thu") {
                  for (i in 0 until 4) {
                     mutableMonthDateList.add(ModelCalendarMonth("-","-","-","-",hour.toString(),minute.toString()))
                  }
                  mutableMonthDateList.add(ModelCalendarMonth(DateUtils.getDayNumber(it),DateUtils.getDay3LettersName(it),DateUtils.getMonthNumber(it),DateUtils.getYear(it),hour.toString(),minute.toString()))
               } else if (DateUtils.getDay3LettersName(it) == "Fri") {
                  for (i in 0 until 5) {
                     mutableMonthDateList.add(ModelCalendarMonth("-","-","-","-",hour.toString(),minute.toString()))
                  }

                  mutableMonthDateList.add(ModelCalendarMonth(DateUtils.getDayNumber(it),DateUtils.getDay3LettersName(it),DateUtils.getMonthNumber(it),DateUtils.getYear(it),hour.toString(),minute.toString()))
               } else if (DateUtils.getDay3LettersName(it) == "Sat") {
                  for (i in 0 until 6) {
                     mutableMonthDateList.add(ModelCalendarMonth("-","-","-","-",hour.toString(),minute.toString()))
                  }

                  mutableMonthDateList.add(ModelCalendarMonth(DateUtils.getDayNumber(it),DateUtils.getDay3LettersName(it),DateUtils.getMonthNumber(it),DateUtils.getYear(it),hour.toString(),minute.toString()))
               }
            } else {
               mutableMonthDateList.add(ModelCalendarMonth(DateUtils.getDayNumber(it),DateUtils.getDay3LettersName(it),DateUtils.getMonthNumber(it),DateUtils.getYear(it),hour.toString(),minute.toString()))
            }
         }
         val list = arrayListOf<TestConcat?>()
         mutableMonthDateList.forEach {
            list.add(TestConcat(it,null,null))
         }
         binding.recyclerDates.adapter = AdapterCalendar(list)
         showFestivals(DateUtils.getMonthNumber(getFutureDatesOfCurrentMonth().get(0)),DateUtils.getYear(getFutureDatesOfCurrentMonth().get(0)),mutableMonthDateList as ArrayList<ModelCalendarMonth>)
      } else if (action == 1) {
         getDatesOfPreviousMonth().forEach {
            val getMonthName = DateUtils.getMonthName(it)
            val getYear = DateUtils.getYear(it)
            binding.texcViewDateCenter.text = """$getMonthName - $getYear"""
            if (DateUtils.getDayNumber(it) == "01") {
               if (DateUtils.getDay3LettersName(it) == "Sun") {
                  mutableMonthDateList.add(ModelCalendarMonth(DateUtils.getDayNumber(it),DateUtils.getDay3LettersName(it),DateUtils.getMonthNumber(it),DateUtils.getYear(it),hour.toString(),minute.toString()))
               } else if (DateUtils.getDay3LettersName(it) == "Mon") {
                  mutableMonthDateList.add(ModelCalendarMonth("-","-","-","-",hour.toString(),minute.toString()))

                  mutableMonthDateList.add(ModelCalendarMonth(DateUtils.getDayNumber(it),DateUtils.getDay3LettersName(it),DateUtils.getMonthNumber(it),DateUtils.getYear(it),hour.toString(),minute.toString()))
               } else if (DateUtils.getDay3LettersName(it) == "Tue") {
                  for (i in 0 until 2) {
                     mutableMonthDateList.add(ModelCalendarMonth("-","-","-","-",hour.toString(),minute.toString()))
                  }
                  mutableMonthDateList.add(ModelCalendarMonth(DateUtils.getDayNumber(it),DateUtils.getDay3LettersName(it),DateUtils.getMonthNumber(it),DateUtils.getYear(it),hour.toString(),minute.toString()))
               } else if (DateUtils.getDay3LettersName(it) == "Wed") {
                  for (i in 0 until 3) {
                     mutableMonthDateList.add(ModelCalendarMonth("-","-","-","-",hour.toString(),minute.toString()))
                  }
                  mutableMonthDateList.add(ModelCalendarMonth(DateUtils.getDayNumber(it),DateUtils.getDay3LettersName(it),DateUtils.getMonthNumber(it),DateUtils.getYear(it),hour.toString(),minute.toString()))
               } else if (DateUtils.getDay3LettersName(it) == "Thu") {
                  for (i in 0 until 4) {
                     mutableMonthDateList.add(ModelCalendarMonth("-","-","-","-",hour.toString(),minute.toString()))
                  }
                  mutableMonthDateList.add(ModelCalendarMonth(DateUtils.getDayNumber(it),DateUtils.getDay3LettersName(it),DateUtils.getMonthNumber(it),DateUtils.getYear(it),hour.toString(),minute.toString()))
               } else if (DateUtils.getDay3LettersName(it) == "Fri") {
                  for (i in 0 until 5) {
                     mutableMonthDateList.add(ModelCalendarMonth("-","-","-","-",hour.toString(),minute.toString()))
                  }
                  mutableMonthDateList.add(ModelCalendarMonth(DateUtils.getDayNumber(it),DateUtils.getDay3LettersName(it),DateUtils.getMonthNumber(it),DateUtils.getYear(it),hour.toString(),minute.toString()))
               } else if (DateUtils.getDay3LettersName(it) == "Sat") {
                  for (i in 0 until 6) {
                     mutableMonthDateList.add(ModelCalendarMonth("-","-","-","-",hour.toString(),minute.toString()))
                  }

                  mutableMonthDateList.add(ModelCalendarMonth(DateUtils.getDayNumber(it),DateUtils.getDay3LettersName(it),DateUtils.getMonthNumber(it),DateUtils.getYear(it),hour.toString(),minute.toString()))
               }
            } else {
               mutableMonthDateList.add(ModelCalendarMonth(DateUtils.getDayNumber(it),DateUtils.getDay3LettersName(it),DateUtils.getMonthNumber(it),DateUtils.getYear(it),hour.toString(),minute.toString()))
            }
         }
         val list = arrayListOf<TestConcat?>()
         mutableMonthDateList.forEach {
            list.add(TestConcat(it,null,null))
         }
         binding.recyclerDates.adapter = AdapterCalendar(list)
         // showFestivals(DateUtils.getMonthNumber(getDatesOfPreviousMonth()[0]),DateUtils.getYear(getDatesOfPreviousMonth()[0]),mutableMonthDateList as ArrayList<ModelCalendarMonth>)
      } else if (action == 2) {
         getDatesOfNextMonth().forEach {
            val getMonthName = DateUtils.getMonthName(it)
            val getYear = DateUtils.getYear(it)
            binding.texcViewDateCenter.text = """$getMonthName - $getYear"""
            if (DateUtils.getDayNumber(it) == "01") {
               getMonthNum = DateUtils.getMonthNumber(it)
               getYearNum = DateUtils.getYear(it)
               if (DateUtils.getDay3LettersName(it) == "Sun") {
                  mutableMonthDateList.add(ModelCalendarMonth(DateUtils.getDayNumber(it),DateUtils.getDay3LettersName(it),DateUtils.getMonthNumber(it),DateUtils.getYear(it),hour.toString(),minute.toString()))
               } else if (DateUtils.getDay3LettersName(it) == "Mon") {
                  mutableMonthDateList.add(ModelCalendarMonth("-","-","-","-",hour.toString(),minute.toString()))

                  mutableMonthDateList.add(ModelCalendarMonth(DateUtils.getDayNumber(it),DateUtils.getDay3LettersName(it),DateUtils.getMonthNumber(it),DateUtils.getYear(it),hour.toString(),minute.toString()))
               } else if (DateUtils.getDay3LettersName(it) == "Tue") {
                  for (i in 0 until 2) {
                     mutableMonthDateList.add(ModelCalendarMonth("-","-","-","-",hour.toString(),minute.toString()))
                  }
                  mutableMonthDateList.add(ModelCalendarMonth(DateUtils.getDayNumber(it),DateUtils.getDay3LettersName(it),DateUtils.getMonthNumber(it),DateUtils.getYear(it),hour.toString(),minute.toString()))
               } else if (DateUtils.getDay3LettersName(it) == "Wed") {
                  for (i in 0 until 3) {
                     mutableMonthDateList.add(ModelCalendarMonth("-","-","-","-",hour.toString(),minute.toString()))
                  }
                  mutableMonthDateList.add(ModelCalendarMonth(DateUtils.getDayNumber(it),DateUtils.getDay3LettersName(it),DateUtils.getMonthNumber(it),DateUtils.getYear(it),hour.toString(),minute.toString()))
               } else if (DateUtils.getDay3LettersName(it) == "Thu") {
                  for (i in 0 until 4) {
                     mutableMonthDateList.add(ModelCalendarMonth("-","-","-","-",hour.toString(),minute.toString()))
                  }
                  mutableMonthDateList.add(ModelCalendarMonth(DateUtils.getDayNumber(it),DateUtils.getDay3LettersName(it),DateUtils.getMonthNumber(it),DateUtils.getYear(it),hour.toString(),minute.toString()))
               } else if (DateUtils.getDay3LettersName(it) == "Fri") {
                  for (i in 0 until 5) {
                     mutableMonthDateList.add(ModelCalendarMonth("-","-","-","-",hour.toString(),minute.toString()))
                  }
                  mutableMonthDateList.add(ModelCalendarMonth(DateUtils.getDayNumber(it),DateUtils.getDay3LettersName(it),DateUtils.getMonthNumber(it),DateUtils.getYear(it),hour.toString(),minute.toString()))
               } else if (DateUtils.getDay3LettersName(it) == "Sat") {
                  for (i in 0 until 6) {
                     mutableMonthDateList.add(ModelCalendarMonth("-","-","-","-",hour.toString(),minute.toString()))
                  }

                  mutableMonthDateList.add(ModelCalendarMonth(DateUtils.getDayNumber(it),DateUtils.getDay3LettersName(it),DateUtils.getMonthNumber(it),DateUtils.getYear(it),hour.toString(),minute.toString()))
               }
            } else {
               mutableMonthDateList.add(ModelCalendarMonth(DateUtils.getDayNumber(it),DateUtils.getDay3LettersName(it),DateUtils.getMonthNumber(it),DateUtils.getYear(it),hour.toString(),minute.toString()))
            }
         }
         val list = arrayListOf<TestConcat?>()
         mutableMonthDateList.forEach {
            list.add(TestConcat(it,null,null))
         }
         binding.recyclerDates.adapter = AdapterCalendar(list)
         //  showFestivals(DateUtils.getMonthNumber(getDatesOfNextMonth().get(0)),DateUtils.getYear(getDatesOfNextMonth().get(0)),mutableMonthDateList as ArrayList<ModelCalendarMonth>)
      }
   }

   private fun getDatesOfNextMonth(): List<Date> {
      currentMonth ++ // + because we want next month
      if (currentMonth == 12) {
         calendar.set(Calendar.YEAR,calendar[Calendar.YEAR] + 1)
         currentMonth = 0 // 0 == january
      }
      return getDates(mutableListOf())
   }

   private fun getDatesOfPreviousMonth(): List<Date> {
      currentMonth -- // - because we want previous month
      if (currentMonth == - 1) {
         calendar.set(Calendar.YEAR,calendar[Calendar.YEAR] - 1)
         currentMonth = 11 // 11 == december
      }
      return getDates(mutableListOf())
   }

   private fun getFutureDatesOfCurrentMonth(): List<Date> {
      // get all next dates of current month
      currentMonth --
      currentMonth = calendar[Calendar.MONTH]

      return getDates(mutableListOf())
   }

   private fun getAnyDate(selectedMonth: Int): List<Date> {
      currentMonth --
      currentMonth = selectedMonth
      return getDates(mutableListOf())
   }

   private fun getDates(list: MutableList<Date>): List<Date> {
      // load dates of whole month
      Log.e("pihu","getDates: " + currentMonth)
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

   fun startAnimation(action: Int) {
      // Intro animation configuration.
      val ANIMATION_DURATION: Long = 2000
      val valueAnimator = ValueAnimator.ofFloat(0f,1f)
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

   private fun showFestivals(month: String?,year: String?,mutableMonthDateList: ArrayList<ModelCalendarMonth>) {
      Fasttrack.post {
         it.apply {
            url("https://maestrosinfotech.org/shubh_calendar/appservice/process.php?action=show_holidays")
            bodyParameter(mutableMapOf("month" to month !!,"year" to year !!))
            executor { result,exception ->
               launch(coroutineContext) {
                  if (result?.responseCode == 200) {
                     if (result.responseString?.isNotEmpty() == true) {
                        val data = createModelFromClass<DataShowFestival>(result.responseString !!)
                        Log.e("CalendarFragment","showFestivals: ${result.responseString}");
                        if (data.result == "sucessfull") {
                           var count: Boolean
                           for (elements in mutableMonthDateList) {
                              count = true
                              for (i in 0 until data.data?.size !!) {
                                 if (elements.day == "0" + data.data.get(i)?.month) {
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
                              concatList.add(TestConcat(mutableMonthDateList.get(i),concatFestivs?.get(i),listIsFestival?.get(i)))
                           }
                           binding.festivalRecyclerView.adapter = AdapterShowFestivs(data.data as ArrayList<DataShowFestival.Data>)
                           binding.festivalRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
                           binding.recyclerDates.adapter = AdapterCalendar(concatList)
                        } else {
                           val emty = arrayListOf<DataShowFestival.Data>()
                           concatList.clear()
                           for (i in 0 until mutableMonthDateList.size) {
                              concatList.add(TestConcat(mutableMonthDateList.get(i),null,null))
                           }
                           binding.recyclerDates.adapter = AdapterCalendar(concatList)
                           binding.festivalRecyclerView.adapter = AdapterShowFestivs(emty)
                           binding.festivalRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
                        }
                     }
                  }
               }
            }
         }
      }
   }

   class AdapterShowFestivs(items: ArrayList<DataShowFestival.Data>) : GenricAdapter<DataShowFestival.Data>(items) {
      override fun configure(item: DataShowFestival.Data,holder: ViewHolder,position: Int) {
         val set = holder.itemView
         set.textViewName.text = """${item.title} (${item.date}-${item.month}-${item.year})"""
      }

      override fun getItemViewType(position: Int): Int {
         return R.layout.items_festivals
      }
   }

   class AdapterCalendar(items: ArrayList<TestConcat?>) : GenricAdapter<TestConcat?>(items) {
      override fun configure(item: TestConcat?,holder: ViewHolder,position: Int) {
         val set = holder.itemView
         if (item?.modelCalendarMonth?.day == "-") {
            set.cardViewCalendar.visibility = View.GONE
         }
         if (item?.isFestival?.isFestival == true) {
            set.cardViewIsFestiv.visibility = View.VISIBLE
         }
         set.textViewDateNum.text = item?.modelCalendarMonth?.day
      }

      override fun getItemViewType(position: Int): Int {
         return R.layout.full_screen_calendar
      }
   }
}

data class TestConcat(val modelCalendarMonth: ModelCalendarMonth?,val dataShowFestival: DataShowFestival.Data?,val isFestival: ModelIsFestival?)
data class ModelIsFestival(val isFestival: Boolean = false)
data class ModelCalendarMonth(val day: String,val dayname: String,val monthNum: String,val year: String,val hour: String,val minute: String)


