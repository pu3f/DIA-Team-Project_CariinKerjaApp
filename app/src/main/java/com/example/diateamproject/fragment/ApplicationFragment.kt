package com.example.diateamproject.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.diateamproject.R
import com.example.diateamproject.activity.MenuActivity
import com.example.diateamproject.activity.RecentJobActivity
import com.example.diateamproject.adapter.AllJobAdapter
import com.example.diateamproject.adapter.ApplicationStatusAdapter
import com.example.diateamproject.databinding.FragmentApplicationBinding
import com.example.diateamproject.databinding.FragmentHomeBinding
import com.example.diateamproject.util.PrefsLogin
import com.example.diateamproject.util.PrefsLoginConstant
import com.example.diateamproject.viewmodel.ApplicationStatusViewModel
import com.example.diateamproject.viewmodel.RecentJobViewModel

class ApplicationFragment : Fragment() {

    private var _binding: FragmentApplicationBinding? = null
    private val binding get() = _binding!!
    private val adapter = ApplicationStatusAdapter()
    private val viewModelApplication: ApplicationStatusViewModel by lazy {
        ViewModelProviders.of(this).get(ApplicationStatusViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentApplicationBinding.inflate(inflater, container, false)
        val view = binding.root
        return  view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId = PrefsLogin.loadInt(PrefsLoginConstant.USERID, 0)
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvListApplication.layoutManager = layoutManager

        viewModelApplication.getApplicationStatus(userId)
        setObserver()

        binding.ivBack.setOnClickListener {
            val intent = Intent(requireContext(), MenuActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setObserver() {
        viewModelApplication.allListResponse().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                binding.rvListApplication.adapter = adapter
                Log.d("listapp", "if22")
                adapter.initData(it)
                binding.tvJobList.isGone = true
                binding.tvApplyNow.isGone = true
            }  else {
                binding.tvJobList.isGone = false
                binding.tvApplyNow.isGone = false
            }
        })
    }
}