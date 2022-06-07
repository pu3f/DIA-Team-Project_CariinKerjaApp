package com.example.diateamproject.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.diateamproject.adapter.SkillAdapter
import com.example.diateamproject.databinding.FragmentBottomsheetSkillBinding
import com.example.diateamproject.model.allskills.Data
import com.example.diateamproject.model.updateprofile.SkillData
import com.example.diateamproject.util.PrefsLogin
import com.example.diateamproject.util.PrefsLoginConstant
import com.example.diateamproject.util.Skill
import com.example.diateamproject.util.Testing
import com.example.diateamproject.viewmodel.SkillViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.xiaofeng.flowlayoutmanager.Alignment
import com.xiaofeng.flowlayoutmanager.FlowLayoutManager


class BottomSheetSkillFragment(skill: Skill) : BottomSheetDialogFragment(), Testing {
    var skillListener: Skill = skill
    private var _binding: FragmentBottomsheetSkillBinding? = null
    private val binding get() = _binding!!
    var NUM_COLUMNS = 2
    var temp: ArrayList<String> = ArrayList<String>()
    lateinit var adapter: SkillAdapter
    var arraySkill: ArrayList<Data> = ArrayList<Data>()
    var listSkill: ArrayList<SkillData> = ArrayList<SkillData>()
    var skill: String = ""
    var flowLayoutManager = FlowLayoutManager()

    private val viewModelSkill: SkillViewModel by lazy {
        ViewModelProviders.of(this).get(SkillViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBottomsheetSkillBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelSkill.getSkill()
        (dialog as? BottomSheetDialog)?.behavior?.halfExpandedRatio
        flowLayoutManager.isAutoMeasureEnabled = true
        binding.rvSkill.layoutManager = flowLayoutManager.setAlignment(Alignment.LEFT)

        val tempSkill = arguments?.getSerializable("tes")
        Log.d("tempSkill1", "xx = $tempSkill")

        if (tempSkill != null) {
            //note
            //add array list skill
            listSkill.addAll(tempSkill as ArrayList<SkillData>)
            Log.d("tempSkill1", "newxx = $temp")
            for (i in listSkill.indices) {
                temp.add(listSkill[i].skillId.toString())
                adapter = SkillAdapter(temp, this)
                Log.d("tempSkill33", "newxx = $temp")
                binding.rvSkill.adapter = adapter
                adapter.notifyDataSetChanged()
            }
        }

        binding.ivHideSheet.setOnClickListener {
            dismiss()
        }

        binding.btnChoose.setOnClickListener {
            if (temp.size < 3) {
                Log.d("listSize", "size = ${temp.size}")
                Toast.makeText(activity, "minimal pilih 3 skill", Toast.LENGTH_SHORT).show()
            }
            // add new array to skill condition
            else if (arraySkill != temp) {
                for (i in temp.indices) {
                    if (i == 0) {
                        skill = skill + temp[0]
                    } else {
                        skill = skill + "," + temp[i]
                    }
                }
                Log.d("newSkill", "skillList = $skill")
                PrefsLogin.saveString(PrefsLoginConstant.SKILL, skill)
                dismiss()
            }
        }
        setObserver()
    }

    private fun setObserver() {
        viewModelSkill.responseSkill().observe(viewLifecycleOwner, Observer {
            adapter = SkillAdapter(temp, this)
            Log.d("listSkills", "xx11 = " + it.data as ArrayList<Data>)
            binding.rvSkill.adapter = adapter
            arraySkill = it.data as ArrayList<Data>
            adapter.initData(arraySkill)
        })
    }

    override fun addData(list: String) {
        if (!temp.contains(list)) {
            temp.add(list)
            Log.d("addNoList", "skillList = $temp")
        }
        Log.d("listAdded", "skillList = $temp")
        adapter = SkillAdapter(temp, this)
        binding.rvSkill.adapter = adapter
        adapter.initData(arraySkill)
        Log.d("addHaveList", "skillList = $arraySkill")
        adapter.notifyDataSetChanged()
    }

    override fun removeData(list: String) {
        for (i in temp.indices) {
            if (temp.contains(list)) {
                Log.d("removeHaveList", "skillList = $temp")
                temp.remove(list)
            }
            Log.d("listRemoved", "skillList = $temp")
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        skillListener.setData(temp)
        Log.d("lasss", "skillList = $temp")
    }
}