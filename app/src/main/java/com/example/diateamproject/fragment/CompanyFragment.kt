package com.example.diateamproject.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.diateamproject.R
import com.example.diateamproject.databinding.FragmentCompanyBinding
import com.example.diateamproject.databinding.FragmentDescriptionBinding


class CompanyFragment : Fragment() {

    private var _binding: FragmentCompanyBinding? = null
    private val binding get() = _binding!!

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
        //get allJobResponse from previous activity
        val aboutCompany = activity?.intent?.getStringExtra("companyDescription")
        binding.tvAboutCompany.text = "$aboutCompany"
    }
}