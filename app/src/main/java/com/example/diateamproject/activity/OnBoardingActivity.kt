package com.example.diateamproject.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.viewpager.widget.ViewPager
import com.example.diateamproject.R
import com.example.diateamproject.adapter.OnBoardingAdapter
import com.example.diateamproject.databinding.ActivityOnBoardingBinding
import com.example.diateamproject.model.onboarding.OnBoardingData
import com.google.android.material.tabs.TabLayout

class OnBoardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnBoardingBinding
    var onBoardingAdapter: OnBoardingAdapter? = null
    var tabLayout: TabLayout? = null
    var onBoardingViewPager: ViewPager? = null
    var buttonStarted: Button? = null
    var position = 0
    var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if (restorePrefData()) {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
        }

        tabLayout = binding.tabIndicator
        buttonStarted = findViewById(R.id.btnGetStarted)

        val onBoardingData: MutableList<OnBoardingData> = ArrayList()
        onBoardingData.add(
            OnBoardingData(
                title = "Have a dream job?",
                subTitle = "Go for it now!",
                desc = "Have access to thousands of open vacancies in the most recognizable fields and more",
                R.drawable.ic_vacancy
            )
        )
        onBoardingData.add(
            OnBoardingData(
                title = "Track your job application easily!",
                subTitle = "",
                desc = "No more wondering about the status of your job application. You will have a full insight on Toptalent App!",
                R.drawable.ic_trackjob
            )
        )
        onBoardingData.add(
            OnBoardingData(
                title = "Are you an employer?",
                subTitle = "Post your job, now on toptalent.id!",
                desc = "We make your recruitment process easily",
                R.drawable.ic_screening
            )
        )

        setOnBoardingAdapter(onBoardingData)

        buttonStarted?.setOnClickListener {
//            if(position == onBoardingViewPager!!.currentItem) {
//                Log.d("cekPosition", "$position")
                savePrefData()
                val intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
                finish()
//            }

        }
    }

    private fun setOnBoardingAdapter(onBoardingData: List<OnBoardingData>) {
        onBoardingViewPager = findViewById(R.id.vpOnBoarding)
        onBoardingAdapter = OnBoardingAdapter(this, onBoardingData)
        onBoardingViewPager!!.adapter = onBoardingAdapter
        tabLayout?.setupWithViewPager(onBoardingViewPager)
    }

    private fun savePrefData() {
        sharedPreferences = applicationContext.getSharedPreferences("pref", Context.MODE_PRIVATE)
        val editor = sharedPreferences!!.edit()
        editor.putBoolean("isFirstTimeRun", true)
        editor.apply()
    }
    private fun restorePrefData(): Boolean {
        sharedPreferences = applicationContext.getSharedPreferences("pref", Context.MODE_PRIVATE)
        return sharedPreferences!!.getBoolean("isFirstTImeRun", false)
    }
}