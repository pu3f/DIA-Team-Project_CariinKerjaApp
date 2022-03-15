package com.example.diateamproject.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.diateamproject.databinding.CardApplicationBinding
import com.example.diateamproject.model.applicationstatus.ApplicationStatusResponse
import com.example.diateamproject.model.applicationstatus.Data
import java.text.SimpleDateFormat

class ApplicationStatusAdapter : RecyclerView.Adapter<ApplicationStatusAdapter.ViewHolder>() {

    var ApplicationList = arrayListOf<Data>()
    private var context: Context? = null

    @SuppressLint("SimpleDateFormat")
    val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy")

    inner class ViewHolder(val binding: CardApplicationBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {

        }

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
            with(ApplicationList[position]) {
                binding.tvJobPosition.text = jobName
                binding.tvCompanyName.text = recruiterCompany
                binding.tvLocation.text = recruiterAddress
                binding.tvStatus.text = applicationStatus
                val dateString= simpleDateFormat.format(createdAt)
                binding.tvPostDate.text = String.format("%s", dateString)

            }
        }
    }

    override fun getItemCount() = ApplicationList.size

    fun initData(lists: ApplicationStatusResponse){
        this.ApplicationList = lists
        notifyDataSetChanged()
    }

}