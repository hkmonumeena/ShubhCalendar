package com.shubhcalendar.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import com.shubhcalendar.databinding.ActivityRashiBinding
import com.shubhcalendar.ui.HomeNewActivity
import com.shubhcalendar.ui.profile.ProfileFragment
import com.shubhcalendar.utills.BaseFragment
import com.shubhcalendar.utills.Craft.startActivity
import kotlinx.android.synthetic.main.activity_home_new.*


class RashiActivity : BaseFragment(), View.OnClickListener {

    private var _binding: ActivityRashiBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ActivityRashiBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.relativeLayoutOpenMenu.setOnClickListener(this)
        binding.cardViewProfile.setOnClickListener(this)
        binding.rlDismiss.setOnClickListener(this)
        binding.imageViewNotification.setOnClickListener(this)


    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

            binding.imageViewNotification -> {
                requireActivity().startActivity<NotificationActivity>()
            }

        }

    }

}