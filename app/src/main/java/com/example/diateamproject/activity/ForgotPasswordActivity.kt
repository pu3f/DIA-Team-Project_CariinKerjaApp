package com.example.diateamproject.activity

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.diateamproject.R
import com.example.diateamproject.databinding.ActivityForgotPasswordBinding
import com.example.diateamproject.fragment.CheckEmailDialogFragment
import com.example.diateamproject.util.PrefsForgotPassword
import com.example.diateamproject.util.PrefsForgotPasswordConstant
import com.example.diateamproject.util.ProgressButtonForgotPassword
import com.example.diateamproject.viewmodel.EmailResetPasswordViewModel
import com.google.android.material.snackbar.Snackbar

class ForgotPasswordActivity : AppCompatActivity(), View.OnFocusChangeListener {
    private lateinit var binding: ActivityForgotPasswordBinding
    lateinit var pb: ProgressButtonForgotPassword
    private val viewModel: EmailResetPasswordViewModel by lazy {
        ViewModelProviders.of(this).get(EmailResetPasswordViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        pb = ProgressButtonForgotPassword(this, view)

        binding.etEmail.onFocusChangeListener = this
        binding.btnSend.cvForgotPass.setOnClickListener {
            pb.ActiveButton()
            if (validateEmail()) {
                viewModel.postEmailResetPassword(
                    binding.etEmail.text.toString()
                )
                pb.FinishButton()
            } else {
                Snackbar.make(view, "Email required", Snackbar.LENGTH_SHORT).show()
                pb.FinishButton()
            }
        }
        setObserver()

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setObserver() {
        viewModel.listResponse().observe(this, Observer {
            checkEmail()
            //put user email in prefs for api reset password
            val userEmail = it.data
            PrefsForgotPassword.saveString(PrefsForgotPasswordConstant.USEREMAIL, userEmail)
        })

        viewModel.getIsError().observe(this, Observer {
            sendEmailFailed()
            pb.FinishButton()
        })
    }

    private fun validateEmail(): Boolean {
        var errorMessage: String? = null
        val value: String = binding.etEmail.text.toString()
        if (value.isEmpty()) {
            errorMessage = "Enter your registered email address"
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

    private fun sendEmailFailed() {
        AlertDialog.Builder(this)
            .setTitle("Send Email Failed")
            .setMessage("Email doesn't exist, use your registered email address")
            .setPositiveButton("OK") { _, _ ->
                //do nothing
            }
            .show()
    }
}

