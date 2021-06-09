package com.shubhcalendar.ui.home.poojaartiskatha.childhelp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import com.httpconnection.httpconnectionV2.Http
import com.httpconnection.httpconnectionV2.interfaces.IGetResponse
import com.httpconnection.httpconnectionV2.models.Exception
import com.shubhcalendar.R
import com.shubhcalendar.databinding.FragmentViddhiBinding
import com.shubhcalendar.utills.Api.show_vidhi_bypuja_id

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

// created on 26/05/21 by monu
class ViddhiFragment : Fragment(), View.OnClickListener,InterfaceUpdateKath {

    private var _binding: FragmentViddhiBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViddhiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showVidhiKatha(mutableMapOf("puja_id" to "6"))

    }

    private fun showVidhiKatha(paramMap: MutableMap<String, String>) {
        Http.Post(show_vidhi_bypuja_id)
            .bodyParameter(paramMap)
            .build()
            .executeString(object : IGetResponse {
                override fun onResponse(response: String?) {
                    if (response?.isNotEmpty() == true) {
                        Log.e("gdsgdsfdg", "(ViddhiFragment.kt:44)-->>$response")

                        if (response?.isNotBlank()){
                            val getData = Http().createModelFromClass<DataShowVidhiKatha>(response!!)
                            if (getData.result=="sucessfull"){
                                    binding.textViewVidhi.text = getData.data?.get(0)?.vidhi ?: "not available"
                                    binding.textViewSamagri.text = getData.data?.get(0)?.samagri ?: "not available"
                                binding.textViewEmptyNote.isVisible = false
                                binding.linearLayoutData.isVisible = true
                            }else {
                                binding.textViewEmptyNote.isVisible = true
                                binding.linearLayoutData.isVisible = false
                            }
                        }else {
                            binding.textViewEmptyNote.isVisible = true
                            binding.linearLayoutData.isVisible = false
                        }
                    }
                }

                override fun onError(error: Exception?) {
                    Log.e("flag--", "(ViddhiFragment.kt:44)-->>")
                }
            })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {

    }

    override fun updateAdapter(value: String) {
        Toast.makeText(requireContext(), "$value", Toast.LENGTH_SHORT).show()
      showVidhiKatha(mutableMapOf("puja_id" to value))
    }
}


private
data class DataShowVidhiKatha(
    val `data`: List<Data?>? = null,
    val result: String? = null
) {
    data class Data(
        val id: String? = null,
        val path: String? = null,
        val puja_id: String? = null,
        val samagri: String? = null,
        val vidhi: String? = null
    )
}