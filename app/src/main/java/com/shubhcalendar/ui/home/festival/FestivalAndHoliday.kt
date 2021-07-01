package com.shubhcalendar.ui.home.festival

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import com.shubhcalendar.activities.NotificationActivity
import com.shubhcalendar.databinding.FragmentPanchabgMuhuratBinding
import com.shubhcalendar.ui.HomeNewActivity
import com.shubhcalendar.ui.profile.ProfileFragment
import com.shubhcalendar.utills.BaseFragment
import com.shubhcalendar.utills.Craft.startActivity
import kotlinx.android.synthetic.main.activity_home_new.*

class FestivalAndHoliday : BaseFragment(), View.OnClickListener {
    lateinit var binding: FragmentPanchabgMuhuratBinding
    lateinit var adapterPanchangeMuhurat: AdapterHolidayFestival
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPanchabgMuhuratBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapterPanchangeMuhurat = AdapterHolidayFestival(fragmentManager, activity!!)
        binding.viewPager.offscreenPageLimit = 2
        binding.viewPager.adapter = adapterPanchangeMuhurat
        binding.tabs.setupWithViewPager(binding.viewPager)
        binding.relativeLayoutOpenMenu.setOnClickListener(this)
        binding.cardViewProfile.setOnClickListener(this)
        binding.rlDismiss.setOnClickListener(this)
        binding.imageViewNotification.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.relativeLayoutOpenMenu -> {
                (activity as HomeNewActivity).drawer.openDrawer(GravityCompat.END)
            }
            binding.cardViewProfile -> {
                multipleStackNavigator?.start(
                    ProfileFragment()
                )
            }
            binding.rlDismiss -> {
                if (multipleStackNavigator?.canGoBack() == true) {
                    multipleStackNavigator?.goBack()
                }
            }

            binding.imageViewNotification ->{
                requireActivity().startActivity<NotificationActivity>()
            }
        }
    }


}