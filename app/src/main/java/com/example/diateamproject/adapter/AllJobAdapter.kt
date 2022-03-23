package com.example.diateamproject.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.diateamproject.R
import com.example.diateamproject.databinding.CardJobBinding
import com.example.diateamproject.listener.OnItemClickListener
import com.example.diateamproject.model.alljobs.AllJobsResponse
import com.example.diateamproject.model.alljobs.Data
import com.example.diateamproject.util.Path
import java.text.SimpleDateFormat
import kotlin.time.toDuration


class AllJobAdapter : RecyclerView.Adapter<AllJobAdapter.ViewHolder>() {
    var list = arrayListOf<Data>()
    private var context: Context? = null
    var onSelectedItemListener : OnItemClickListener? = null

    @SuppressLint("SimpleDateFormat")
    val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy")

    inner class ViewHolder(val binding: CardJobBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.root.setOnClickListener {
                onSelectedItemListener?.onItemClick(it, layoutPosition)
            }
        }

        override fun onClick(p0: View?) {

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
                val dateString= simpleDateFormat.format(createdAt)
                binding.tvPostDate.text = String.format("%s", dateString)
                Glide.with(context!!)
                    .load(Path.IMAGE_URL + recruiterImage)
                    .placeholder(R.drawable.ic_placeholder_list)
                    .into(binding.ivJob)

            }
        }
    }

    override fun getItemCount() = list.size

    fun initData(lists: AllJobsResponse) {
        this.list = lists
        notifyDataSetChanged()
    }

    fun setOnClickItemListener(OnClickItemListener:OnItemClickListener) {
        this.onSelectedItemListener = OnClickItemListener
    }

}


