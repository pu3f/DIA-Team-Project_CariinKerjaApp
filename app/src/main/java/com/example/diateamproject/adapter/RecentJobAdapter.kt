package com.example.diateamproject.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.diateamproject.R
import com.example.diateamproject.databinding.CardJobBinding
import com.example.diateamproject.listener.OnItemClickListener
import com.example.diateamproject.model.recentpostingjobs.RecentJobsResponse
import com.example.diateamproject.model.recentpostingjobs.Jobs
import com.example.diateamproject.util.Path
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RecentJobAdapter : RecyclerView.Adapter<RecentJobAdapter.ViewHolder>() {
    var recentJobList = arrayListOf<Jobs>()
    private var context: Context? = null
    var onSelectedItemListener : OnItemClickListener? = null

    inner class ViewHolder(val binding: CardJobBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.root.setOnClickListener {
                onSelectedItemListener?.onItemClick(it, layoutPosition)
            }
        }

        override fun onClick(p0: View?) {}
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(
            CardJobBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    @SuppressLint("NewApi")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(recentJobList[position]) {
                binding.tvJobPosition.text = jobName
                binding.tvCompanyName.text = recruiterCompany
                binding.tvLocation.text = jobAddress
                val formatter: DateTimeFormatter =
                    DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
                val dateTime: LocalDateTime = LocalDateTime.parse(createdAt, formatter)
                val formatter2: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
                binding.tvPostDate.text = dateTime.format(formatter2)

                Glide.with(context!!)
                    .load(Path.IMAGE_URL + recruiterImage)
                    .placeholder(R.drawable.ic_placeholder_list)
                    .into(binding.ivJob)
            }
        }
    }

    override fun getItemCount() = recentJobList.size

    @SuppressLint("NotifyDataSetChanged")
    fun initData(lists: RecentJobsResponse) {
        this.recentJobList = lists
        notifyDataSetChanged()
    }

    fun setOnClickItemListener(OnClickItemListener:OnItemClickListener) {
        this.onSelectedItemListener = OnClickItemListener
    }

    fun clear(){
        recentJobList.clear()
        notifyDataSetChanged()
    }
}


