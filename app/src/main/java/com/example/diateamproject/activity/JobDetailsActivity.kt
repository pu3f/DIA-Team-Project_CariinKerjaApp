package com.example.diateamproject.activity

import android.annotation.SuppressLint
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
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.pixplicity.easyprefs.library.Prefs
import java.text.NumberFormat
import java.util.*

class JobDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJobDetailsBinding
    private val idJob: Int by lazy { intent!!.getIntExtra("jobId", 0) }
    private val userId = PrefsLogin.loadInt(PrefsLoginConstant.USERID, 0)
    private val localeId = Locale("in", "ID")
    private val currencyFormatter = NumberFormat.getCurrencyInstance(localeId)
    private val viewModelJobDetail: JobDetailViewModel by lazy {
        ViewModelProviders.of(this).get(JobDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModelJobDetail.getJobById(idJob, userId)
        setObserver()

        //object viewPager & tabLayout
        val viewPager = findViewById<ViewPager>(R.id.viewPager)
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)

        //object detailJobAdapter
        val detailJobAdapter = DetailJobAdapter(supportFragmentManager)

        //call addFragment Method
        detailJobAdapter.addFragment(DescriptionFragment(), "Job")
        detailJobAdapter.addFragment(CompanyFragment(), "Company")

        //set adapter to viewPager
        viewPager.adapter = detailJobAdapter
        tabLayout.setupWithViewPager(viewPager)

        binding.btnApply.setOnClickListener {
            //get isLogin conditions
            val isLogin = Prefs.getBoolean(PrefsLoginConstant.IS_LOGIN, false)
            if (isLogin) {
                val dialog = ApplyDialogFragment()
                dialog.onApplied = {
                    binding.btnApply.isEnabled = false
                    Snackbar.make(view, "Application success", Snackbar.LENGTH_SHORT).show()
                }
                dialog.show(supportFragmentManager, "applyDialog")
            } else {
                //for guest
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }

        binding.ivBack.setOnClickListener { onBackPressed() }
    }

    @SuppressLint("SetTextI18n")
    private fun setObserver() {
        viewModelJobDetail.listJobResponse().observe(this, Observer {
            val jobName = it.data.jobName
            Log.d("testDetail", "==== $jobName")
            val date = it.data.createdAt.substringBefore(" ")
            val companyImage = it.data.recruiterImage
            val salary = it.data.jobSalary
            val salaryFormat = currencyFormatter.format(salary.toDouble())
            val applicationStatus = it.data.applicationStatus

            binding.tvJobPosition.text = jobName
            binding.tvCompanyName.text = it.data.recruiterCompany
            binding.tvLocation.text = it.data.jobAddress
            binding.tvCreateAt.text = date
            binding.tvJobType.text = it.data.jobPosition
            binding.tvJobsalary.text =
                salaryFormat.replace("Rp", "IDR ", true).substringBefore(",")

            Glide.with(this)
                .load("http://54.255.4.75:9091/resources/$companyImage")
                .placeholder(R.drawable.ic_placeholder_list)
                .into(binding.ivCompanyLogo)

            if (!(applicationStatus == "sent" || applicationStatus == "accepted" || applicationStatus == "screening")) {
                binding.btnApply.isEnabled = true
            } else {
                binding.btnApply.isEnabled = false
                binding.btnApply.setText("Your application $applicationStatus")
                    .apply {
                        if (this.equals("Sent")) {
                            binding.btnApply.setBackgroundResource(R.color.medium_blue)
                        }
                    }
            }
        })
    }
}