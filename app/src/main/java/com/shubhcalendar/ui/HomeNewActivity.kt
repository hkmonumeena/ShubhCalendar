package com.shubhcalendar.ui

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import coil.api.load
import com.e.mylibrary.Fasttrack
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.shubhcalendar.R
import com.shubhcalendar.activities.SplashActivity
import com.shubhcalendar.databinding.ActivityHomeNewBinding
import com.shubhcalendar.ui.calendar.CalendarFragment
import com.shubhcalendar.ui.childhelps.LangugageSheet
import com.shubhcalendar.ui.holidays.HolidaysFragment
import com.shubhcalendar.ui.home.HomeFragment
import com.shubhcalendar.ui.home.panchangmuhurat.PanchabgMuhurat
import com.shubhcalendar.ui.home.poojaartiskatha.ArtiVidhiKathaFrag
import com.shubhcalendar.ui.horoscope.HoroscopeFragment
import com.shubhcalendar.ui.profile.DataShowProfile
import com.shubhcalendar.ui.profile.ProfileFragment
import com.shubhcalendar.utills.Api
import com.shubhcalendar.utills.Craft.getKey
import com.shubhcalendar.utills.Craft.putKey
import com.shubhcalendar.utills.Craft.startActivity
import com.shubhcalendar.utills.Craft.toast
import com.shubhcalendar.utills.Keys
import com.trendyol.medusalib.navigator.MultipleStackNavigator
import com.trendyol.medusalib.navigator.Navigator
import com.trendyol.medusalib.navigator.NavigatorConfiguration
import com.trendyol.medusalib.navigator.transaction.NavigatorTransaction
import com.trendyol.medusalib.navigator.transitionanimation.TransitionAnimationType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class HomeNewActivity : AppCompatActivity(), Navigator.NavigatorListener, View.OnClickListener,
    CoroutineScope {
    private val rootFragmentProvider: List<() -> Fragment> = listOf(
    //    { CalendarFragment() },     //0
       // { HolidaysFragment() },     //1
        { HomeFragment() },{ ProfileFragment() })       //2
     //   { PanchabgMuhurat() },     //3
     //   { HoroscopeFragment() })    //4
    val multipleStackNavigator: MultipleStackNavigator =
        MultipleStackNavigator(
            supportFragmentManager,
            R.id.frame,
            rootFragmentProvider,
            navigatorListener = this,
            navigatorConfiguration = NavigatorConfiguration(0, true, NavigatorTransaction.SHOW_HIDE)
        )
    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
/*                R.id.navigationCalendar -> {
                    multipleStackNavigator.switchTab(0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigationHolidays -> {

                    multipleStackNavigator.switchTab(1)
                    return@OnNavigationItemSelectedListener true
                }*/
                R.id.navigationHome -> {

                    multipleStackNavigator.switchTab(0)
                    multipleStackNavigator.resetCurrentTab(true)
                    return@OnNavigationItemSelectedListener true
                }
              /*  R.id.navigationPanchang -> {

                    multipleStackNavigator.switchTab(3)
                    multipleStackNavigator.resetCurrentTab(true)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigationHoroscope -> {
                    multipleStackNavigator.switchTab(4)
                    return@OnNavigationItemSelectedListener true
                }*/
            }
            false
        }
    lateinit var binding: ActivityHomeNewBinding
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    private lateinit var job: Job
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeNewBinding.inflate(layoutInflater)
        job = Job()
        setContentView(binding.root)
        multipleStackNavigator.initialize(savedInstanceState)
        binding.bottomNavigation.setOnNavigationItemSelectedListener(
            mOnNavigationItemSelectedListener
        )
        binding.rlLanguage.setOnClickListener(this)
        binding.relativeLayoutLogout.setOnClickListener(this)
        binding.rlCalendar.setOnClickListener(this)
        binding.rlPanchang.setOnClickListener(this)
        binding.rlPooja.setOnClickListener(this)
        binding.rlHoliday.setOnClickListener(this)
        binding.rlHome.setOnClickListener(this)
        binding.ivCross.setOnClickListener(this)
        binding.cardViewProfile.setOnClickListener(this)
        showProfile()

    }

    fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame2, fragment)
        transaction.addToBackStack(null)
        supportFragmentManager.popBackStack()
        transaction.commit()
    }

    override fun onBackPressed() {
        if (multipleStackNavigator.canGoBack()) {
            multipleStackNavigator.goBack()
        } else {
                finish()
            }
    }

    override fun onTabChanged(tabIndex: Int) {
        when (tabIndex) {
            0 -> binding.bottomNavigation.selectedItemId = R.id.navigationCalendar
            1 -> binding.bottomNavigation.selectedItemId = R.id.navigationHolidays
            2 -> binding.bottomNavigation.selectedItemId = R.id.navigationHome
            3 -> binding.bottomNavigation.selectedItemId = R.id.navigationPanchang
            4 -> binding.bottomNavigation.selectedItemId = R.id.navigationHoroscope
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        multipleStackNavigator.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }


    private fun logutDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.logout_popup)
        var btn_confirm: CardView? = null
        var btn_cancel: CardView? = null
        var editTextTextEmailAddress2: TextView? = null
        btn_confirm = dialog.findViewById(R.id.btnLogout)
        btn_cancel = dialog.findViewById(R.id.btn_cancel)
        editTextTextEmailAddress2 = dialog.findViewById(R.id.editTextTextEmailAddress2)
        btn_confirm?.setOnClickListener {
            dialog.dismiss()
            putKey(Keys.userID, "")
            startActivity<SplashActivity>()
            finishAffinity()
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

    fun showProfile() {
        Fasttrack.post {
            it.apply {
                url(Api.show_profile)
                bodyParameter(mutableMapOf("user_id" to getKey(Keys.userID) as String))
                executor { result, exception ->
                    launch(coroutineContext) {
                        if (result?.responseCode == 200) {
                            if (result.responseString?.isNotEmpty() == true) {
                                val getData =
                                    createModelFromClass<DataShowProfile>(result.responseString!!)
                                if (getData.result == true) {
                                    binding.textViewUserName.text = getData.data?.name
                                    binding.textViewUserEmail.text = getData.data?.email
                                    binding.imageViewUserProfile.load(
                                        getData.data?.path.plus(
                                            getData.data?.image
                                        )
                                    )
                                }

                            } else {

                            }
                        } else {
                            toast("${exception?.responseCode} - ${exception?.responseMessage}")
                        }
                    }
                    Log.e("ProfileFragment", "showProfile: ${result?.responseString}")
                }
            }
        }
    }


    override fun onClick(v: View?) {
        when (v) {
            binding.rlLanguage -> {
                val bundle = Bundle()
                bundle.putString("openedFrom", "ShowAddressBottomsheet")
                val bottomSheet = LangugageSheet()
                bottomSheet.arguments = bundle
                bottomSheet.show(supportFragmentManager, "new address")
            }

            binding.rlHome -> {
                multipleStackNavigator.reset()
                binding.drawer.closeDrawer(GravityCompat.END)

            }
            binding.rlCalendar -> {
                multipleStackNavigator.start(CalendarFragment(),TransitionAnimationType.LEFT_TO_RIGHT)
                binding.drawer.closeDrawer(GravityCompat.END)
               // onTabChanged(0)

            }

            binding.rlPanchang -> {
          multipleStackNavigator.start(PanchabgMuhurat(),TransitionAnimationType.RIGHT_TO_LEFT)
                binding.drawer.closeDrawer(GravityCompat.END)
            }

            binding.rlPooja -> {
                multipleStackNavigator.start(
                    ArtiVidhiKathaFrag(),
                    TransitionAnimationType.RIGHT_TO_LEFT
                )
                binding.drawer.closeDrawer(GravityCompat.END)

            }

            binding.rlHoliday -> {
                multipleStackNavigator.start(HolidaysFragment(),TransitionAnimationType.RIGHT_TO_LEFT)
                binding.drawer.closeDrawer(GravityCompat.END)

            }
            binding.ivCross -> {
                binding.drawer.closeDrawer(GravityCompat.END)
            }

            binding.cardViewProfile -> {
              /*  multipleStackNavigator.start(
                    ProfileFragment(),
                    TransitionAnimationType.RIGHT_TO_LEFT
                )*/

                multipleStackNavigator?.reset(1)
               Handler().postDelayed({
                   multipleStackNavigator?.switchTab(1)
               },500)
                binding.drawer.closeDrawer(GravityCompat.END)
            }

            binding.relativeLayoutLogout -> {
                logutDialog()
            }
        }
    }
}