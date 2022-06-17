package com.example.diateamproject.activity

import android.Manifest
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.diateamproject.R
import com.example.diateamproject.databinding.ActivityMenuBinding
import com.example.diateamproject.fragment.*
import com.example.diateamproject.util.PrefsLoginConstant
import com.pixplicity.easyprefs.library.Prefs

class MenuActivity : AppCompatActivity() {
    lateinit var binding: ActivityMenuBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val fragmentt: Fragment = HomeFragment()
        val transactionn: FragmentTransaction = supportFragmentManager.beginTransaction()
        val isLogin = Prefs.getBoolean(PrefsLoginConstant.IS_LOGIN, false)
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
                    //get isLogin conditions
                    if (isLogin) {
                        val fragment: Fragment = ApplicationFragment()
                        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                        transaction.replace(R.id.content, fragment).commit()
                    } else {
                        //for guest
                        startActivity(Intent(this, LoginActivity::class.java))
                    }
                    true
                }
                R.id.menuThree -> {
                    //get isLogin conditions
                    if (isLogin) {
                        val fragment: Fragment = ProfileFragment()
                        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                        transaction.replace(R.id.content, fragment).commit()
                    } else {
                        //for guest
                        startActivity(Intent(this, LoginActivity::class.java))
                    }
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    override fun onBackPressed() {
        finishAffinity()
    }

    fun show() {
        binding.navigation.visibility = View.VISIBLE
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun requestPermission() {
        val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        this.requestPermissions(permissions, 5)
    }
}