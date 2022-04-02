package com.example.diateamproject.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.diateamproject.R
import com.example.diateamproject.databinding.ActivityLoginBinding
import com.example.diateamproject.fragment.HomeFragment
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
            if (binding.etEmail.text!!.isEmpty() && binding.etPassword.text!!.isEmpty()) {
                Toast.makeText(this, "Complete the Form", Toast.LENGTH_LONG).show()
            }
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

    }

    private fun setObserver() {
        viewModel.listResponse().observe(this, Observer {
            //move to other activity
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)

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
            Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show()
        })
    }

}