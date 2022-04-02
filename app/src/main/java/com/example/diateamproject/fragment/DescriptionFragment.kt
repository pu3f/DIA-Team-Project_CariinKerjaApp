package com.example.diateamproject.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.diateamproject.R
import com.example.diateamproject.activity.JobDetailsActivity
import com.example.diateamproject.adapter.AllJobAdapter
import com.example.diateamproject.databinding.FragmentDescriptionBinding
import com.example.diateamproject.listener.OnItemClickListener
import com.example.diateamproject.viewmodel.JobDetailViewModel

class DescriptionFragment : Fragment() {

    private var _binding: FragmentDescriptionBinding? = null
    private val binding get() = _binding!!
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

        val jobId = activity?.intent!!.getIntExtra("jobId",0)
        viewModelJobDetail.getJobById(jobId)
        setObserver()

    }

    private fun setObserver() {
        viewModelJobDetail.listJobResponse().observe(viewLifecycleOwner, Observer {
            binding.tvJobDescription.text = it.data.jobDesc
            binding.tvJobRequirement.text = it.data.jobRequirement
            binding.tvJobBenefit.text = it.data.recruiterBenefit
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}