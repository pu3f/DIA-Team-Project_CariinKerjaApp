package com.example.diateamproject.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.diateamproject.databinding.ActivityMainJobBinding

class MainJobActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainJobBinding
//    private val viewModel: JobViewModel by lazy {
//        ViewModelProviders.of(this).get(JobViewModel::class.java)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainJobBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var id = intent.getStringExtra("id")
        var name = intent.getStringExtra("name")
        var email = intent.getStringExtra("email")
        binding.tvUserId.text = "userId: $id"
        binding.tvUserName.text = "userName: $name"
        binding.tvUserEmail.text = "userEmail: $email"
    }
}