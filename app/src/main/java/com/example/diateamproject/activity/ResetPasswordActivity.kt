package com.example.diateamproject.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.diateamproject.databinding.ActivityResetPasswordBinding
import com.example.diateamproject.fragment.SuccessfullDialogFragment
import com.example.diateamproject.util.PrefsLoginConstant
import com.example.diateamproject.viewmodel.ResetPasswordViewModel
import com.pixplicity.easyprefs.library.Prefs


class ResetPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResetPasswordBinding
    var email = Prefs.getString(PrefsLoginConstant.EMAIL, "")
    private val viewModel: ResetPasswordViewModel by lazy {
        ViewModelProviders.of(this).get(ResetPasswordViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //token for sending data to other application
        val tokenUris: String = intent.data?.getQueryParameter("token").toString()
        Toast.makeText(this, "is token $tokenUris", Toast.LENGTH_SHORT).show()

        binding.btnSubmit.setOnClickListener {
            viewModel.postResetPassword(
                email,
                binding.etNewPassword.text.toString(),
                binding.etConfirmPassword.text.toString()
            ) }
        setObserver()
    }

    private fun setObserver() {
        viewModel.responseVerify().observe(this, Observer {
            val dialog = SuccessfullDialogFragment()
            dialog.show(supportFragmentManager, "successfullDialog")
        })

        viewModel.getIsError().observe(this, Observer {
            Toast.makeText(this, "Password didn't match", Toast.LENGTH_LONG).show()
        })
    }
}