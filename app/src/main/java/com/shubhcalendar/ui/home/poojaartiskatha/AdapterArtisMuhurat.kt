package com.shubhcalendar.ui.home.poojaartiskatha

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.shubhcalendar.R
import com.shubhcalendar.ui.home.panchangmuhurat.InterfaceUpdateAdapter
import com.shubhcalendar.ui.home.panchangmuhurat.childfragments.PanchangFragment
import com.shubhcalendar.ui.home.panchangmuhurat.childfragments.MuhuratFrag
import com.shubhcalendar.ui.home.poojaartiskatha.childhelp.AartiFragment
import com.shubhcalendar.ui.home.poojaartiskatha.childhelp.InterfaceUpdateKath
import com.shubhcalendar.ui.home.poojaartiskatha.childhelp.KathaFragment
import com.shubhcalendar.ui.home.poojaartiskatha.childhelp.ViddhiFragment


/**
 * Created by javierg on 20/05/16.
 */
class AdapterArtisMuhurat(fm: FragmentManager?, var mContext: Context) : FragmentStatePagerAdapter(
    fm!!
) {
    lateinit var poojaID:String
    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return AartiFragment()
            1 -> return ViddhiFragment()
            2 -> return KathaFragment()

            else -> {
                throw IllegalStateException("$position is illegal")
            }
        }

    }

    //received from ManagerFragment
    fun update(
            poojaID: String,
    ) {
        this.poojaID = poojaID
        //updated
        notifyDataSetChanged()
    }

    override fun getItemPosition(`object`: Any): Int {
        if (`object` is InterfaceUpdateKath) {
            //sent to FirstFragment and SecondFragment
            `object`.updateAdapter(poojaID)
        }
        return super.getItemPosition(`object`)
    }

    override fun getCount(): Int {
        return TOTAL_TABS
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> mContext.getString(R.string.matrasandartis)
            1 -> mContext.getString(R.string.vidhikatha)
            2 -> mContext.getString(R.string.kathas)
            else -> mContext.getString(R.string.matrasandartis)
        }
    }

    companion object {
        const val TOTAL_TABS = 3
    }
}