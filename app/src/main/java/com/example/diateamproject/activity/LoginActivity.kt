package com.example.diateamproject.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.diateamproject.databinding.ActivityLoginBinding
import com.example.diateamproject.model.requestlogin.RequestLogin
import com.example.diateamproject.viewmodel.LoginViewModel


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
                RequestLogin(
                    binding.etEmail.text.toString(),
                    binding.etPassword.text.toString()
                )
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
            val intent = Intent(this, MenuActivity::class.java)
            intent.putExtra("id", it.data.userId.toString())
            intent.putExtra("name", it.data.userName.toString())
            intent.putExtra("email", it.data.userEmail.toString())
            startActivity(intent)
        })
        viewModel.getIsError().observe(this, Observer {
            if (it) {
                Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
            }
        })
    }

}