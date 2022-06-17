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
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.diateamproject.R
import com.example.diateamproject.activity.JobDetailsActivity
import com.example.diateamproject.activity.LoginActivity
import com.example.diateamproject.activity.RecentJobActivity
import com.example.diateamproject.activity.SearchActivity
import com.example.diateamproject.adapter.BannerAdapter
import com.example.diateamproject.adapter.RecentJobAdapter
import com.example.diateamproject.databinding.FragmentHomeBinding
import com.example.diateamproject.listener.OnItemClickListener
import com.example.diateamproject.util.BannerItem
import com.example.diateamproject.util.PrefsLogin
import com.example.diateamproject.util.PrefsLoginConstant
import com.example.diateamproject.viewmodel.RecentJobViewModel
import com.pixplicity.easyprefs.library.Prefs
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs

class HomeFragment : Fragment(){
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val adapter = RecentJobAdapter()
    private var isLoading = false
    private lateinit var viewPager: ViewPager2
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

        //check is login condition
        val isLogin = Prefs.getBoolean(PrefsLoginConstant.IS_LOGIN, false)
        if (isLogin) {
            binding.cvGuest.cvHomeGuest.visibility = View.GONE
        } else {
            binding.cvGuest.cvHomeGuest.visibility = View.VISIBLE
            binding.tvSearchTitle.setText("Login to find your dream job!")
        }

        binding.cvGuest.btnGetStarted.setOnClickListener {
            startActivity(Intent(requireContext(), LoginActivity::class.java))
        }

        //get jobseekerName from prefsLoginConstant
        // of username = "" , default value = guest
        val username = PrefsLogin.loadString(PrefsLoginConstant.USERNAME, "Guest")
        binding.tvHello.text = "Hello, $username!".uppercase(Locale.getDefault())
        Log.d("data", "not null")

        binding.btnSearchHome.setOnClickListener {
            startActivity(Intent(requireContext(), SearchActivity::class.java))
        }

        viewPager = binding.vpBanner
        val bannerItems: MutableList<BannerItem> = ArrayList()
        bannerItems.add(BannerItem(R.drawable.ic_banner_postjob))
        bannerItems.add(BannerItem(R.drawable.ic_banner_digicource))
        bannerItems.add(BannerItem(R.drawable.ic_banner_updateprofile))
        viewPager.adapter = BannerAdapter(bannerItems, viewPager)
        viewPager.clipToPadding = false
        viewPager.clipChildren = false
        viewPager.offscreenPageLimit = 3
        viewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(10))
        compositePageTransformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.15f
        }

        viewPager.setPageTransformer(compositePageTransformer)

        binding.tvShowMore.setOnClickListener {
            val intent = Intent(requireContext(), RecentJobActivity::class.java)
            startActivity(intent)
        }
    }

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
