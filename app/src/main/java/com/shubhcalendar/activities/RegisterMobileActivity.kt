package com.shubhcalendar.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import coil.api.load
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.shubhcalendar.R
import com.shubhcalendar.databinding.ActivityRegisterMobileBinding
import com.shubhcalendar.ui.HomeNewActivity
import com.shubhcalendar.ui.childhelps.LangugageSheet
import com.shubhcalendar.utills.Api.mobile_validation
import com.shubhcalendar.utills.Api.signup_mobile
import com.shubhcalendar.utills.Craft.getKey
import com.shubhcalendar.utills.Craft.isOnline
import com.shubhcalendar.utills.Craft.putKey
import com.shubhcalendar.utills.Craft.startActivity
import com.shubhcalendar.utills.Craft.toast
import com.shubhcalendar.utills.GenericTextWatcher
import com.shubhcalendar.utills.Keys

class RegisterMobileActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityRegisterMobileBinding
    var checkSignupOrMobile = 0
    private var getRegToken: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterMobileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val w = window
        w.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        init()
        val edit = arrayOf<EditText>(binding.etOne, binding.etTwo, binding.etThree, binding.etFour)
        binding.etOne.addTextChangedListener(GenericTextWatcher(edit, binding.etOne))
        binding.etTwo.addTextChangedListener(GenericTextWatcher(edit, binding.etTwo))
        binding.etThree.addTextChangedListener(GenericTextWatcher(edit, binding.etThree))
        binding.etFour.addTextChangedListener(GenericTextWatcher(edit, binding.etFour))

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e("FCM", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            getRegToken = task.result
            Log.e("FCM", "" + getRegToken)
            Toast.makeText(baseContext, getRegToken, Toast.LENGTH_SHORT).show()
        })

    }

    private fun init() {
        binding.btnGetOtp.setOnClickListener(this)
        binding.btnVerifyOtp.setOnClickListener(this)
        binding.textViewWrongMob.setOnClickListener(this)
        binding.textViewLoginMobile.setOnClickListener(this)
    }

    private fun validateFields(): Boolean {
        var boolean: Boolean
        if (binding.etOne.text.isNullOrEmpty()) {
            binding.etOne.error = "Required"
            boolean = false
        } else if (binding.etTwo.text.isNullOrEmpty()) {
            binding.etTwo.error = "Required"
            boolean = false
        } else if (binding.etThree.text.isNullOrEmpty()) {
            binding.etThree.error = "Required"
            boolean = false
        } else if (binding.etFour.text.isNullOrEmpty()) {
            binding.etFour.error = "Required"
            boolean = false
        } else {
            boolean = true
        }

        return boolean
    }

    private fun mobileFieldCheck(): Boolean {
        var boolean = false
        if (binding.etName.text.isNullOrEmpty()) {
            binding.etName.error = "Required"
        } else if (binding.etMobile.text.isNullOrEmpty()) {
            binding.etMobile.error = "Required"
        } else if (binding.etMobile.text.toString().length < 10) {
            toast("Please enter 10 digit mobile number")
        } else {
            boolean = true
        }
        return boolean
    }

    private fun getUserNameMobile() {
        binding.imgLoading.visibility = View.VISIBLE
        binding.imgLoading.load("https://fjkf.com") {
            placeholder(R.drawable.loading)
            error(R.drawable.loading)
        }
        Fuel.post(
            signup_mobile,
            listOf(
                "name" to binding.etName.text.toString().trim(),
                "mobile" to binding.etMobile.text.toString().trim(),
                "regid" to getRegToken
            )
        )
            .responseObject(DataSignupMobile.Des()) { request, response, result ->
                val (data, error) = result
                binding.imgLoading.visibility = View.GONE
                if (error == null) {
                    if (data?.result == "Otp Sent Successfully") {
                        if (binding.etName.text!!.isEmpty()) {
                            toast("Number Not Register Yet")
                        } else {
                            binding.rlGetOtp.visibility = View.GONE
                            binding.rlVerifyOtp.visibility = View.VISIBLE
                        }

                    } else if (data?.result == "Otp Sent Successfull") {
                        if (checkSignupOrMobile == 0) {

                        } else if (checkSignupOrMobile == 1) {
                            binding.rlGetOtp.visibility = View.GONE
                            binding.rlVerifyOtp.visibility = View.VISIBLE
                        } else {
                            toast("${data.result}")
                        }
                    } else {
                        toast("Something went wrong")
                    }
                }
            }
    }

    private fun getVerifyUserMobile(getfieldOtp: String) {
        binding.imgLoading.visibility = View.VISIBLE
        binding.imgLoading.load("https://fjkf.com") {
            placeholder(R.drawable.loading)
            error(R.drawable.loading)
        }

        Fuel.post(
            mobile_validation,
            listOf("otp" to getfieldOtp, "mobile" to binding.etMobile.text.toString().trim())
        )
            .responseObject(DataSignupMobile.Des()) { request, response, result ->
                val (data, error) = result
                binding.imgLoading.visibility = View.GONE
                if (error == null) {
                    Log.e("flag--", "getVerifyUserMobile(RegisterMobileActivity.kt:162)-->>$data")
                    if (data?.result == "Login successfully") {
                        toast(data.result)
                        putKey(Keys.userID, data.id)
                        putKey(Keys.userName, data.name)
                        putKey(Keys.userMobile, data.mobile)
                        if (getKey(Keys.isLanguageSelected)?.isEmpty() == true) {
                            val bundle = Bundle()
                            bundle.putString("openedFrom", "ShowAddressBottomsheet")
                            val bottomSheet = LangugageSheet()
                            bottomSheet.arguments = bundle
                            bottomSheet.show(supportFragmentManager, "new address")
                        } else {
                            startActivity<HomeNewActivity>()
                            finish()
                        }


                    } else {
                        toast(data?.result!!)
                    }
                }
            }

    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnGetOtp -> {

                if (checkSignupOrMobile == 0) {
                    if (mobileFieldCheck()) {
                        if (isOnline()) {
                            getUserNameMobile()
                        } else {
                            toast("No internet connection")
                        }
                    } else {
                        mobileFieldCheck()
                    }
                } else if (checkSignupOrMobile == 1) {
                    if (binding.etMobile.text.isNullOrEmpty()) {
                        binding.etMobile.error = "Required"
                    } else if (binding.etMobile.text.toString().length < 10) {
                        toast("Please enter 10 digit mobile number")
                    } else {
                        if (isOnline()) {
                            getUserNameMobile()
                        } else {
                            toast("No internet connection")
                        }
                    }
                }
            }
            binding.btnVerifyOtp -> {
                if (validateFields()) {
                    if (isOnline()) {
                        val etOneValue = binding.etOne.text.toString()
                        val etTwoValue = binding.etTwo.text.toString()
                        val etThree = binding.etThree.text.toString()
                        val etFour = binding.etFour.text.toString()
                        getVerifyUserMobile(etOneValue.plus(etTwoValue).plus(etThree).plus(etFour))
                    } else {
                        toast("No internet connection")
                    }

                } else {
                    validateFields()
                }
            }

            binding.textViewWrongMob -> {

                if (checkSignupOrMobile == 0) {
                    binding.etOne.setText("")
                    binding.etTwo.setText("")
                    binding.etThree.setText("")
                    binding.etFour.setText("")
                    binding.rlGetOtp.visibility = View.VISIBLE
                    binding.etName.visibility = View.VISIBLE
                    binding.rlVerifyOtp.visibility = View.GONE
                } else if (checkSignupOrMobile == 1) {

                    binding.etOne.setText("")
                    binding.etTwo.setText("")
                    binding.etThree.setText("")
                    binding.etFour.setText("")
                    binding.rlGetOtp.visibility = View.VISIBLE
                    binding.etName.visibility = View.GONE
                    binding.rlVerifyOtp.visibility = View.GONE
                }


            }

            binding.textViewLoginMobile -> {
                if (binding.textViewLoginMobile.text == "Or Login with mobile") {
                    checkSignupOrMobile = 1
                    binding.etName.setText("")
                    binding.etMobile.setText("")
                    binding.textViewLoginMobile.text = "Or Signup with mobile"
                    binding.textViewTitleSub.text = "Login With Your Mobile Number"
                    binding.btnGetOtp.text = "LOGIN WITH OTP"
                    binding.name.visibility = View.GONE

                } else {
                    checkSignupOrMobile = 0
                    binding.etName.setText("")
                    binding.etMobile.setText("")
                    binding.btnGetOtp.text = "GET OTP"
                    binding.textViewLoginMobile.text = "Or Login with mobile"
                    binding.textViewTitleSub.text = "Register With Your Mobile Number"
                    binding.name.visibility = View.VISIBLE
                    binding.etName.visibility = View.VISIBLE
                }
            }
        }

    }


    data class DataSignupMobile(
        val id: String,
        val mobile: String,
        val name: String,
        val otp: String,
        val status: String,
        val regid: String,
        val result: String
    ) {
        class Des : ResponseDeserializable<DataSignupMobile> {
            override fun deserialize(content: String) =
                Gson().fromJson(content, DataSignupMobile::class.java)
        }
    }
}