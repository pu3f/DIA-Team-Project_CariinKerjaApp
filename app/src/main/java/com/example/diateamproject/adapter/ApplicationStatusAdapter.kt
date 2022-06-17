package com.example.diateamproject.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.diateamproject.R
import com.example.diateamproject.databinding.CardApplicationBinding
import com.example.diateamproject.listener.OnItemClickListener
import com.example.diateamproject.model.applicationstatus.ApplicationStatusResponse
import com.example.diateamproject.model.applicationstatus.Data
import com.example.diateamproject.model.applyjobstatus.ApplyJobStatusResponse
import com.example.diateamproject.model.applyjobstatus.Content
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class ApplicationStatusAdapter : RecyclerView.Adapter<ApplicationStatusAdapter.ViewHolder>() {
    var applicationList = arrayListOf<Content>()
    private var context: Context? = null
    var onSelectedItemListener : OnItemClickListener? = null

    inner class ViewHolder(val binding: CardApplicationBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.cvJob.setOnClickListener {
                onSelectedItemListener?.onItemClick(it, layoutPosition)
            }
        }

        override fun onClick(p0: View?) {}
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(
            CardApplicationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    @SuppressLint("NewApi")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(applicationList[position]) {
                val formatter: DateTimeFormatter =
                    DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
                val dateTime: LocalDateTime = LocalDateTime.parse(createdAt, formatter)
                val formatter2: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
                binding.tvPostDate.text = dateTime.format(formatter2)
                binding.tvJobPosition.text = jobName
                binding.tvCompanyName.text = recruiterCompany
                binding.tvStatus.text = applicationStatus.capitalize()
                if (applicationStatus.equals("accepted")) {
                    binding.tvStatus.setTextColor(ContextCompat.getColor(context!!, R.color.green))
                } else if (applicationStatus.equals("rejected")) {
                    binding.tvStatus.setTextColor(
                        ContextCompat.getColor(
                            context!!,
                            android.R.color.holo_red_dark
                        )
                    )
                }

                Glide.with(context!!)
                    .load("http://54.255.4.75:9091/resources/$recruiterImage")
                    .placeholder(R.drawable.ic_placeholder_list)
                    .into(binding.ivJob)
            }
        }
    }

    override fun getItemCount() = applicationList.size

    @SuppressLint("NotifyDataSetChanged")
    fun initData(applyLists: ArrayList<Content>){
        this.applicationList = applyLists
        notifyDataSetChanged()
    }

    fun setOnClickItemListener(OnClickItemListener:OnItemClickListener) {
        this.onSelectedItemListener = OnClickItemListener
    }

    fun clear(){
        applicationList.clear()
        notifyDataSetChanged()
    }

}