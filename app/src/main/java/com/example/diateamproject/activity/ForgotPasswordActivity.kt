package com.example.diateamproject.activity

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.diateamproject.R
import com.example.diateamproject.databinding.ActivityForgotPasswordBinding
import com.example.diateamproject.fragment.CheckEmailDialogFragment
import com.example.diateamproject.util.PrefsForgotPassword
import com.example.diateamproject.util.PrefsForgotPasswordConstant
import com.example.diateamproject.util.PrefsLogin
import com.example.diateamproject.viewmodel.EmailResetPasswordViewModel
import com.example.diateamproject.viewmodel.LoginViewModel

class ForgotPasswordActivity : AppCompatActivity(), View.OnFocusChangeListener {
    private lateinit var binding: ActivityForgotPasswordBinding
    private val viewModel: EmailResetPasswordViewModel by lazy {
        ViewModelProviders.of(this).get(EmailResetPasswordViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.etEmail.onFocusChangeListener = this
        binding.btnSend.setOnClickListener {
            viewModel.postEmailResetPassword(
                binding.etEmail.text.toString()
            )
        }
        setObserver()

        binding.btnBack.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun setObserver() {
        viewModel.listResponse().observe(this, Observer {
            if (validateEmail()) {
                checkEmail()
                val userEmail = it.data
                PrefsForgotPassword.saveString(PrefsForgotPasswordConstant.USEREMAIL, userEmail)
            } else {
                Toast.makeText(this, "must fill the form", Toast.LENGTH_LONG).show()
            }
        })

        viewModel.getIsError().observe(this, Observer {
            Toast.makeText(this, "email doesn't exist", Toast.LENGTH_LONG).show()
        })
    }

    private fun validateEmail(): Boolean {
        var errorMessage: String? = null
        val value: String = binding.etEmail.text.toString()
        if (value.isEmpty()) {
            errorMessage = "Enter your email"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            errorMessage = "Invalid email address"
        }

        if (errorMessage != null) {
            binding.tilEmail.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }
        return errorMessage == null
    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if (view != null) {
            when (view.id) {
                R.id.etEmail -> {
                    if (hasFocus) {
                        if (binding.tilEmail.isErrorEnabled) {
                            binding.tilEmail.isErrorEnabled = false
                        }
                    } else {
                        validateEmail()
                    }
                }
            }
        }
    }

    private fun checkEmail() {
        val dialog = CheckEmailDialogFragment()
        dialog.show(supportFragmentManager, "checkEmailDialog")
    }
}

