package com.shubhcalendar.ui.profile

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import coil.api.load
import com.afollestad.assent.*
import com.e.mylibrary.Fasttrack
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.util.FileUriUtils
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.DataPart
import com.github.kittinunf.fuel.core.FileDataPart
import com.shubhcalendar.R
import com.shubhcalendar.activities.HomeActivity
import com.shubhcalendar.activities.SplashActivity
import com.shubhcalendar.databinding.FragmentProfileBinding
import com.shubhcalendar.ui.HomeNewActivity
import com.shubhcalendar.utills.Api.UPDATE_PROFILE
import com.shubhcalendar.utills.Api.show_profile
import com.shubhcalendar.utills.BaseFragment
import com.shubhcalendar.utills.Craft.getKey
import com.shubhcalendar.utills.Craft.putKey
import com.shubhcalendar.utills.Craft.startActivity
import com.shubhcalendar.utills.Craft.toast
import com.shubhcalendar.utills.Keys
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.File
import kotlin.coroutines.CoroutineContext

class ProfileFragment : BaseFragment(), CoroutineScope, View.OnClickListener {
    private lateinit var binding: FragmentProfileBinding
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    private lateinit var job: Job
    private var reqCode = 0
    private val dataParts: MutableCollection<DataPart> = mutableListOf()
    private lateinit var imageProfileFile: File
    private var userName: String? = null
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
        binding.imageViewClose.setOnClickListener(this)
        binding.cardViewPhotoSelect.setOnClickListener(this)
        binding.textViewEditName.setOnClickListener(this)
        binding.textViewEditEmail.setOnClickListener(this)
        binding.relativeLayoutLogout.setOnClickListener(this)
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
                                val getData =
                                    createModelFromClass<DataShowProfile>(result.responseString!!)
                                if (getData.result == true) {
                                    userName = getData.data?.name
                                    binding.textViewName.text = getData.data?.name
                                    binding.textViewName2.text = getData.data?.name
                                    binding.textViewEmail.text = getData.data?.email
                                    binding.imageViewUserProfile.load(
                                        getData.data?.path.plus(
                                            getData.data?.image
                                        )
                                    )
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

    private fun updateProfileImage() {
        val progressBar = ProgressDialog(requireActivity())
        progressBar.setCancelable(false)
        progressBar.setTitle("Please Wait...")
        progressBar.show()
        val param = listOf("user_id" to requireActivity().getKey(Keys.userID))
        Fuel.upload(UPDATE_PROFILE, parameters = param)
            .plus(dataParts)
            .responseString { request, response, result ->
                progressBar.dismiss()
                val getAsJSON = JSONObject(result.get())
                if (getAsJSON.getString("result") == "true") {
                    Toast.makeText(
                        requireActivity(),
                        "Profile Updated Successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    showProfile()
                    (activity as HomeNewActivity).showProfile()
                } else {
                    Toast.makeText(requireActivity(), "Something went wrong ", Toast.LENGTH_SHORT)
                        .show()
                }
                Log.e("flag--", "updateProfileImage(ProfileFragment.kt:93)-->>${result.get()}")
            }

    }

    private fun updateDetails() {
        val params = listOf(
            "name" to binding.textViewName2.text.toString(),
            "email" to binding.textViewEmail.text.toString(),
            "user_id" to requireActivity().getKey((Keys.userID))
        )
        val progressBar = ProgressDialog(requireActivity())
        progressBar.setCancelable(false)
        progressBar.setTitle("Please Wait...")
        progressBar.show()
        Fuel.post(UPDATE_PROFILE, parameters = params)
            .responseString { request, response, result ->
                progressBar.dismiss()
                val getAsJSON = JSONObject(result.get())
                if (getAsJSON.getString("result") == "true") {
                    Toast.makeText(
                        requireActivity(),
                        "Profile Updated Successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    showProfile()
                    (activity as HomeNewActivity).showProfile()
                } else {
                    Toast.makeText(requireActivity(), "Something went wrong ", Toast.LENGTH_SHORT)
                        .show()
                }
                Log.e("flag--", "updateProfileImage(ProfileFragment.kt:93)-->>${result.get()}")
            }

    }

    private fun updateImageConfirmDialog() {
        val dialog = Dialog(requireActivity())
        dialog.setContentView(R.layout.upload_image_confirm)
        var btn_confirm: CardView? = null
        var btn_cancel: CardView? = null
        var editTextTextEmailAddress2: TextView? = null
        btn_confirm = dialog.findViewById(R.id.btnLogout)
        btn_cancel = dialog.findViewById(R.id.btn_cancel)
        editTextTextEmailAddress2 = dialog.findViewById(R.id.editTextTextEmailAddress2)
        btn_confirm?.setOnClickListener {
            dialog.dismiss()
            updateProfileImage()
        }
        btn_cancel?.setOnClickListener {
            dialog.dismiss()
        }
        //  val draw = BitmapDrawable(resources, blurBitmap)
        val window = dialog.window
        dialog.window?.setBackgroundDrawableResource(R.color.black_overlay)
        //  window?.setBackgroundDrawable(draw)
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        window?.setGravity(Gravity.CENTER_VERTICAL)
        dialog.show()

    }

    private fun updateFields(field: String?, hintText: String, action: Int) {
        val dialog = Dialog(requireActivity())
        dialog.setContentView(R.layout.edit_profile_dialogue)
        var btn_confirm: CardView? = null
        var btn_cancel: CardView? = null
        var editTextTextEmailAddress2: EditText? = null
        btn_confirm = dialog.findViewById(R.id.btnLogout)
        btn_cancel = dialog.findViewById(R.id.btn_cancel)
        editTextTextEmailAddress2 = dialog.findViewById(R.id.editTextTextEmailAddress2)
        editTextTextEmailAddress2.setText(field)
        editTextTextEmailAddress2.hint = hintText
        btn_confirm?.setOnClickListener {
            dialog.dismiss()
            if (action == 1) {
                binding.textViewName2.text = editTextTextEmailAddress2.text.toString()
            } else {
                binding.textViewEmail.text = editTextTextEmailAddress2.text.toString()
            }
            updateDetails()
        }
        btn_cancel?.setOnClickListener {
            dialog.dismiss()
        }
        //  val draw = BitmapDrawable(resources, blurBitmap)
        val window = dialog.window
        dialog.window?.setBackgroundDrawableResource(R.color.black_overlay)
        //  window?.setBackgroundDrawable(draw)
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        window?.setGravity(Gravity.CENTER_VERTICAL)
        dialog.show()

    }


    private fun logutDialog() {
        val dialog = Dialog(requireActivity())
        dialog.setContentView(R.layout.logout_popup)
        var btn_confirm: CardView? = null
        var btn_cancel: CardView? = null
        var editTextTextEmailAddress2: TextView? = null
        btn_confirm = dialog.findViewById(R.id.btnLogout)
        btn_cancel = dialog.findViewById(R.id.btn_cancel)
        editTextTextEmailAddress2 = dialog.findViewById(R.id.editTextTextEmailAddress2)
        btn_confirm?.setOnClickListener {
            dialog.dismiss()
            requireActivity().putKey(Keys.userID, "")
            requireActivity().startActivity<SplashActivity>()
            requireActivity().finishAffinity()
        }
        btn_cancel?.setOnClickListener {
            dialog.dismiss()
        }
        //  val draw = BitmapDrawable(resources, blurBitmap)
        val window = dialog.window
        dialog.window?.setBackgroundDrawableResource(R.color.black_overlay)
        //  window?.setBackgroundDrawable(draw)
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        window?.setGravity(Gravity.CENTER_VERTICAL)
        dialog.show()

    }


    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (reqCode) {
                132 -> {
                    val path = data?.data?.let {
                        FileUriUtils.getRealPath(
                            context = requireActivity(),
                            uri = it
                        )
                    }
                    imageProfileFile = File(path)
                    dataParts.add(FileDataPart(imageProfileFile, name = "image"))
                    binding.imageViewUserProfile.setImageURI(data?.data)
                    Handler().postDelayed(
                        {
                            updateImageConfirmDialog()
                        }, 1000
                    )
                }
            }

        }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.imageViewClose -> {
                requireActivity().onBackPressed()
            }
            binding.cardViewPhotoSelect -> {
                askForPermissions(
                    Permission.READ_EXTERNAL_STORAGE,
                    Permission.WRITE_EXTERNAL_STORAGE,
                    Permission.CAMERA,
                ) {
                    val result: AssentResult = it
                    if (result[Permission.READ_EXTERNAL_STORAGE] == GrantResult.PERMANENTLY_DENIED && result[Permission.WRITE_EXTERNAL_STORAGE] == GrantResult.PERMANENTLY_DENIED) {
                        showSystemAppDetailsPage()
                    } else {
                        reqCode = 132
                        ImagePicker.with(this)
                            .crop()
                            .compress(1024)
                            .maxResultSize(1080, 1080)
                            .start()
                    }

                }

            }

            binding.textViewEditName -> {
                updateFields(binding.textViewName2.text.toString(), "Enter your name here", 1)
            }
            binding.textViewEditEmail -> {
                updateFields(binding.textViewEmail.text.toString(), "Enter your email here", 2)
            }
            binding.relativeLayoutLogout -> {
                logutDialog()
            }
        }
    }
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