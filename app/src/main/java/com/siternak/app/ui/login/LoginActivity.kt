package com.siternak.app.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.Insets
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.siternak.app.MainActivity
import com.siternak.app.core.utils.getGoogleIdToken
import com.siternak.app.databinding.ActivityLoginBinding
import com.siternak.app.ui.register.RegisterActivity
import com.siternak.app.ui.user_form.UserFormActivity
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars: Insets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            WindowInsetsCompat.CONSUMED
        }

        supportActionBar?.hide()

        binding.loginButton.setOnClickListener {
            val noTelp = binding.loginEditEmail.text.toString()
            val password = binding.loginEditPassword.text.toString()
            if (noTelp.isNotEmpty() && password.isNotEmpty()) {
                viewModel.loginWithEmail(noTelp, password)
            } else {
                Toast.makeText(
                    this,
                    "Email dan Kata Sandi tidak boleh kosong",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.googleButton.setOnClickListener {
            lifecycleScope.launch {
                getGoogleIdToken(this@LoginActivity)?.let { token ->
                    viewModel.loginWithGoogle(token)
                }
            }
        }

        binding.toRegister.setOnClickListener {
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

        viewModel.isSuccess.observe(this) { isSuccess ->
            val isFormAlreadyFilled = viewModel.isFormAlreadyFilled.value

            if (isSuccess && isFormAlreadyFilled == false) {
                navigateToActivity(UserFormActivity::class.java)
                return@observe
            }

            if (isSuccess) {
                navigateToActivity(MainActivity::class.java)
            }
        }
    }

    private fun navigateToActivity(activity: Class<*>) {
        val intent = Intent(this, activity)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}