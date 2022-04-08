package com.example.diateamproject.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.diateamproject.R
import com.example.diateamproject.adapter.DetailJobAdapter
import com.example.diateamproject.databinding.ActivityJobDetailsBinding
import com.example.diateamproject.fragment.ApplyDialogFragment
import com.example.diateamproject.fragment.CompanyFragment
import com.example.diateamproject.fragment.DescriptionFragment
import com.example.diateamproject.util.PrefsLogin
import com.example.diateamproject.util.PrefsLoginConstant
import com.example.diateamproject.viewmodel.JobDetailViewModel
import com.google.android.material.tabs.TabLayout

class JobDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJobDetailsBinding
    private val idJob : Int by lazy { intent!!.getIntExtra("jobId", 0) }
    private val userId = PrefsLogin.loadInt(PrefsLoginConstant.USERID, 0)
    private val viewModelJobDetail: JobDetailViewModel by lazy {
        ViewModelProviders.of(this).get(JobDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModelJobDetail.getJobById(idJob, userId)
        setObserver()

        //object viewPager & tabLayout
        var viewPager = findViewById<ViewPager>(R.id.viewPager)
        var tabLayout = findViewById<TabLayout>(R.id.tabLayout)

        //object detailJobAdapter
        val detailJobAdapter = DetailJobAdapter(supportFragmentManager)

        //call addFragment Method
        detailJobAdapter.addFragment(DescriptionFragment(),"Job")
        detailJobAdapter.addFragment(CompanyFragment(),"Company")

        //set adapter to viewPager
        viewPager.adapter = detailJobAdapter
        tabLayout.setupWithViewPager(viewPager)

        binding.btnApply.setOnClickListener {
            var dialog = ApplyDialogFragment()
            dialog.onApplied = {
                binding.btnApply.isEnabled = false
            }
            dialog.show(supportFragmentManager,"applyDialog")
        }

        binding.ivBack.setOnClickListener {
            val intent = Intent(this, RecentJobActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setObserver() {
        viewModelJobDetail.listJobResponse().observe(this, Observer {
            val jobName = it.data.jobName
            Log.d("testDetail", "==== $jobName")
            binding.tvJobPosition.text = jobName
            binding.tvCompanyName.text = it.data.recruiterCompany
            binding.tvLocation.text = it.data.jobAddress
            val date = it.data.createdAt.substringBefore(" ")
            binding.tvCreateAt.text = date
            val companyImage = it.data.recruiterImage
            binding.tvJobType.text = it.data.jobPosition
            binding.tvJobsalary.text = it.data.jobSalary.toString()
            Glide.with(this)
                .load("http://54.255.4.75:9091/resources/$companyImage")
                .placeholder(R.drawable.ic_placeholder_list)
                .into(binding.ivCompanyLogo)
            val applicationStatus = it.data.applicationStatus
            binding.btnApply.isEnabled = applicationStatus == "rejected" || false
        })
    }
}
