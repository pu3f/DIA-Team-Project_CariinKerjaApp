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
import com.example.diateamproject.model.applicationstatus.ApplicationStatusResponse
import com.example.diateamproject.model.applicationstatus.Data
import java.text.SimpleDateFormat
import java.util.*

class ApplicationStatusAdapter : RecyclerView.Adapter<ApplicationStatusAdapter.ViewHolder>() {

    var applicationList = arrayListOf<Data>()
    private var context: Context? = null
    private val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH)

    inner class ViewHolder(val binding: CardApplicationBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {}

        override fun onClick(p0: View?) {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(
            CardApplicationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(applicationList[position]) {
                binding.tvJobPosition.text = jobName
                binding.tvCompanyName.text = recruiterCompany
                binding.tvLocation.text = recruiterAddress
                binding.tvStatus.text = applicationStatus.capitalize()
                if (applicationStatus.equals("accepted")) {
                    binding.tvStatus.setTextColor(ContextCompat.getColor(context!!,R.color.green))
                }else if (applicationStatus.equals("rejected")) {
                binding.tvStatus.setTextColor(ContextCompat.getColor(context!!,android.R.color.holo_red_dark))
            }
                val dateString= simpleDateFormat.format(createdAt)
                binding.tvPostDate.text = String.format("%s", dateString)
                Glide.with(context!!)
                    .load("http://54.255.4.75:9091/resources/$recruiterImage")
                    .into(binding.ivJob)
            }
        }
    }

    override fun getItemCount() = applicationList.size

    @SuppressLint("NotifyDataSetChanged")
    fun initData(lists: ApplicationStatusResponse){
        this.applicationList = lists
        notifyDataSetChanged()
    }

}