package com.example.diateamproject.activity

import android.Manifest
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.diateamproject.R
import com.example.diateamproject.databinding.ActivityMenuBinding
import com.example.diateamproject.fragment.*

class MenuActivity : AppCompatActivity() {
    lateinit var binding: ActivityMenuBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val fragmentt: Fragment = HomeFragment()
        val transactionn: FragmentTransaction = supportFragmentManager.beginTransaction()
        transactionn.replace(R.id.content, fragmentt).commit()
        binding.navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menuOne -> {
                    val fragment: Fragment = HomeFragment()
                    val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.content, fragment).commit()
                    true
                }
                R.id.menuTwo -> {
                    val fragment: Fragment = ApplicationFragment()
                    val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.content, fragment).commit()
                    true
                }
                R.id.menuThree -> {
                    val fragment: Fragment = ProfileFragment()
                    val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.content, fragment).commit()
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun requestPermission() {

        val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        this.requestPermissions(permissions, 5)
    }
}