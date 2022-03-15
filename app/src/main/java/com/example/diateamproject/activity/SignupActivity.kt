package com.example.diateamproject.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.diateamproject.databinding.ActivitySignupBinding
import com.example.diateamproject.viewmodel.RegisterViewModel

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private val viewModel: RegisterViewModel by lazy {
        ViewModelProviders.of(this).get(RegisterViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

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
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            Toast.makeText(this, "Signup Success", Toast.LENGTH_LONG).show()
        })

        viewModel.getIsError().observe(this, Observer {
            if (it) {
                Toast.makeText(this, "User Already Exist", Toast.LENGTH_LONG).show()
            }
        })
    }
}