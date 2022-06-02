package com.example.diateamproject.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.diateamproject.databinding.FragmentDescriptionBinding
import com.example.diateamproject.util.ConvertHtml
import com.example.diateamproject.util.PrefsLogin
import com.example.diateamproject.util.PrefsLoginConstant
import com.example.diateamproject.viewmodel.JobDetailViewModel

class DescriptionFragment : Fragment() {
    private var _binding: FragmentDescriptionBinding? = null
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
        _binding = FragmentDescriptionBinding.inflate(inflater, container, false)
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
            val desc = it.data.jobDesc
            val require = it.data.jobRequirement
            val benefit = it.data.recruiterBenefit
            Log.d("isBenefitNull?", "benefit = $benefit")
            if (it != null) {
                binding.tvJobDescription.text = convertHtml.convertHtmlString(desc)
                binding.tvJobRequirement.text = convertHtml.convertHtmlString(require)
            } else {
                binding.tvJobDescription.text = "-"
                binding.tvJobRequirement.text = "-"
            }
            if (benefit.isNullOrEmpty()) {
                Log.d("is benefit null?", "benefit = $benefit")
                binding.tvJobBenefit.text = "-"
            } else {
                binding.tvJobBenefit.text = convertHtml.convertHtmlString(benefit)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}