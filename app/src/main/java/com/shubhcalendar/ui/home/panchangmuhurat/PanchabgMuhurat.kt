package com.shubhcalendar.ui.home.panchangmuhurat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shubhcalendar.databinding.FragmentPanchabgMuhuratBinding

class PanchabgMuhurat : Fragment() {
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
    }
}