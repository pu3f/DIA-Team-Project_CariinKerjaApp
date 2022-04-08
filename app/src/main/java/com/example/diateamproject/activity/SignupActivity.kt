package com.example.diateamproject.activity

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.diateamproject.R
import com.example.diateamproject.databinding.ActivitySignupBinding
import com.example.diateamproject.viewmodel.RegisterViewModel

class SignupActivity : AppCompatActivity(), View.OnClickListener, View.OnFocusChangeListener {
    private lateinit var binding: ActivitySignupBinding
    private val viewModel: RegisterViewModel by lazy {
        ViewModelProviders.of(this).get(RegisterViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.etEmail.onFocusChangeListener = this
        binding.etName.onFocusChangeListener = this
        binding.etPassword.onFocusChangeListener = this
        binding.etName.addTextChangedListener(signUpTextWatcher)
        binding.etEmail.addTextChangedListener(signUpTextWatcher)
        binding.etPassword.addTextChangedListener(signUpTextWatcher)

        binding.btnSignUp.setOnClickListener {
            viewModel.postRegister(
                binding.etName.text.toString(),
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString()
            )
        }
        setObserver()

        binding.tvLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setObserver() {
        viewModel.listResponse().observe(this, Observer {
            signUpSuccess()
        })
        viewModel.getIsError().observe(this, Observer {
            signUpFailed()
        })
    }

    private fun validateName(): Boolean {
        var errorMessage: String? = null
        val value: String = binding.etName.text.toString()
        if (value.isEmpty()) {
            errorMessage = "Enter your full name"
        }

        if (errorMessage != null) {
            binding.tilName.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }

        return errorMessage == null
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

    private fun validatePassword(): Boolean {
        var errorMessage: String? = null
        val value: String = binding.etPassword.text.toString()
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
            binding.tilPassword.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }

        return errorMessage == null
    }

    override fun onClick(view: View?) {

    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if (view != null) {
            when (view.id) {
                R.id.etName -> {
                    if (hasFocus) {
                        if (binding.tilName.isErrorEnabled) {
                            binding.tilName.isErrorEnabled = false
                        }

                    } else {
                        validateName()
                    }
                }
                R.id.etEmail -> {
                    if (hasFocus) {
                        if (binding.tilEmail.isErrorEnabled) {
                            binding.tilEmail.isErrorEnabled = false
                        }

                    } else {
                        validateEmail()
                    }
                }
                R.id.etPassword -> {
                    if (hasFocus) {
                        if (binding.tilPassword.isErrorEnabled) {
                            binding.tilPassword.isErrorEnabled = false
                        }

                    } else {
                        validatePassword()
                    }
                }
            }
        }
    }

    private val signUpTextWatcher = object : TextWatcher {

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            var userName = binding.etName.text!!.trim().isNotEmpty()
            var userEmail = binding.etEmail.text!!.trim().isNotEmpty()
            var userPassword = binding.etPassword.text!!.trim().isNotEmpty()
            binding.btnSignUp.isEnabled = userName && userEmail && userPassword
        }

        override fun afterTextChanged(p0: Editable?) {
        }
    }

    private fun signUpSuccess() {
        if (validateName() && validateEmail() && validatePassword()){
        AlertDialog.Builder(this)
            .setTitle("Sign Up Success")
            .setMessage("Email verification sent. Check your inbox or spam to verify your email")
            .setPositiveButton("OK") { _, _ ->
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
            .show()
        }
    }

    private fun signUpFailed() {
        if ((validateEmail() && validateEmail() && validatePassword()) == false) {
            AlertDialog.Builder(this)
                .setTitle("Invalid Form")
                .setMessage("Enter valid data")
                .setPositiveButton("OK") { _, _ ->
                    //do nothing
                }
                .show()
        } else {
            AlertDialog.Builder(this)
                .setTitle("Sign Up Failed")
                .setMessage("User Already Exist, create new account")
                .setPositiveButton("OK") { _, _ ->
                    //do nothing
                }
                .show()
        }
    }
}
