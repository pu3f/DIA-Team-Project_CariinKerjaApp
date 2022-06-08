package com.example.diateamproject.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.diateamproject.R
import com.example.diateamproject.activity.JobDetailsActivity
import com.example.diateamproject.activity.RecentJobActivity
import com.example.diateamproject.activity.SearchActivity
import com.example.diateamproject.adapter.RecentJobAdapter
import com.example.diateamproject.databinding.FragmentHomeBinding
import com.example.diateamproject.listener.OnItemClickListener
import com.example.diateamproject.util.PrefsLogin
import com.example.diateamproject.util.PrefsLoginConstant
import com.example.diateamproject.viewmodel.RecentJobViewModel
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment(){

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val adapter = RecentJobAdapter()
    private var isLoading = false
    var bannerArray: ArrayList<Int> = ArrayList()
    var carouselView: CarouselView? = null
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
        return view
    }

    @SuppressLint("SetTextI18n", "DefaultLocale")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvListJob.layoutManager = layoutManager
        listRefresh()
        getRecentJob(false)
        viewModelRecent.getRecentJobs()
        setObserver()
        action()

        //get jobseekerName from prefsLoginConstant
        val username = PrefsLogin.loadString(PrefsLoginConstant.USERNAME, "userName")
        binding.tvHello.text = "Hello, $username".uppercase(Locale.getDefault())
        Log.d("data", "not null")

        binding.btnSearchHome.setOnClickListener {
            startActivity(Intent(requireContext(), SearchActivity::class.java) )
        }

        bannerArray.add(R.drawable.ic_banner_postjob)
        bannerArray.add(R.drawable.ic_banner_digicource)
        bannerArray.add(R.drawable.ic_banner_updateprofile)
        carouselView = binding.selvBanner
        carouselView!!.pageCount = bannerArray.size
        carouselView!!.setImageListener(imageListener)



        binding.tvShowMore.setOnClickListener {
            val intent = Intent(requireContext(), RecentJobActivity::class.java)
            startActivity(intent)
        }
    }

    var imageListener = ImageListener{ position, imageView -> imageView.setImageResource(bannerArray[position])  }

    private fun setObserver() {
        viewModelRecent.recentListResponse().observe(viewLifecycleOwner, Observer {
            binding.rvListJob.adapter = adapter
            Log.d("listapp", "if22")
            adapter.initData(it)
            binding.pbHome.visibility = View.GONE
            isLoading = false
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
                    it.putExtra("jobId", adapter.recentJobList[position].jobId)
                    startActivity(it)
                }
            }
        })
    }

    private fun getRecentJob(isOnRefresh: Boolean) {
        isLoading = true
        if (!isOnRefresh) {
            binding.pbHome.visibility = View.VISIBLE
        }
        viewModelRecent.getRecentJobs()
    }

    private fun listRefresh() {
        binding.swipeToRefresh.setOnRefreshListener {
            adapter.clear()
            getRecentJob(true)
            adapter.notifyDataSetChanged()
            binding.swipeToRefresh.isRefreshing = false
        }
    }
}
