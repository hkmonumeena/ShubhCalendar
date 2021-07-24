package com.shubhcalendar.ui.horoscope

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import com.shubhcalendar.activities.NotificationActivity
import com.shubhcalendar.activities.RashiActivity
import com.shubhcalendar.databinding.ActivityGetHoroscopeBinding
import com.shubhcalendar.ui.HomeNewActivity
import com.shubhcalendar.ui.profile.ProfileFragment
import com.shubhcalendar.utills.BaseFragment
import com.shubhcalendar.utills.Craft.isValidate
import com.shubhcalendar.utills.Craft.startActivity
import com.shubhcalendar.utills.DatePickerHelper
import kotlinx.android.synthetic.main.activity_home_new.*
import java.util.*


class HoroscopeFragment : BaseFragment(), View.OnClickListener {

    private var _binding: ActivityGetHoroscopeBinding? = null
    private val binding get() = _binding!!
    lateinit var datePicker: DatePickerHelper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ActivityGetHoroscopeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.relativeLayoutOpenMenu.setOnClickListener(this)
        binding.cardViewProfile.setOnClickListener(this)
        binding.rlDismiss.setOnClickListener(this)
        binding.imageViewNotification.setOnClickListener(this)
        binding.editTextDob.setOnClickListener(this)
        binding.cardViewArrow.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.relativeLayoutOpenMenu -> {
                (activity as HomeNewActivity).drawer.openDrawer(GravityCompat.END)
            }
            binding.cardViewProfile -> {
                multipleStackNavigator?.start(ProfileFragment())
            }
            binding.rlDismiss -> {
                if (multipleStackNavigator?.canGoBack() == true) {
                    multipleStackNavigator?.goBack()
                }
            }

            binding.imageViewNotification -> {
                requireActivity().startActivity<NotificationActivity>()
            }
            binding.editTextDob -> {
                showDatePickerDialog()
            }
            binding.cardViewArrow -> {
                val getFieldWithValiidation = isValidate(binding.editTextName).isValidate(binding.editTextDob).getValidatedFields()
                if (getFieldWithValiidation) { multipleStackNavigator?.start(RashiActivity()) }
            }
        }
    }

    fun showDatePickerDialog() {
        val cal = Calendar.getInstance()
        val d = cal.get(Calendar.DAY_OF_MONTH)
        val m = cal.get(Calendar.MONTH)
        val y = cal.get(Calendar.YEAR)
        datePicker = DatePickerHelper(requireActivity(), true)
        datePicker.showDialog(d, m, y, object : DatePickerHelper.Callback {
            override fun onDateSelected(dayofMonth: Int, month: Int, year: Int) {
                val dayStr = if (dayofMonth < 10) "0${dayofMonth}" else "${dayofMonth}"
                val mon = month + 1
                val monthStr = if (mon < 10) "0${mon}" else "${mon}"
                binding.editTextDob.setText("${dayStr}-${monthStr}-${year}")
            }
        })
    }
}