package com.shubhcalendar.ui.home.poojaartiskatha.childhelp

import android.app.Dialog
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.SeekBar
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.shubhcalendar.R
import com.shubhcalendar.databinding.PlayingListSheetBinding
import kotlinx.android.synthetic.main.playing_list_sheet.*
import kotlinx.coroutines.Job
import java.io.IOException
import java.util.concurrent.TimeUnit

class PlayingListSheet: BottomSheetDialogFragment(), View.OnClickListener {
   lateinit var binding:PlayingListSheetBinding
   var mPlayer:MediaPlayer? = null
   private var oTime = 0
   private var sTime:Int = 0
   private var eTime:Int = 0
   private var fTime:Int = 5000
   private var bTime:Int = 5000
   private val hdlr:Handler = Handler()
   lateinit var dialoglocal:BottomSheetDialog
   val job = Job()

   override fun onCreate(savedInstanceState:Bundle?) {
      super.onCreate(savedInstanceState)
      setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
   }

   override fun onCreateDialog(savedInstanceState:Bundle?):Dialog {
      val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
      dialog.setOnShowListener {
         val bottomSheet = (it as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
         val behavior = BottomSheetBehavior.from(bottomSheet !!)
         behavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
         behavior.state = BottomSheetBehavior.STATE_COLLAPSED

         BottomSheetBehavior.from(bottomSheet).peekHeight = 330
         behavior.isDraggable = true
         behavior.isHideable = false
         behavior.expandedOffset  =100
        dialoglocal = dialog
      }

      return dialog
   }

   override fun onCreateView(inflater:LayoutInflater, container:ViewGroup?, savedInstanceState:Bundle?):View {
      binding = PlayingListSheetBinding.inflate(layoutInflater)

      return binding.root
   }

   override fun onViewCreated(view:View, savedInstanceState:Bundle?) {





      }


   override fun onClick(v:View?) {
      when (v) {
      }
   }
}