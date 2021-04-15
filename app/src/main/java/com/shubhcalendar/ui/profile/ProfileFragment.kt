package com.shubhcalendar.ui.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.e.mylibrary.Fasttrack
import com.shubhcalendar.R
import com.shubhcalendar.databinding.FragmentProfileBinding
import com.shubhcalendar.utills.Api.show_profile
import com.shubhcalendar.utills.BaseFragment
import com.shubhcalendar.utills.Craft.getKey
import com.shubhcalendar.utills.Craft.toast
import com.shubhcalendar.utills.Keys
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ProfileFragment : BaseFragment(), CoroutineScope {
    lateinit var binding: FragmentProfileBinding
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    lateinit var job: Job
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        job = Job()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
      showProfile()
    }

    private fun showProfile() {
        Fasttrack.post {
            it.apply {
                url(show_profile)
                bodyParameter(mutableMapOf("user_id" to requireActivity().getKey(Keys.userID) as String))
                executor { result, exception ->
                    launch(coroutineContext) {
                        if (result?.responseCode == 200) {
                            if (result.responseString?.isNotEmpty() == true) {
                                val getData = createModelFromClass<DataShowProfile>(result.responseString!!)
                                if (getData.result == true) {
                                    binding.textViewName.text = getData.data?.name
                                    binding.textViewName2.text = getData.data?.name
                                    binding.textViewEmail.text = getData.data?.email
                                }

                            } else {

                            }
                        } else {
                            requireActivity().toast("${exception?.responseCode} - ${exception?.responseMessage}")
                        }
                    }
                    Log.e("ProfileFragment", "showProfile: ${result?.responseString}")
                }
            }
        }
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }

    data class DataShowProfile(
        val result: Boolean?,
        val `data`: Data?
    ) {
        data class Data(
            val id: String?,
            val mobile: String?,
            val name: String?,
            val otp: String?,
            val status: String?,
            val regid: String?,
            val image: String?,
            val email: String?,
            val path: String?
        )
    }
}