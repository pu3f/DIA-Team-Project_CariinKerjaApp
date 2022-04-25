package com.example.diateamproject.fragment

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.diateamproject.R
import com.example.diateamproject.activity.LoginActivity
import com.example.diateamproject.activity.ResetPasswordActivity
import com.example.diateamproject.databinding.FragmentCheckEmailDialogBinding

class CheckEmailDialogFragment : DialogFragment() {
    private var _binding: FragmentCheckEmailDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCheckEmailDialogBinding.inflate(inflater, container, false)
        val view = binding.root
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.setCancelable(false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnOk.setOnClickListener {
            startActivity(Intent(activity, LoginActivity::class.java))
        }

        binding.tvTryAnotherAccount.setOnClickListener {
            dismiss()
        }
    }

}