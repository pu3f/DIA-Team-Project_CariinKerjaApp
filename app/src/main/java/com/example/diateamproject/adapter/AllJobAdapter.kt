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
import com.example.diateamproject.model.allpostingjobs.Content
import com.example.diateamproject.util.Path
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AllJobAdapter : RecyclerView.Adapter<AllJobAdapter.ViewHolder>() {
    var allJobList = arrayListOf<Content>()
    private var context: Context? = null
    var onSelectedItemListener : OnItemClickListener? = null

    inner class ViewHolder(val binding: CardJobBinding) :
    RecyclerView.ViewHolder(binding.root), View.OnClickListener{
        init {
            binding.root.setOnClickListener{
                onSelectedItemListener?.onItemClick(it, layoutPosition)
//                onItemClickListener?.let { it(allJobList[adapterPosition].jobId) }
            }
        }

        override fun onClick(p0: View?) {}
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllJobAdapter.ViewHolder {
        context = parent.context
        return  ViewHolder(
            CardJobBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    @SuppressLint("NewApi")
    override fun onBindViewHolder(holder: AllJobAdapter.ViewHolder, position: Int) {
        with(holder) {
            with(allJobList[position]) {
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

    override fun getItemCount() = allJobList.size

//    private var onItemClickListener: ((Int) -> Unit)? = null
//
//    fun setOnItemClickListener(listener: (Int) -> Unit) {
//        onItemClickListener = listener
//    }

    fun setOnClickItemListener(OnClickItemListener: OnItemClickListener) {
        this.onSelectedItemListener = OnClickItemListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun initData(jobLists: ArrayList<Content>) {
        this.allJobList = jobLists
        notifyDataSetChanged()
    }

    fun clear(){
        allJobList.clear()
        notifyDataSetChanged()
    }


}