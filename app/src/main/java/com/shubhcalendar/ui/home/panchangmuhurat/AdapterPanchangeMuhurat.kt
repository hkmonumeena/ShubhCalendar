package com.shubhcalendar.ui.home.panchangmuhurat

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.shubhcalendar.R
import com.shubhcalendar.ui.home.panchangmuhurat.childfragments.MuhuratFragment
import com.shubhcalendar.ui.home.panchangmuhurat.childfragments.PanchangFragment


/**
 * Created by javierg on 20/05/16.
 */
class AdapterPanchangeMuhurat(fm: FragmentManager?, var mContext: Context) : FragmentStatePagerAdapter(
    fm!!
) {
    lateinit var myDay:String
    lateinit var myMonth:String
    lateinit var myYear:String
    lateinit var myHour:String
    lateinit var myMinute:String
    lateinit var mySeconds:String
    lateinit var mylat:String
    lateinit var mylng:String
    lateinit var mytime:String
    lateinit var myaddress:String
    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return PanchangFragment()
            1 -> return MuhuratFragment()

            else -> {
                throw IllegalStateException("$position is illegal")
            }
        }

    }

    //received from ManagerFragment
    fun update(
        day: String,
        month: String,
        year: String,
        hour: String,
        minute: String,
        seconds: String,
        lat: String,
        lng: String,
        time: String,
        address: String
    ) {
        myDay = day
        myMonth = month
        myYear = year
        myHour = hour
        myMinute = minute
        mySeconds = seconds
        mylat = lat
        mylng = lng
        mytime = time
        myaddress = address
        //updated
        notifyDataSetChanged()
    }

    override fun getItemPosition(`object`: Any): Int {
        if (`object` is InterfaceUpdateAdapter) {
            //sent to FirstFragment and SecondFragment
            `object`.updateAdapter("value1")
        }
        return super.getItemPosition(`object`)
    }

    override fun getCount(): Int {
        return TOTAL_TABS
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> mContext.getString(R.string.panchang)
            1 -> mContext.getString(R.string.muhurat)
            else -> mContext.getString(R.string.panchang_muharat)
        }
    }

    companion object {
        const val TOTAL_TABS = 2
    }
}