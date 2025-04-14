package com.siternak.app.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.siternak.app.MainActivity
import com.siternak.app.data.local.AuthPreference
import com.siternak.app.databinding.ActivityLoginBinding
import com.siternak.app.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.loginButton.setOnClickListener {
            val noTelp = binding.loginEditPhone.text.toString()
            val password = binding.loginEditPassword.text.toString()
            if (noTelp.isNotEmpty() && password.isNotEmpty()) {
                viewModel.loginUser(noTelp, password)
            } else {
                Toast.makeText(this, "Nomor HP dan Kata Sandi tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
        }

        binding.toRegister.setOnClickListener{
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.message.observe(this) { message ->
            message?.let {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.loginResponse.observe(this) { loginResponse ->
            loginResponse?.let {
                val authPreference = AuthPreference(this)
                authPreference.setToken(it.loginResult.token)

                val preferences = getSharedPreferences("auth_prefs", MODE_PRIVATE)
                with(preferences.edit()) {
                    putString("user_name", it.loginResult.nama)
                    apply()
                }

                navigateToMainActivity()
            }
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}