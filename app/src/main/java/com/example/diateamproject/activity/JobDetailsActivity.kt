package com.example.diateamproject.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.viewpager.widget.ViewPager
import com.example.diateamproject.R
import com.example.diateamproject.adapter.DetailJobAdapter
import com.example.diateamproject.databinding.ActivityJobDetailsBinding
import com.example.diateamproject.fragment.CompanyFragment
import com.example.diateamproject.fragment.DescriptionFragment
import com.google.android.material.tabs.TabLayout

class JobDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJobDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var jobId = intent.getIntExtra("jobId", 0)
        Log.d("Success", jobId.toString())
        binding.tvJobPosition.text = "userId: $jobId"

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
    }


}