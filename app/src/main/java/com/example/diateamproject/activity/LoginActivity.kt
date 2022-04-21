package com.example.diateamproject.activity

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.diateamproject.R
import com.example.diateamproject.databinding.ActivityLoginBinding
import com.example.diateamproject.util.PrefsLogin
import com.example.diateamproject.util.PrefsLoginConstant
import com.example.diateamproject.viewmodel.LoginViewModel
import com.pixplicity.easyprefs.library.Prefs


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by lazy {
        ViewModelProviders.of(this).get(LoginViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnLogin.setOnClickListener {
            viewModel.postLogin(
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString()
            )
        }
        setObserver()

        binding.tvSignup.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        binding.tvForgotPin.setOnClickListener {
            startActivity(Intent(this, ResetPasswordActivity::class.java))
            finish()
        }
    }

    private fun setObserver() {
        viewModel.listResponse().observe(this, Observer {
            //move to other activity
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
            finish()

            //put response to prefs
            val userId = it.data.jobseekerId
            val userName = it.data.jobseekerName
            val email = it.data.jobseekerEmail
            PrefsLogin.saveInt(PrefsLoginConstant.USERID, userId)
            PrefsLogin.saveString(PrefsLoginConstant.USERNAME, userName)
            PrefsLogin.saveString(PrefsLoginConstant.EMAIL, email)
            Prefs.putBoolean(PrefsLoginConstant.IS_LOGIN, true)
        })

        viewModel.getIsError().observe(this, Observer {
            logInFailed()
        })
    }

    private fun logInFailed() {
        if (binding.etEmail.text!!.isEmpty() && binding.etPassword.text!!.isEmpty()) {
            AlertDialog.Builder(this)
                .setTitle("Login Failed")
                .setMessage("Complete the Form")
                .setPositiveButton("OK") { _, _ ->
                    //do nothing
                }
                .show()
        } else {
            AlertDialog.Builder(this)
                .setTitle("Login Failed")
                .setMessage("Account not found. Email or Password wrong")
                .setPositiveButton("OK") { _, _ ->
                    //do nothing
                }
                .show()
        }
    }

}
