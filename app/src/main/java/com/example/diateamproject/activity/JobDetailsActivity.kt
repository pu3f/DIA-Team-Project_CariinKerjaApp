package com.example.diateamproject.activity

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.diateamproject.R
import com.example.diateamproject.adapter.DetailJobAdapter
import com.example.diateamproject.databinding.ActivityJobDetailsBinding
import com.example.diateamproject.fragment.CompanyFragment
import com.example.diateamproject.fragment.DescriptionFragment
import com.example.diateamproject.util.PrefsJobConstant
import com.example.diateamproject.util.PrefsLogin
import com.example.diateamproject.util.PrefsLoginConstant
import com.example.diateamproject.viewmodel.ApplicationStatusViewModel
import com.example.diateamproject.viewmodel.ApplyViewModel
import com.google.android.material.tabs.TabLayout
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class JobDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJobDetailsBinding
    private lateinit var btnApplyDialog : Button
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

        var jobName = intent.getStringExtra("jobName")
        var companyName = intent.getStringExtra("companyName")
        var jobLocation = intent.getStringExtra("jobLocation")
        var companyImage = intent.getStringExtra("companyImage")
        Log.d("Success", jobName.toString())
        binding.tvJobPosition.text = "$jobName"
        binding.tvCompanyName.text = "$companyName"
        binding.tvLocation.text = "$jobLocation"
        Glide.with(this!!)
            .load("http://54.255.4.75:9091/resources/$companyImage")
            .placeholder(R.drawable.ic_placeholder_list)
            .into(binding.ivCompanyLogo)

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

        dialogApply()

        binding.ivBack.setOnClickListener {
            val intent = Intent(this, RecentJobActivity::class.java)
            startActivity(intent)
        }
    }

    private fun dialogApply() {

        btnApplyDialog = findViewById(R.id.btnApply)
        btnApplyDialog.setOnClickListener {

            val dialog = Dialog (this)
            dialog.setTitle("Apply Confirm")
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.apply_dialog)

            val btnClose = dialog.findViewById<ImageView>(R.id.ivClose)
            val btnJustApply = dialog.findViewById<Button>(R.id.btnJustApply)
            val btnUpload = dialog.findViewById<Button>(R.id.btnUploadCv)

            btnClose.setOnClickListener {
                dialog.dismiss()
            }
            btnUpload.setOnClickListener {

            }
            btnJustApply.setOnClickListener {
                applyJob()
                setObserver()
                dialog.dismiss()
//                binding.btnApply.isClickable = false
            }
            dialog.show()
        }
    }

    private fun setObserver() {
        viewModelApply.responseApply().observe(this, Observer {
            val userId = PrefsLogin.loadInt(PrefsLoginConstant.USERID, 0)
            viewModelApplication.getApplicationStatus(userId)
            Toast.makeText(this, "Success Applied", Toast.LENGTH_LONG).show()
        })
    }

    private fun applyJob() {
        val jobId = PrefsLogin.loadInt(PrefsJobConstant.JOBID, 0)
            .toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val jobaseekerId = PrefsLogin.loadInt(PrefsLoginConstant.USERID, 0)
            .toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val file = null

        Log.d("testApply", "=====this is apply")
        viewModelApply.postApply(jobId, jobaseekerId, null)

    }




}