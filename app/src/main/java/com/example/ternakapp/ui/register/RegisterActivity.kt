package com.example.ternakapp.ui.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.ternakapp.databinding.ActivityRegisterBinding
import com.example.ternakapp.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()



        binding.registerButton.setOnClickListener {
            run {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(it.windowToken, 0)

                fun EditText.isEmpty(): Boolean {
                    return this.text.toString().isEmpty()
                }

                fun areAllFieldsFilled(vararg fields: EditText): Boolean {
                    return fields.all { it.text.isNotEmpty() }
                }

                if (!areAllFieldsFilled(binding.edRegPhone, binding.edRegPassword, binding.edRegFullname,
                        binding.edRegProvince, binding.edRegCity, binding.edRegRegion, binding.edRegAddress)) {
                    showFailedDialog("Pastikan semua data terisi!")
                }
            }
        }

        binding.toLogin.setOnClickListener{
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
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