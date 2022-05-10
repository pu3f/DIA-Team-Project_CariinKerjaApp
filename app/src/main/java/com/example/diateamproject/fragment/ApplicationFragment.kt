package com.example.diateamproject.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.diateamproject.activity.JobDetailsActivity
import com.example.diateamproject.activity.MenuActivity
import com.example.diateamproject.adapter.ApplicationStatusAdapter
import com.example.diateamproject.databinding.FragmentApplicationBinding
import com.example.diateamproject.listener.OnItemClickListener
import com.example.diateamproject.model.applyjobstatus.Content
import com.example.diateamproject.util.EndlessScrollingRecyclerView
import com.example.diateamproject.util.PrefsLogin
import com.example.diateamproject.util.PrefsLoginConstant
import com.example.diateamproject.viewmodel.ApplicationStatusViewModel

class ApplicationFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private var _binding: FragmentApplicationBinding? = null
    private val binding get() = _binding!!
    private val adapter = ApplicationStatusAdapter()
    var applyArray: ArrayList<Content> = ArrayList<Content>()
    val userId = PrefsLogin.loadInt(PrefsLoginConstant.USERID, 0)

    //request not private
    var page = 0
    var size = 10

    //response
    private var totalPage = 0
    private var isLastPage = false
    private var isLoading = false
    var layoutManager: RecyclerView.LayoutManager? = null
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
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvListApplication.layoutManager = layoutManager
//        binding.swipeRefresh.setOnRefreshListener(this)
        getApplyJobStatus(false)

        var scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    Log.d("listStatus", "$page")
                    doLoadData()
                }
            }
        }
        binding.rvListApplication.addOnScrollListener(scrollListener)
        doLoadData()
        setObserver()
        action()

        binding.ivBack.setOnClickListener {
            val intent = Intent(requireContext(), MenuActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getApplyJobStatus(isOnRefresh: Boolean) {
        isLoading = true
        if (isOnRefresh) {
            binding.progressBar.visibility = View.VISIBLE
            viewModelApplication.getApplyJobStatus(userId, page, size)
        }
    }

    private fun setObserver() {
        viewModelApplication.allListResponse().observe(viewLifecycleOwner, Observer {
            isLastPage = it.last

            if (it != null) {
                Log.d("listStatus", "if11")
                binding.rvListApplication.setHasFixedSize(true)
                binding.rvListApplication.adapter = adapter
            }

            if (page != 0) {
                Log.d("listStatus", "if123 $page")
                adapter.applicationList.addAll(
                    it.content as ArrayList<Content>
                )
                adapter.notifyDataSetChanged()
                adapter.notifyItemRangeChanged(
                    adapter.applicationList.size - it.content.size,
                    adapter.applicationList.size
                )
            } else {
                binding.rvListApplication.adapter = adapter
                Log.d("listStatus", "if33")
                adapter.initData(it.content as ArrayList<Content>)
            }

            if (isLastPage) {
                binding.progressBar.visibility = View.GONE
            } else {
                binding.progressBar.visibility = View.INVISIBLE
            }

            if (adapter.itemCount == 0) {
                binding.rvListApplication.visibility = View.GONE
                binding.llNoApplication.visibility = View.VISIBLE

            } else {
                binding.rvListApplication.visibility = View.VISIBLE
                binding.llNoApplication.visibility = View.GONE
            }
            isLoading = false
//            binding.swipeRefresh.isRefreshing = false
        })
    }

    private fun doLoadData() {
        applyArray = adapter.applicationList
        Log.d("this array", "ne" + applyArray)
        viewModelApplication.getApplyJobStatus(userId, page, size)
    }

    private fun action() {
        adapter.setOnClickItemListener(object : OnItemClickListener {
            override fun onItemClick(item: View, position: Int) {
                Intent(requireContext(), JobDetailsActivity::class.java).also {
                    it.putExtra("jobId", adapter.applicationList[position].jobId)
                    startActivity(it)
                }
            }

        })
    }

    override fun onRefresh() {
        adapter.clear()
        page = 0
        getApplyJobStatus(true)
    }
}