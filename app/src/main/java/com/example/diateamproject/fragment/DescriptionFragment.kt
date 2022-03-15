package com.example.diateamproject.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.diateamproject.R
import com.example.diateamproject.activity.JobDetailsActivity
import com.example.diateamproject.adapter.AllJobAdapter
import com.example.diateamproject.databinding.FragmentDescriptionBinding
import com.example.diateamproject.listener.OnItemClickListener

class DescriptionFragment : Fragment() {

    private var _binding: FragmentDescriptionBinding? = null
    private val binding get() = _binding!!

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
        //get allJobResponse from previous activity
        val jobDescription = activity?.intent?.getStringExtra("jobDescription")
        val jobRequirement = activity?.intent?.getStringExtra("jobRequirement")
        binding.tvJobDescription.text = "$jobDescription"
        binding.tvJobRequirement.text = "$jobRequirement"


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}