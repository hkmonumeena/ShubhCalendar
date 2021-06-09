package com.shubhcalendar.ui.childhelps

import android.app.Dialog
import android.content.Intent
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.shubhcalendar.activities.SplashActivity
import com.shubhcalendar.databinding.ActivityLanguageBinding
import com.shubhcalendar.utills.BaseBottomSheetFragment
import com.shubhcalendar.utills.Craft.getKey
import com.shubhcalendar.utills.Craft.putKey
import com.shubhcalendar.utills.Keys
import com.shubhcalendar.utills.LocaleUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext


class LangugageSheet : BaseBottomSheetFragment(), View.OnClickListener, CoroutineScope {
    lateinit var binder: ActivityLanguageBinding
    lateinit var myDialog: Dialog
    lateinit var inflater: LayoutInflater
    lateinit var layout: View
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    lateinit var job: Job
    val bundle = Bundle()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener {
            val bottomSheet =
                (it as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
            val behavior = BottomSheetBehavior.from(bottomSheet!!)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            BottomSheetBehavior.from(bottomSheet).peekHeight =
                Resources.getSystem().displayMetrics.heightPixels
            behavior.isDraggable = false
        }

        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binder = ActivityLanguageBinding.inflate(layoutInflater)
        return binder.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        job = Job()
        myDialog = Dialog(requireActivity())
        binder.rlBack.setOnClickListener(this)
        binder.cardViewHindi.setOnClickListener(this)
        binder.cardViewEnglish.setOnClickListener(this)
        binder.cardViewBengali.setOnClickListener(this)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onClick(v: View?) {
        when (v) {
            binder.rlBack -> {
                dismiss()
            }
            binder.cardViewHindi -> {
                requireActivity().putKey(Keys.Language, "hi")
                langcheck()
            }
            binder.cardViewEnglish -> {
                requireActivity().putKey(Keys.Language, "en")
                langcheck()
            }
            binder.cardViewBengali -> {
                requireActivity().putKey(Keys.Language, "bn")
                langcheck()
            }

        }
    }

    fun langcheck() {
        val Lang = requireActivity().getKey(Keys.Language)
        if (Lang == "en") {
            LocaleUtils.setLocale(requireActivity(), 1)
        } else if (Lang == "hi") {
            LocaleUtils.setLocale(requireActivity(), 0)
        } else if (Lang == "bn") {
            LocaleUtils.setLocale(requireActivity(), 2)
        } else {
            LocaleUtils.setLocale(requireActivity(), 1)
        }

        requireActivity().putKey(Keys.isLanguageSelected, "true")
        startActivity(Intent(requireActivity(), SplashActivity::class.java))
        requireActivity().finishAffinity()
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }
}