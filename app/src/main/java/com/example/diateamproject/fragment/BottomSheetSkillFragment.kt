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
import com.example.diateamproject.model.skills.Data
import com.example.diateamproject.util.PrefsLogin
import com.example.diateamproject.util.PrefsLoginConstant
import com.example.diateamproject.util.Skill
import com.example.diateamproject.util.Testing
import com.example.diateamproject.viewmodel.SkillViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import okhttp3.internal.notify

class BottomSheetSkillFragment(skill: Skill) : BottomSheetDialogFragment(), Testing {
    var skillListener: Skill = skill
    private var _binding: FragmentBottomsheetSkillBinding? = null
    private val binding get() = _binding!!
    var NUM_COLUMNS = 2
    var temp: ArrayList<String> = ArrayList<String>()
    lateinit var adapter: SkillAdapter
    var arraySkill: ArrayList<Data> = ArrayList<Data>()
    var skill: String = ""

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
        (dialog as? BottomSheetDialog)?.behavior?.halfExpandedRatio
        val layoutManager =
            StaggeredGridLayoutManager(NUM_COLUMNS, StaggeredGridLayoutManager.VERTICAL)
        binding.rvSkill.layoutManager = layoutManager
        viewModelSkill.getSkill()

        val tempSkill = arguments?.getSerializable("tes")
        Log.d("tempSkill1", "xx = $tempSkill")
        if (tempSkill != null) {
            temp.addAll(tempSkill as ArrayList<String>)
            Log.d("tempSkill1", "newxx = $temp")
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
                        skill = skill + temp[0].toString()
                    } else {
                        skill = skill + ";" + temp[i].toString()
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
            Log.d("listSkills", "xx11 = "+temp.toString())
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
        skillListener.setData(temp as ArrayList<Data>)
    }
}