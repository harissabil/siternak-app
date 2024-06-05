package com.example.ternakapp.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.ternakapp.databinding.ActivityLoginBinding
import com.example.ternakapp.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.loginButton.setOnClickListener {
            run {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(it.windowToken, 0)

                if (binding.loginEditPhone.text.toString().isEmpty() || binding.loginEditPassword.text.toString().isEmpty())
                {
                    showFailedDialog("Pastikan semua data terisi")
                }

                else if (binding.loginEditPhone.error == null && binding.loginEditPassword.error == null) {
                    // masukkan val loginViewModel dari class LoginViewModel
                }
            }
        }

        binding.toRegister.setOnClickListener{
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showFailedDialog(message: String) {
        showLoading(false)
        AlertDialog.Builder(this).apply {
            setTitle("Failed")
            setMessage(message)
            setPositiveButton("Continue") { dialog, _ -> dialog.dismiss() }
            create()
            show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}