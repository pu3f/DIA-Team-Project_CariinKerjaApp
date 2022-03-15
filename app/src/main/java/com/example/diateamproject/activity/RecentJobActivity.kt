package com.example.diateamproject.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.diateamproject.adapter.AllJobAdapter
import com.example.diateamproject.databinding.ActivityRecentJobBinding
import com.example.diateamproject.listener.OnItemClickListener
import com.example.diateamproject.viewmodel.AllJobViewModel

class RecentJobActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecentJobBinding
    private val adapter = AllJobAdapter()
    private val viewModelAll: AllJobViewModel by lazy {
        ViewModelProviders.of(this).get(AllJobViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecentJobBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvListJob.layoutManager = layoutManager
        viewModelAll.getAllJobs()
        setObserver()

        action()

        binding.ivBack.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setObserver() {
        viewModelAll.allListResponse().observe(this, Observer {
            binding.rvListJob.adapter = adapter
            Log.d("listapp", "if22")
            adapter.initData(it)


        })

    }

    private fun action() {
        adapter.setOnClickItemListener(object : OnItemClickListener {
            override fun onItemClick(item: View, position: Int) {
                Intent(this@RecentJobActivity, JobDetailsActivity::class.java).also {
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