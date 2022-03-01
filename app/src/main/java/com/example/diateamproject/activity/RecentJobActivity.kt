package com.example.diateamproject.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.diateamproject.adapter.AllJobAdapter
import com.example.diateamproject.databinding.ActivityRecentJobBinding
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
}