package com.example.diateamproject.activity

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.diateamproject.R
import com.example.diateamproject.databinding.ActivityResetPasswordBinding
import com.example.diateamproject.fragment.SuccessfullDialogFragment
import com.example.diateamproject.util.PrefsForgotPasswordConstant
import com.example.diateamproject.util.PrefsLoginConstant
import com.example.diateamproject.viewmodel.ResetPasswordViewModel
import com.pixplicity.easyprefs.library.Prefs

class ResetPasswordActivity : AppCompatActivity(), View.OnFocusChangeListener {
    private lateinit var binding: ActivityResetPasswordBinding
    private var email = Prefs.getString(PrefsForgotPasswordConstant.USEREMAIL, "")
    private val viewModel: ResetPasswordViewModel by lazy {
        ViewModelProviders.of(this).get(ResetPasswordViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //token for sending data to other application
        val tokenUris: String = intent.data?.getQueryParameter("token").toString()
        Toast.makeText(this, "is token $tokenUris", Toast.LENGTH_SHORT).show()

        binding.etNewPassword.onFocusChangeListener = this
        binding.etConfirmPassword.onFocusChangeListener = this

        binding.btnSubmit.setOnClickListener {
            viewModel.postResetPassword(
                email,
                binding.etNewPassword.text.toString(),
                binding.etConfirmPassword.text.toString()
            )
        }
        setObserver()
    }

    private fun setObserver() {
        viewModel.listResetPasswordResponse().observe(this, Observer {
            if (validatePasswordMatch()) {
                val dialog = SuccessfullDialogFragment()
                dialog.show(supportFragmentManager, "successfullDialog")
            } else {
                Toast.makeText(this, "Password didn't match", Toast.LENGTH_LONG).show()
            }
        })

        viewModel.getIsError().observe(this, Observer {
            Toast.makeText(this, "User not found", Toast.LENGTH_LONG).show()
        })
    }

    private fun validateNewPassword(): Boolean {
        var errorMessage: String? = null
        val value: String = binding.etNewPassword.text.toString()
        if (value.isEmpty()) {
            errorMessage = "Enter your password"
        } else if (value.length != 8) {
            errorMessage = "Password must contain 8 character"
        } else if (!value.matches(".*[A-Z].*".toRegex())) {
            errorMessage = "Password must contain 1 upper-case character"
        } else if (!value.matches(".*[a-z].*".toRegex())) {
            errorMessage = "Password must contain 1 lower-case character"
        } else if (!value.matches(".*[0-9].*".toRegex())) {
            errorMessage = "Password must contain 1 number character"
        } else if (!value.matches(".*[@#\$%^&+=!].*".toRegex())) {
            errorMessage = "Password must contain 1 symbol"
        }

        if (errorMessage != null) {
            binding.tilNewPassword.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }
        return errorMessage == null
    }

    private fun validateConfirmPassword(): Boolean {
        var errorMessage: String? = null
        val value: String = binding.etConfirmPassword.text.toString()
        if (value.isEmpty()) {
            errorMessage = "Enter your password"
        } else if (value.length != 8) {
            errorMessage = "Password must contain 8 character"
        } else if (!value.matches(".*[A-Z].*".toRegex())) {
            errorMessage = "Password must contain 1 upper-case character"
        } else if (!value.matches(".*[a-z].*".toRegex())) {
            errorMessage = "Password must contain 1 lower-case character"
        } else if (!value.matches(".*[0-9].*".toRegex())) {
            errorMessage = "Password must contain 1 number character"
        } else if (!value.matches(".*[@#\$%^&+=!].*".toRegex())) {
            errorMessage = "Password must contain 1 symbol"
        }

        if (errorMessage != null) {
            binding.tilConfirmPassword.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }
        return errorMessage == null
    }

    private fun validatePasswordMatch(): Boolean {
        var errorMessage: String? = null
        val password = binding.etNewPassword.text.toString()
        val confirmPassword = binding.etConfirmPassword.text.toString()
        if (password != confirmPassword) {
            errorMessage = "Confirm new password doesn't match with New password"
        }

        if (errorMessage != null) {
            binding.tilConfirmPassword.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }
        return errorMessage == null
    }

    @SuppressLint("NewApi", "ResourceType")
    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if (view != null) {
            when (view.id) {
                R.id.etNewPassword -> {
                    if (hasFocus) {
                        if (binding.tilNewPassword.isErrorEnabled) {
                            binding.tilNewPassword.isErrorEnabled = false
                        }

                    } else {
                        if (validateNewPassword() && binding.etConfirmPassword.text!!.isNotEmpty() && validateConfirmPassword() && validatePasswordMatch()) {
                         if (binding.tilConfirmPassword.isErrorEnabled){
                             binding.tilConfirmPassword.isErrorEnabled = false
                         }
                            binding.tilConfirmPassword.apply {
                                setStartIconDrawable(R.drawable.ic_check_circle)
                                setStartIconTintList(
                                    ColorStateList.valueOf(Color.parseColor("#27AE60"))
                                )
                            }
                        }
                    }
                }
                R.id.etConfirmPassword -> {
                    if (hasFocus) {
                        if (binding.tilConfirmPassword.isErrorEnabled) {
                            binding.tilConfirmPassword.isErrorEnabled = false
                        }

                    } else {
                        if (validateNewPassword() && validateConfirmPassword() && validatePasswordMatch()) {
                            if (binding.tilConfirmPassword.isErrorEnabled) {
                                binding.tilConfirmPassword.isErrorEnabled = false
                            }
                            binding.tilConfirmPassword.apply {
                                setStartIconDrawable(R.drawable.ic_check_circle)
                                setStartIconTintList(
                                    ColorStateList.valueOf(Color.parseColor("#27AE60"))
                                )
                            }
                        } else {
                            if (binding.tilConfirmPassword.startIconDrawable != null)
                                binding.tilConfirmPassword.startIconDrawable = null
                        }
                    }
                }
            }
        }
    }
}