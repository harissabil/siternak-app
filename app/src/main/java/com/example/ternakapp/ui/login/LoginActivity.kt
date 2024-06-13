package com.example.ternakapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.ternakapp.MainActivity
import com.example.ternakapp.data.local.AuthPreference
import com.example.ternakapp.databinding.ActivityLoginBinding
import com.example.ternakapp.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        // check are fields empty
        binding.loginButton.setOnClickListener {
            val noTelp = binding.loginEditPhone.text.toString()
            val password = binding.loginEditPassword.text.toString()
            if (noTelp.isNotEmpty() && password.isNotEmpty()) {
                viewModel.loginUser(noTelp, password)
            } else {
                Toast.makeText(this, "Nomor HP dan Kata Sandi tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
        }

        // go to register activity
        binding.toRegister.setOnClickListener{
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        viewModel.isLoading.observe(this, Observer { isLoading ->
            binding.progressBar.visibility = if (isLoading) {
                android.view.View.VISIBLE
            } else {
                android.view.View.GONE
            }
        })

        viewModel.message.observe(this, Observer { message ->
            message?.let {
                Toast.makeText(this, message ?: "Error", Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.loginResponse.observe(this, Observer { loginResponse ->
            loginResponse?.let {
                val authPreference = AuthPreference(this)
                authPreference.setToken(loginResponse.loginResult.token)
                navigateToMainActivity()

            }
        })
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}