package com.example.diateamproject.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.diateamproject.adapter.SearchJobAdapter
import com.example.diateamproject.databinding.ActivitySearchBinding
import com.example.diateamproject.listener.OnItemClickListener
import com.example.diateamproject.model.searchjob.Jobs
import com.example.diateamproject.viewmodel.SearchJobViewModel
import kotlin.collections.ArrayList

class SearchActivity : AppCompatActivity() {
    private  lateinit var binding: ActivitySearchBinding
    private val adapter = SearchJobAdapter()
    private val viewModelSearchJob: SearchJobViewModel by lazy {
        ViewModelProviders.of(this).get(SearchJobViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val  layoutManager = LinearLayoutManager(this)
        binding.rvListJob.layoutManager = layoutManager

        binding.srvJob.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query!=null) {
                    binding.rvListJob.scrollToPosition(0)
                    viewModelSearchJob.getSearchJob(query)
                    binding.srvJob.clearFocus()
                    binding.tvNoJobList.visibility = View.GONE
                } else {
//                    binding.rvListJob.visibility = View.GONE
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText!!.isEmpty()) {
//                    binding.rvListJob.visibility = View.GONE
                }
                return true
            }
        })
        setObserver()
        action()

        binding.ivBack.setOnClickListener { onBackPressed() }
    }

    private fun setObserver() {
        viewModelSearchJob.listJobsResponse().observe(this, Observer {
            binding.rvListJob.adapter = adapter
            Log.d("listapp", "if22")
            adapter.initData(it.data as ArrayList<Jobs>)
        })

        viewModelSearchJob.getIsError().observe(this, Observer {
            binding.tvNoJobList.visibility = View.VISIBLE
        })
    }

    private fun action() {
        adapter.setOnClickItemListener(object : OnItemClickListener {
            override fun onItemClick(item: View, position: Int) {
                Intent(this@SearchActivity, JobDetailsActivity::class.java).also {
                    it.putExtra("jobId", adapter.listJob[position].jobId)
                    startActivity(it)
                }
            }
        })
    }
}