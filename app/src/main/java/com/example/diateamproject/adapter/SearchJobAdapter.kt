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
import com.example.diateamproject.model.searchjob.Jobs
import com.example.diateamproject.util.Path

class SearchJobAdapter : RecyclerView.Adapter<SearchJobAdapter.ViewHolder>() {
    var listJob = arrayListOf<Jobs>()
    private var context: Context? = null
    var onSelectedItemListener : OnItemClickListener? = null

    inner class ViewHolder(val binding: CardJobBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener{
        init {
            binding.root.setOnClickListener{
                onSelectedItemListener?.onItemClick(it, layoutPosition)
            }
        }

        override fun onClick(p0: View?) {}
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchJobAdapter.ViewHolder {
        context = parent.context
        return  ViewHolder(
            CardJobBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: SearchJobAdapter.ViewHolder, position: Int) {
        with(holder) {
            with(listJob[position]) {
                binding.tvJobPosition.text = jobName
                binding.tvCompanyName.text = recruiterCompany
                binding.tvLocation.text = jobAddress
                val date = createdAt.substringBefore(" ")
                binding.tvPostDate.text = date

                Glide.with(context!!)
                    .load(Path.IMAGE_URL + recruiterImage)
                    .placeholder(R.drawable.ic_placeholder_list)
                    .into(binding.ivJob)
            }
        }
    }

    override fun getItemCount() = listJob.size

    fun setOnClickItemListener(OnClickItemListener: OnItemClickListener) {
        this.onSelectedItemListener = OnClickItemListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun initData(jobLists: ArrayList<Jobs>) {
        this.listJob = jobLists
        notifyDataSetChanged()
    }

    fun clear(){
        listJob.clear()
        notifyDataSetChanged()
    }
}