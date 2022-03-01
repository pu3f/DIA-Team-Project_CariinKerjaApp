package com.example.diateamproject.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.diateamproject.databinding.CardJobBinding
import com.example.diateamproject.model.alljobs.AllJobsResponse
import com.example.diateamproject.model.alljobs.Data

//interface pattern to passing jobId
interface JobClickListener {
    fun onCLickItem(jobId: Int)
}

class AllJobAdapter : RecyclerView.Adapter<AllJobAdapter.ViewHolder>() {
    var list = arrayListOf<Data>()
    private var context: Context? = null
    var jobClickListener: JobClickListener? = null

    inner class ViewHolder(val binding: CardJobBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            //implement interface passing jobId
            var jobId = list!![position].jobId
            jobClickListener?.onCLickItem(jobId)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(
            CardJobBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(list[position]) {
                binding.tvJobPosition.text = jobName
                binding.tvCompanyName.text = recruiterCompany
                binding.tvLocation.text = jobAddress
                binding.tvPostDate.text = createdAt.toString()
            }
        }
    }

    override fun getItemCount() = list.size

    fun initData(lists: AllJobsResponse) {
        this.list = lists
        notifyDataSetChanged()
    }

}


