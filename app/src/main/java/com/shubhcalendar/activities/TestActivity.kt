package com.shubhcalendar.activities

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinelearn.roomdatabase.AppDatabase
import com.example.kotlinelearn.roomdatabase.ModelDb
import com.shubhcalendar.databinding.ActivityTestBinding
import com.shubhcalendar.roomdb.Address
import com.shubhcalendar.roomdb.User
import kotlin.concurrent.thread


class TestActivity : AppCompatActivity() {

    lateinit var binding: ActivityTestBinding
    lateinit var locaDb: AppDatabase
    private val listData: ArrayList<User> = ArrayList()
    private val listNames: java.util.ArrayList<String> = java.util.ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(binding.root)
        locaDb = AppDatabase.getAppDatabase(this)!!

        binding.btnTest.setOnClickListener {
            val address = Address("dsds","Dsdsds","Dsadsd",462010)
            val user = User(Math.random().toInt(), "fdfdfdfdf",address)
            thread {
                locaDb.getTest().insert(user)
            }

            runOnUiThread {
              locaDb.getTest().getAllUsers().observe(this){
                  listData.addAll(it)
                  Toast.makeText(this, "${it[0].address}", Toast.LENGTH_SHORT).show()
              }
               // Toast.makeText(this,listData[0].name, Toast.LENGTH_SHORT).show()


            }
        }


    }

}




