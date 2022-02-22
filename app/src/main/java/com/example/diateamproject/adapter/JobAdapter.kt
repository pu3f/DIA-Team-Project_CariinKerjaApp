package com.example.diateamproject.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.diateamproject.databinding.ItemJobBinding
import com.example.diateamproject.model.getalljob.Data

class JobAdapter : RecyclerView.Adapter<JobAdapter.ViewHolder>() {
    var list = arrayListOf<Data>()
    private var context: Context? = null

    inner class ViewHolder(val binding: ItemJobBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
//            binding.root.setOnClickListener {
//                onItemClickListener?.let { it(da[adapterPosition].eventId) }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return  ViewHolder(
            ItemJobBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(list[position]) {
                var imageArray: ArrayList<String> = ArrayList<String>()
                binding.tvJobName.text = jobName
//                binding.tvJobSalary.text = jobSalary.toString()
            }
        }
    }

    override fun getItemCount() = list.size

    private var onItemClickListener: ((Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    fun initData(lists: ArrayList<Data>) {
        this.list = lists
        notifyDataSetChanged()
    }

}


