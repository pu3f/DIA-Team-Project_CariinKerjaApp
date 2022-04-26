package com.example.diateamproject.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.diateamproject.adapter.AllJobAdapter
import com.example.diateamproject.databinding.ActivityRecentJobBinding
import com.example.diateamproject.listener.OnItemClickListener
import com.example.diateamproject.model.allpostingjobs.Content
import com.example.diateamproject.util.EndlessScrollingRecyclerView
import com.example.diateamproject.viewmodel.AllJobViewModel

class RecentJobActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {
    private lateinit var binding: ActivityRecentJobBinding
    private val adapter = AllJobAdapter()
    private var page = 0
    private var size = 10
    private var isLastPage = false
    private var isLoading = false
    var jobArray: ArrayList<Content> = ArrayList<Content>()
    private val viewModelAllJob: AllJobViewModel by lazy {
        ViewModelProviders.of(this).get(AllJobViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecentJobBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvListJob.layoutManager = layoutManager
        binding.srJobList.setOnRefreshListener(this)
        getAllJobs(false)

        var scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    Log.d("testPage", "$page")
                    doLoadData()
                }
            }
        }
        binding.rvListJob.addOnScrollListener(scrollListener)
        doLoadData()
        setObserver()
        action()

        binding.ivBack.setOnClickListener { onBackPressed() }
    }

    private fun getAllJobs(isOnRefresh: Boolean) {
        isLoading = true
        if (!isOnRefresh) binding.pbjobs.visibility = View.VISIBLE
        var page = page++
        viewModelAllJob.getAllJobs(page, size)
        Log.d("testJobPage", "$page")
    }

    private fun doLoadData() {
        jobArray = adapter.allJobList
        Log.d("this array", "ja" + jobArray)
        viewModelAllJob.getAllJobs(page, size)
//        binding.rvListJob.scrollTo(0,10)
    }

    private fun setObserver() {
        viewModelAllJob.allListResponse().observe(this, Observer {
            isLastPage = it.last

            if (it != null) {
                Log.d("listjob", "if11")
//                binding.rvListJob.setHasFixedSize(true)
                binding.rvListJob.adapter = adapter
            }

            if (page != 0) {
                Log.d("listjob", "if22")
                adapter.allJobList.addAll(
                    it.content as ArrayList<Content>
                )
                adapter.notifyDataSetChanged()
                adapter.notifyItemRangeChanged(
                    adapter.allJobList.size - it.content.size,
                    adapter.allJobList.size
                )
            } else {
                Log.d("listjob", "if33 $page")
//                binding.rvListJob.setHasFixedSize(true)
                binding.rvListJob.adapter = adapter
                adapter.initData(it.content as ArrayList<Content>)
            }

            if (isLastPage) {
                binding.pbjobs.visibility = View.GONE
            } else {
                binding.pbjobs.visibility = View.INVISIBLE
            }

            if (adapter.itemCount == 0) {
                binding.rvListJob.visibility = View.GONE
                binding.llNoJob.visibility = View.VISIBLE
                binding.pbjobs.visibility = View.INVISIBLE
            } else {
                binding.rvListJob.visibility = View.VISIBLE
                binding.llNoJob.visibility = View.GONE
            }
            isLoading = false
            binding.srJobList.isRefreshing = false
        })
    }

    override fun onRefresh() {
        adapter.clear()
        page = 0
        getAllJobs(true)
    }

    private fun action() {
        adapter.setOnClickItemListener(object : OnItemClickListener {
            override fun onItemClick(item: View, position: Int) {
                Intent(this@RecentJobActivity, JobDetailsActivity::class.java).also {
                    it.putExtra("jobId", adapter.allJobList[position].jobId)
                    startActivity(it)
                }
            }
        })
    }
}