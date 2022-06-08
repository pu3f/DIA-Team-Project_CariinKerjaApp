package com.example.diateamproject.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.diateamproject.databinding.ChipSkillBinding
import com.example.diateamproject.listener.OnItemClickListener
import com.example.diateamproject.model.allskills.Data
import com.example.diateamproject.model.updateprofile.SkillData
import com.example.diateamproject.util.Testing

class SkillAdapter(var temp: ArrayList<String>, testing: Testing) :
    RecyclerView.Adapter<SkillAdapter.ViewHolder>() {
    private var context: Context? = null
    var skillList = arrayListOf<Data>()
    var updateSkillList = arrayListOf<SkillData>()
    var onSelectedItemListener: OnItemClickListener? = null
    var listener = testing

    inner class ViewHolder(val binding: ChipSkillBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.chipList.setOnClickListener {
                onSelectedItemListener?.onItemClick(it, layoutPosition)
            }
        }

        override fun onClick(p0: View?) {}
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkillAdapter.ViewHolder {
        context = parent.context
        return ViewHolder(
            ChipSkillBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SkillAdapter.ViewHolder, position: Int) {
        with(holder) {
            with(skillList[position]) {
                Log.d("tempSkill", "temp = $temp")
                for (i in temp.indices) {
                    //temp.get(i) condition
                    Log.d("testingtrytemp","$skillId,${temp.get(i)}")
                        //note
                        //ubah kondisi jadi temp.id = skillId
                    if (temp[i].toInt() == skillId) {
                        Log.d("testingtrytemp","true")
                        binding.chipList.isChecked = true
                        binding.chipList.isSelected = true
                    }
                }
                //note
                //pake list skill
                binding.chipList.text = skillName
                binding.chipList.setOnClickListener {
                    Log.d("tempSkill1", "temp = $temp")
                    //onclick isSelected condition
                    if (binding.chipList.isSelected) {
                        listener.removeData(skillId.toString())
                        Log.d("removeData", "data = $skillName")
                        binding.chipList.isChecked = false
                        binding.chipList.isSelected= false
                        //onclick not isSelected condition
                    } else if (!binding.chipList.isSelected) {
                        binding.chipList.isChecked = true
                        binding.chipList.isSelected= true
                        listener.addData(skillId.toString())
                        Log.d("addData", "data = $skillName")
                    }
                }
            }
        }
    }

    override fun getItemCount() = skillList.size

    fun initData(skillLists: ArrayList<Data>) {
        this.skillList = skillLists
        notifyDataSetChanged()
    }

    fun setOnClickItemListener(OnClickItemListener: OnItemClickListener) {
        this.onSelectedItemListener = OnClickItemListener
    }


}