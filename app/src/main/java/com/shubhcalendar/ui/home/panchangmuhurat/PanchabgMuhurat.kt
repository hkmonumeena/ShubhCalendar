package com.shubhcalendar.ui.home.panchangmuhurat

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import com.shubhcalendar.databinding.FragmentPanchabgMuhuratBinding
import com.shubhcalendar.ui.HomeNewActivity
import com.shubhcalendar.ui.profile.ProfileFragment
import com.shubhcalendar.utills.BaseFragment
import com.trendyol.medusalib.navigator.transitionanimation.TransitionAnimationType
import kotlinx.android.synthetic.main.activity_home_new.*

class PanchabgMuhurat : BaseFragment(),View.OnClickListener {
    lateinit var binding: FragmentPanchabgMuhuratBinding
    lateinit var adapterPanchangeMuhurat: AdapterPanchangeMuhurat
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPanchabgMuhuratBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapterPanchangeMuhurat = AdapterPanchangeMuhurat(fragmentManager, activity!!)
        binding.viewPager.offscreenPageLimit =2
        binding.viewPager.adapter = adapterPanchangeMuhurat
        binding.tabs.setupWithViewPager(binding.viewPager)

        binding.relativeLayoutOpenMenu.setOnClickListener(this)
        binding.cardViewProfile.setOnClickListener(this)
        binding.rlDismiss.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
       when(v){
           binding.relativeLayoutOpenMenu ->{
               (activity as HomeNewActivity).drawer.openDrawer(GravityCompat.END)
           }
           binding.cardViewProfile ->{
              multipleStackNavigator?.start(ProfileFragment(),TransitionAnimationType.RIGHT_TO_LEFT)
           }
           binding.rlDismiss ->{
            if (multipleStackNavigator?.canGoBack() == true){
                multipleStackNavigator?.goBack()
            }
           }
       }
    }


}