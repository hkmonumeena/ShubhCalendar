package com.shubhcalendar.ui.home.poojaartiskatha

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.GravityCompat
import com.httpconnection.httpconnectionV2.Http
import com.httpconnection.httpconnectionV2.interfaces.IGetResponse
import com.httpconnection.httpconnectionV2.models.Exception
import com.shubhcalendar.databinding.FragmentArtiVidhiKathaBinding
import com.shubhcalendar.ui.HomeNewActivity
import com.shubhcalendar.ui.home.poojaartiskatha.childhelp.DataShowAllPooja
import com.shubhcalendar.ui.profile.ProfileFragment
import com.shubhcalendar.utills.BaseFragment
import com.shubhcalendar.utills.Craft.confirmationDialog
import com.trendyol.medusalib.navigator.transitionanimation.TransitionAnimationType

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ArtiVidhiKathaFrag : BaseFragment(), View.OnClickListener {
    private var param1: String? = null
    private var param2: String? = null
    var binding: FragmentArtiVidhiKathaBinding? = null
    lateinit var adapterArtisMuhurat: AdapterArtisMuhurat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArtiVidhiKathaBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapterArtisMuhurat = AdapterArtisMuhurat(fragmentManager, activity!!)
        binding!!.viewPager.offscreenPageLimit = 3
        binding!!.viewPager.adapter = adapterArtisMuhurat
        binding!!.tabs.setupWithViewPager(binding!!.viewPager)
        binding!!.rlDismiss.setOnClickListener(this)
        binding!!.relativeLayoutMenu.setOnClickListener(this)
        binding!!.cardViewProfile.setOnClickListener(this)
        showAllKatha()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = ArtiVidhiKathaFrag().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM1, param1)
                putString(ARG_PARAM2, param2)
            }
        }
    }

    fun showAllKatha() {
        Http.Post("https://maestrosinfotech.org/shubh_calendar/appservice/process.php?action=show_puja")
            .build().executeString(object : IGetResponse {
                override fun onResponse(response: String?) {
                    if (response?.isNotEmpty() == true) {
                        val getData = Http().createModelFromClass<DataShowAllPooja>(response)
                        if (getData.result == "sucessfull") {
                            val textList = arrayListOf<String>()
                            val textID = arrayListOf<String>()
                            binding?.rlSelect?.setOnClickListener {
                                requireActivity().confirmationDialog(
                                    "Select Pooja",
                                    textList,
                                    textID
                                ) { position, value, id ->
                                    adapterArtisMuhurat.update(id)
                                }

                            }

                            getData.data?.forEach {
                                textList.add(it?.title!!)
                                textID.add(it.id!!)
                            }

                        }
                    }
                    Log.e("flag--", "onResponse(ArtiVidhiKathaFrag.kt:55)-->>$response")

                }

                override fun onError(error: Exception?) {
                }
            })
    }

    override fun onClick(v: View?) {
        when (v) {
            binding!!.rlDismiss -> {
                /* (activity as HomeNewActivity).binding.frame.isVisible = true
                 (activity as HomeNewActivity).binding.frame2.isVisible = false
                 (activity as HomeNewActivity).binding.bottomNavigation.isVisible = true
                 getActivity()?.getSupportFragmentManager()?.beginTransaction()?.remove(this)?.commit();*/

                requireActivity().onBackPressed()
            }

            binding!!.relativeLayoutMenu -> {
                (activity as HomeNewActivity).binding.drawer.openDrawer(GravityCompat.END)
            }

            binding!!.cardViewProfile -> {
                multipleStackNavigator?.start(
                    ProfileFragment(),
                    TransitionAnimationType.RIGHT_TO_LEFT
                )
            }
        }
    }


    override fun onDestroy() {
        binding = null

        super.onDestroy()
    }


}