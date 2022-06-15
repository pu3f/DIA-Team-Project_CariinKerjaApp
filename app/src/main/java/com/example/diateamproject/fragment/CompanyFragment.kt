package com.example.diateamproject.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.diateamproject.databinding.FragmentCompanyBinding
import com.example.diateamproject.util.ConvertHtml
import com.example.diateamproject.util.PrefsLogin
import com.example.diateamproject.util.PrefsLoginConstant
import com.example.diateamproject.viewmodel.JobDetailViewModel

class CompanyFragment : Fragment() {
    private var _binding: FragmentCompanyBinding? = null
    private val binding get() = _binding!!
    private val userId = PrefsLogin.loadInt(PrefsLoginConstant.USERID, 0)
    val convertHtml = ConvertHtml()
    private val viewModelJobDetail: JobDetailViewModel by lazy {
        ViewModelProviders.of(this).get(JobDetailViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCompanyBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val jobId = activity?.intent!!.getIntExtra("jobId", 0)
        viewModelJobDetail.getJobById(jobId, userId)
        setObserver()
    }

    private fun setObserver() {
        viewModelJobDetail.listJobResponse().observe(viewLifecycleOwner, Observer {
            val aboutCompany = it.data.recruiterDesc
            val industries = it.data.recruiterIndustry
            val employee = it.data.recruiterStaff
            Log.d("isEmployeeNull?", "employee = $employee")
            val culture = it.data.recruiterCulture
            val website = it.data.recruiterWebsite
            val ig = it.data.recruiterIg
            val linkedin = it.data.recruiterLinkedin
            val fb = it.data.recruiterFb

            if (aboutCompany.isNullOrEmpty() || aboutCompany == ("")) {
                binding.tvAboutCompany.text = "-"
            } else {
                binding.tvAboutCompany.text = aboutCompany
            }
            if (industries.isNullOrEmpty() || industries == ("")) {
                binding.tvIndustries.text = "-"
            } else {
                binding.tvIndustries.text = industries
            }
            if (employee.isNullOrEmpty() || employee == ("")) {
                binding.tvEmployee.text = "-"
            } else {
                binding.tvEmployee.text = employee
            }
            if (culture.isNullOrEmpty() || culture == ("")) {
                binding.tvCulture.text = "-"
            } else {
                binding.tvCulture.text = convertHtml.convertHtmlString(culture)
            }
            if (website.isNullOrEmpty() || website == ("")) {
                binding.ivLink.visibility = View.GONE
                binding.tvWebsite.text = "-"
            } else {
                binding.tvWebsite.text = website
            }
            if (linkedin.isNullOrEmpty() ||linkedin == ("")) {
                binding.llLinkedin.visibility = View.GONE
            } else {
                binding.tvLinkedin.text = linkedin
            }
            if (fb.isNullOrEmpty() || fb == ("")) {
                binding.llFb.visibility = View.GONE
            } else {
                binding.tvFb.text = fb
            }
            if (ig.isNullOrEmpty() || ig == ("")) {
                binding.llIg.visibility = View.GONE
            } else {
                binding.tvIg.text = ig
            }
        })
    }
}