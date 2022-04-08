package com.example.diateamproject.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.diateamproject.databinding.ActivitySplashScreenBinding
import com.example.diateamproject.util.PrefsLoginConstant
import com.pixplicity.easyprefs.library.Prefs


@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Handler().postDelayed({
            val isLogin = Prefs.getBoolean(PrefsLoginConstant.IS_LOGIN, false)
            if (isLogin) {
                val intent = Intent(this, MenuActivity::class.java)
                this.startActivity(intent)
            } else {
                val intent = Intent(this, LoginActivity::class.java)
                this.startActivity(intent)
            }
        },1000)
    }
}