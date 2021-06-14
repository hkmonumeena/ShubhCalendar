package com.shubhcalendar.utills

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.view.View
import android.view.animation.BounceInterpolator
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object Craft {
    private var sharedPreferences: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    private const val MY_PREF = "MY_PREF"

    fun Context.toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    inline fun <reified T : Activity> Activity.startActivity() {
        startActivity(createIntent<T>())
    }

    inline fun <reified T : Activity> Context.createIntent() =
        Intent(this, T::class.java)


    fun Context.putKey(Key: String?, Value: String?) {
        sharedPreferences =
            getSharedPreferences(MY_PREF, Context.MODE_PRIVATE)
        editor = sharedPreferences?.edit()
        editor?.putString(Key, Value)
        editor?.apply()
    }

    fun Context.getKey(Key: String?): String? {
        sharedPreferences =
            getSharedPreferences(MY_PREF, Context.MODE_PRIVATE)
        return sharedPreferences?.getString(Key, "")
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun Context.isOnline(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }


    fun Context.confirmationDialog(
        title: String,
        items: ArrayList<String>,
        id: ArrayList<String>,
        proceed: (position: Int, value: String, id: String) -> Unit
    ) {
        lateinit var itemaNamesArray: Array<String>
        lateinit var itemaIdArray: Array<String>

        if (items.size != 0) {
            itemaNamesArray = items.toTypedArray()
            itemaIdArray = id.toTypedArray()
            val checkedItem = 1
            MaterialAlertDialogBuilder(this)
                .setTitle(title)
                .setNegativeButton("Cancel") { dialog, which ->
                    // Respond to neutral button press
                }
                .setPositiveButton("Okay") { dialog, which ->
                    // Respond to positive button press

                    val position =
                        (dialog as androidx.appcompat.app.AlertDialog).listView.checkedItemPosition
                    proceed.invoke(position, itemaNamesArray[position], itemaIdArray[position])
                }
                // Single-choice items (initialized with checked item)
                .setSingleChoiceItems(itemaNamesArray, checkedItem) { dialog, which ->
                    // Respond to item chosen
                }
                .show()
        } else {
            Toast.makeText(this, "No items found", Toast.LENGTH_SHORT).show()
        }


    }

    fun EditText.setFieldError(errorMsg: String? = null) {
        return if (errorMsg.isNullOrEmpty()) {
            this.setError("Required")
        } else {
            this.setError(errorMsg)
        }

    }

    private val getValidateList = arrayListOf<Boolean>()
    fun isValidate(
        id: EditText,
        errorMsg: String? = null,
        isMobile: Boolean? = false,
        mobileNumberLength: Int = 10
    ) = apply {
        var isValidatedField = false
        when {
            id.text.isNullOrEmpty() -> {
                id.setFieldError(errorMsg)
            }
            isMobile == true -> {
                isValidatedField = if (id.length() < mobileNumberLength) {
                    false
                } else id.length() <= mobileNumberLength

            }

            else -> {
                isValidatedField = true
            }
        }
        getValidateList.add(isValidatedField)
        getValidateList.forEach {
            if (!it) isValidatedField = it
        }

    }

    fun getValidatedFields(): Boolean {
        var isValidatedField = true
        getValidateList.forEach {
            if (it) {

            } else {
                isValidatedField = it
            }
        }
        getValidateList.clear()
        return isValidatedField
    }

    fun startAnimation(view: View,startValue:Float =0f,endValue:Float=1f,animationDuration: Long = 1500, animatorListener: (ValueAnimator?) -> Unit) {
        val valueAnimator = ValueAnimator.ofFloat(startValue, endValue)
        valueAnimator.addUpdateListener {
            val value = it.animatedValue as Float
            view.scaleX = value
            view.scaleY = value

        }
        valueAnimator.interpolator = BounceInterpolator()
        valueAnimator.duration = animationDuration
        // Set animator listener.
        animatorListener(valueAnimator)
        /*valueAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {}
            override fun onAnimationEnd(p0: Animator?) {

                // Navigate to main activity on navigation end.
            }

            override fun onAnimationCancel(p0: Animator?) {}
            override fun onAnimationStart(p0: Animator?) {}
        })*/
        // Start animation.
        valueAnimator.start()
    }

     fun crossFadeHideShow(viewToShow: View, viewToHide: View) {
        viewToShow.apply {
            alpha = 0f
            isVisible = true
            animate().alpha(1f).setDuration(500L).setListener(null)
        }
        viewToHide.animate().alpha(0f).setDuration(500)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    viewToHide.isVisible = false
                }
            })
    }



    fun View.textView(id: Int): TextView {
        return findViewById(id)

    }

    fun View.editText(id: Int): EditText {
        return findViewById(id)

    }

    fun View.imageView(id: Int): ImageView {
        return findViewById(id)

    }

    fun View.materialCardView(id: Int): MaterialCardView {
        return findViewById(id)

    }

    fun View.linearLayout(id: Int): LinearLayout {
        return findViewById(id)

    }

    fun View.constraintLayout(id: Int): ConstraintLayout {
        return findViewById(id)

    }

    fun View.relativeLayout(id: Int): RelativeLayout {
        return findViewById(id)

    }   fun View.recyclerView(id: Int): RecyclerView {
        return findViewById(id)

    }









}