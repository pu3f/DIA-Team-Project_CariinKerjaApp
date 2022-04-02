package com.example.diateamproject.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
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
import com.example.diateamproject.viewmodel.ApplicationStatusViewModel
import com.example.diateamproject.viewmodel.ApplyViewModel
import com.example.diateamproject.viewmodel.JobDetailViewModel
import com.google.android.material.tabs.TabLayout
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.text.SimpleDateFormat
import java.util.*

class JobDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJobDetailsBinding
    private lateinit var btnApplyDialog : Button
    private val idJob : Int by lazy { intent!!.getIntExtra("jobId", 0) }
    private val userId = PrefsLogin.loadInt(PrefsLoginConstant.USERID, 0)
    val formatDate = SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH)
    private val viewModelJobDetail: JobDetailViewModel by lazy {
        ViewModelProviders.of(this).get(JobDetailViewModel::class.java)
    }
    private val viewModelApply: ApplyViewModel by lazy {
        ViewModelProviders.of(this).get(ApplyViewModel::class.java)
    }
    private val viewModelApplication: ApplicationStatusViewModel by lazy {
        ViewModelProviders.of(this).get(ApplicationStatusViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModelJobDetail.getJobById(idJob)
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
            val dateString = formatDate.format(it.data.createdAt)
            binding.tvCreateAt.text = String.format("%s", dateString)
            val companyImage = it.data.recruiterImage
            binding.tvJobType.text = it.data.jobPosition
            binding.tvJobsalary.text = it.data.jobSalary.toString()
            Glide.with(this)
                .load("http://54.255.4.75:9091/resources/$companyImage")
                .placeholder(R.drawable.ic_placeholder_list)
                .into(binding.ivCompanyLogo)

        })
        viewModelApply.responseApply().observe(this, Observer {
            if (it != null) {
                binding.btnApply.isEnabled = false
            }

        })
    }

    private fun applyJob() {
        val jobId = idJob.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val jobaseekerId =
            userId.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())

        Log.d("testApply", "=====$jobId")
        viewModelApply.postApply(jobId, jobaseekerId)
    }
}