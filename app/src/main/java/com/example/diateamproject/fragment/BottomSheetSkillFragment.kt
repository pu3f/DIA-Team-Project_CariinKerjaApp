package com.example.diateamproject.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.diateamproject.adapter.SkillAdapter
import com.example.diateamproject.databinding.FragmentBottomsheetSkillBinding
import com.example.diateamproject.listener.OnItemClickListener
import com.example.diateamproject.model.skills.Data
import com.example.diateamproject.util.PrefsLogin
import com.example.diateamproject.util.PrefsLoginConstant
import com.example.diateamproject.util.Skill
import com.example.diateamproject.util.Testing
import com.example.diateamproject.viewmodel.SkillViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetSkillFragment(skill: Skill) : BottomSheetDialogFragment(), Testing {
    var skillListener: Skill = skill
    private var _binding: FragmentBottomsheetSkillBinding? = null
    private val binding get() = _binding!!
    var NUM_COLUMNS = 2
    var list_of_items = arrayOf("")
    var arrayValue: ArrayList<String> = ArrayList<String>()
    var temp: ArrayList<String> = ArrayList<String>()
    lateinit var adapter: SkillAdapter
    var arraySkill: ArrayList<Data> = ArrayList<Data>()
    var onSelected: (() -> Unit)? = null
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
            temp = tempSkill as ArrayList<String>
        }

        binding.btnChoose.setOnClickListener {
            if (temp.size < 3) {
                Log.d("listSize", "size = ${temp.size}")
                Toast.makeText(activity, "minimal pilih 3 skill", Toast.LENGTH_SHORT).show()
            } else {
                for (i in temp.indices) {
                    if (i == 0) {
                        skill = skill + temp[0].toString()
                    } else {
                        skill = skill + ";" + temp[i].toString()
                    }
                }
                Log.d("listSize", "size = $skill")
                PrefsLogin.saveString(PrefsLoginConstant.SKILL, skill)
                dismiss()
            }
        }
        setObserver()
//        selectedSkill()
    }

    private fun setObserver() {
        viewModelSkill.responseSkill().observe(viewLifecycleOwner, Observer {
            adapter = SkillAdapter(temp, this)
            binding.rvSkill.adapter = adapter
            Log.d("listSkill", "xx11")
            arraySkill = it.data as ArrayList<Data>
            adapter.initData(arraySkill)
        })
    }

//    private fun selectedSkill() {
//        adapter.setOnClickItemListener(object : OnItemClickListener {
//            override fun onItemClick(item: View, position: Int) {
//                val skillId = adapter.getItemId(position)
//                Toast.makeText(activity, "$skillId", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }

    override fun addData(list: String) {
        Log.d("noList", "skillList = $list")
        if (!temp.contains(list)) {
            temp.add(list)
            Log.d("addNoList", "skillList = $temp")
        }
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
//            adapter = SkillAdapter(temp, this)
//            binding.rvSkill.adapter = adapter
//            adapter.initData(arraySkill)
//            Log.d("removeNoList", "skillList = $arraySkill")
//            adapter.notifyDataSetChanged()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        skillListener.setData(temp as ArrayList<Data>)
    }
}