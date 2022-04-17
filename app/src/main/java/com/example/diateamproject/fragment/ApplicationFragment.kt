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
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.diateamproject.activity.MenuActivity
import com.example.diateamproject.adapter.ApplicationStatusAdapter
import com.example.diateamproject.databinding.FragmentApplicationBinding
import com.example.diateamproject.model.applyjobstatus.Content
import com.example.diateamproject.util.PrefsLogin
import com.example.diateamproject.util.PrefsLoginConstant
import com.example.diateamproject.viewmodel.ApplicationStatusViewModel

class ApplicationFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private var _binding: FragmentApplicationBinding? = null
    private val binding get() = _binding!!
    private val adapter = ApplicationStatusAdapter()
    var applyArray: ArrayList<Content> = ArrayList<Content>()
    val userId = PrefsLogin.loadInt(PrefsLoginConstant.USERID, 0)
    private var page = 1
    private var size = 1
    private var totalPage = 1
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
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvListApplication.layoutManager = layoutManager
        binding.swipeRefresh.setOnRefreshListener(this)
        getApplyJobStatus(false)
        binding.rvListApplication.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val visibleItemCount = layoutManager.childCount
                val pastVisibleItem = layoutManager.findFirstVisibleItemPosition()
                val total = adapter.itemCount
                if (!isLoading && page < totalPage) {
                    if (visibleItemCount + pastVisibleItem >= total) {
                        page++
                        getApplyJobStatus(false)
                    }
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })

        viewModelApplication.getApplyJobStatus(userId, page, size)
        setObserver()

        binding.ivBack.setOnClickListener {
            val intent = Intent(requireContext(), MenuActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getApplyJobStatus(isOnRefresh: Boolean) {
        isLoading = true
        if (!isOnRefresh) binding.progressBar.visibility = View.VISIBLE
    }

    private fun setObserver() {
        viewModelApplication.allListResponse().observe(viewLifecycleOwner, Observer {
            page = it.pageable.pageSize
            totalPage = it.totalPages
            if (it != null) {
                binding.rvListApplication.setHasFixedSize(true)
                binding.rvListApplication.adapter = adapter
                Log.d("listapp", "if22")
                adapter.initData(it.content as ArrayList<Content>)
                binding.tvJobList.visibility = View.GONE
                binding.tvApplyNow.visibility = View.GONE
            }
            if (page == totalPage) {
                binding.progressBar.visibility = View.GONE
            }else {
                binding.progressBar.visibility = View.VISIBLE
                isLoading = false
                binding.swipeRefresh.isRefreshing = false
                binding.tvJobList.visibility = View.VISIBLE
                binding.tvApplyNow.visibility = View.VISIBLE
            }
        })
    }

    override fun onRefresh() {
        adapter.clear()
        page = 1
        getApplyJobStatus(true)
    }
}