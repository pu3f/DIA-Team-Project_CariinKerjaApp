package com.example.diateamproject.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.diateamproject.activity.JobDetailsActivity
import com.example.diateamproject.activity.RecentJobActivity
import com.example.diateamproject.adapter.AllJobAdapter
import com.example.diateamproject.databinding.FragmentHomeBinding
import com.example.diateamproject.listener.OnItemClickListener
import com.example.diateamproject.util.PrefsLogin
import com.example.diateamproject.util.PrefsLoginConstant
import com.example.diateamproject.viewmodel.RecentJobViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val adapter = AllJobAdapter()
    private val viewModelRecent: RecentJobViewModel by lazy {
        ViewModelProviders.of(this).get(RecentJobViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return  view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvListJob.layoutManager = layoutManager

        viewModelRecent.getRecentJobs()
        setObserver()

        action()

        //get jobseekerName from login activity
        val username = getActivity()?.getIntent()?.getStringExtra("username")
        binding.tvHello.text = "Hello, $username"

//        val username =binding.tvHello.text.toString()
//        PrefsLogin.loadString(PrefsLoginConstant.JOBSEEKERNAME,username)

        Log.d("data", "not null")


        binding.tvShowMore.setOnClickListener {
            val intent = Intent(requireContext(), RecentJobActivity::class.java)
            startActivity(intent)
        }

    }

    private fun setObserver() {
        viewModelRecent.allListResponse().observe(viewLifecycleOwner, Observer {
            binding.rvListJob.adapter = adapter
            Log.d("listapp", "if22")
            adapter.initData(it)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun action() {
        //use function from adapter
        adapter.setOnClickItemListener(object : OnItemClickListener {
            override fun onItemClick(item: View, position: Int) {
                Intent(requireContext(), JobDetailsActivity::class.java).also {
                    it.putExtra("jobName", adapter.list[position].jobName)
                    it.putExtra("companyName", adapter.list[position].recruiterCompany)
                    it.putExtra("jobLocation", adapter.list[position].jobAddress)
                    it.putExtra("jobDescription", adapter.list[position].jobDesc)
                    it.putExtra("jobRequirement", adapter.list[position].jobRequirement)
                    it.putExtra("companyDescription", adapter.list[position].recruiterDesc)
                    startActivity(it)
                }
            }

        })
    }
}