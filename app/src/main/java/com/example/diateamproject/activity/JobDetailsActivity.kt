package com.example.diateamproject.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager.widget.ViewPager
import com.example.diateamproject.R
import com.example.diateamproject.adapter.DetailJobAdapter
import com.example.diateamproject.databinding.ActivityJobDetailsBinding
import com.example.diateamproject.fragment.ApplicationFragment
import com.example.diateamproject.fragment.CompanyFragment
import com.example.diateamproject.fragment.DescriptionFragment
import com.google.android.material.tabs.TabLayout

class JobDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJobDetailsBinding
    private lateinit var btnApplyDialog : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var jobName = intent.getStringExtra("jobName")
        var companyName = intent.getStringExtra("companyName")
        var jobLocation = intent.getStringExtra("jobLocation")
        Log.d("Success", jobName.toString())
        binding.tvJobPosition.text = "$jobName"
        binding.tvCompanyName.text = "$companyName"
        binding.tvLocation.text = "$jobLocation"

        //object viewPager & tabLayout
        var viewPager = findViewById(R.id.viewPager) as ViewPager
        var tabLayout = findViewById(R.id.tabLayout) as TabLayout

        //object detailJobAdapter
        val detailJobAdapter = DetailJobAdapter(supportFragmentManager)

        //call addFragment Method
        detailJobAdapter.addFragment(DescriptionFragment(),"Description")
        detailJobAdapter.addFragment(CompanyFragment(),"Company")

        //set adapter to viewPager
        viewPager.adapter = detailJobAdapter
        tabLayout.setupWithViewPager(viewPager)


        btnApplyDialog = findViewById(R.id.btnApply)
        btnApplyDialog.setOnClickListener {

            val dialog = Dialog (this)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.apply_dialog)

            val btnClose = dialog.findViewById<ImageView>(R.id.ivClose)
            val btnApplyNow = dialog.findViewById<Button>(R.id.btnApplyNow)

            btnClose.setOnClickListener {
                dialog.dismiss()
            }
            btnApplyNow.setOnClickListener {
                val intent = Intent(this, MenuActivity::class.java)
                startActivity(intent)
                Toast.makeText(this, "Success Applied", Toast.LENGTH_LONG).show()
            }
            dialog.show()
        }

        binding.ivBack.setOnClickListener {
            val intent = Intent(this, RecentJobActivity::class.java)
            startActivity(intent)
        }
    }


}