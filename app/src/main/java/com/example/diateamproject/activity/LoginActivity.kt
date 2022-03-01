package com.example.diateamproject.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        binding.cvLogin.setOnClickListener {
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
            //get response from prefs
//            val username = it.data.jobseekerName
//            PrefsLogin.saveString(PrefsLoginConstant.JOBSEEKERNAME,username)

            val intent = Intent(this, MenuActivity::class.java)
            intent.putExtra("username", it.data.jobseekerName)

            startActivity(intent)
        })
        viewModel.getIsError().observe(this, Observer {
            if (it) {
                Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
            }
        })
    }

}