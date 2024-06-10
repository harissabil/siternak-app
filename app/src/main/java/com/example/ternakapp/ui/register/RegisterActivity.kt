package com.example.ternakapp.ui.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.ternakapp.databinding.ActivityRegisterBinding
import com.example.ternakapp.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        viewModel.message.observe(this) { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.isRegisterSuccess.observe(this) { isSuccess ->
            if (isSuccess) {
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        binding.registerButton.setOnClickListener {
            hideKeyboard(it)

            fun EditText.isEmpty(): Boolean {
                return this.text.toString().isEmpty()
            }

            fun areAllFieldsFilled(vararg fields: EditText): Boolean {
                return fields.all { it.text.isNotEmpty() }
            }

            if (!areAllFieldsFilled(
                    binding.edRegPhone, binding.edRegPassword, binding.edRegFullname,
                    binding.edRegProvince, binding.edRegCity, binding.edRegRegion, binding.edRegAddress
                )
            ) {
                showFailedDialog("Pastikan semua data terisi!")
            } else {
                val noTelp = binding.edRegPhone.text.toString().trim()
                val password = binding.edRegPassword.text.toString().trim()
                val nama = binding.edRegFullname.text.toString().trim()
                val provinsi = binding.edRegProvince.text.toString().trim()
                val kota = binding.edRegCity.text.toString().trim()
                val kecamatan = binding.edRegRegion.text.toString().trim()
                val alamat = binding.edRegAddress.text.toString().trim()
                viewModel.registerUser(noTelp, password, nama, provinsi, kota, kecamatan,alamat)
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

    private fun hideKeyboard(view: android.view.View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}