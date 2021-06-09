package com.shubhcalendar.ui.home.poojaartiskatha.childhelp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.httpconnection.httpconnectionV2.Http
import com.httpconnection.httpconnectionV2.interfaces.IGetResponse
import com.httpconnection.httpconnectionV2.models.Exception
import com.shubhcalendar.R
import com.shubhcalendar.databinding.FragmentAartiBinding
import com.shubhcalendar.utills.Api.show_mantra_bypuja_id
import com.shubhcalendar.utills.BaseFragment
import com.shubhcalendar.utills.GenricAdapter
import com.shubhcalendar.utills.ViewHolder


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

interface IAartiFragment {
    fun iAartiFragment(action: Int, plus: String, title: String?)
}

class AartiFragment : BaseFragment(), IAartiFragment, InterfaceUpdateKath {

    private var param1: String? = null
    private var param2: String? = null


    private var _binding: FragmentAartiBinding? = null

    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAartiBinding.inflate(layoutInflater)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AartiFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showKatha("6")
    }


    override fun onDestroy() {

        _binding = null

        super.onDestroy()
    }


    private fun showKatha(poojaID: String) {
        Http.Post(show_mantra_bypuja_id)
            .bodyParameter(mutableMapOf("puja_id" to poojaID))
            .build()
            .executeString(object : IGetResponse {
                override fun onResponse(response: String?) {
                    Log.e("flag--", "onResponse(AartiFragment.kt:68)-->>$response")
                    val getData = Http().createModelFromClass<DataShowAarti>(response!!)
                    if (getData.result == "sucessfull") {
                        binding.textViewEmpty.isVisible = false
                        binding.rvShowAarti.apply {
                            layoutManager = LinearLayoutManager(requireActivity())
                            adapter = RvShowAarti(
                                getData.data as ArrayList<DataShowAarti.Data>,
                                this@AartiFragment
                            )
                        }
                    } else if (getData.result == "data not present ") {
                        val emptyList = arrayListOf<DataShowAarti.Data>()
                        binding.textViewEmpty.isVisible = true
                        binding.textViewEmpty.text = "No Aarti Avialable at this time"
                        binding.rvShowAarti.apply {
                            layoutManager = LinearLayoutManager(requireActivity())
                            adapter = RvShowAarti(emptyList, this@AartiFragment)
                        }

                        binding.textViewEmpty.isVisible = true

                    }
                }

                override fun onError(error: Exception?) {

                }

            })
    }


    override fun iAartiFragment(action: Int, musicLink: String, title: String?) {
        val sheet = MusicPlayerSheet()
        sheet.arguments = bundleOf("musicLink" to musicLink, "title" to title)
        sheet.show(requireActivity().supportFragmentManager, "tag")
    }

    override fun updateAdapter(value: String) {
        showKatha(value)
    }
}

private class RvShowAarti(
    items: ArrayList<DataShowAarti.Data>,
    val iAartiFragment: IAartiFragment
) : GenricAdapter<DataShowAarti.Data>(items) {
    override fun configure(item: DataShowAarti.Data, holder: ViewHolder, position: Int) {
        val set = holder.itemView
        set.setOnClickListener {
            iAartiFragment.iAartiFragment(1, item.path.plus(item.file), item.title)

        }
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.rv_katha
    }

}