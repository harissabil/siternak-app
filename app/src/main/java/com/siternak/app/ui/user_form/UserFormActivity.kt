package com.siternak.app.ui.user_form

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.siternak.app.MainActivity
import com.siternak.app.R
import com.siternak.app.databinding.ActivityUserFormBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserFormActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserFormBinding

    private val viewModel: UserFormViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        binding = ActivityUserFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.hide()

        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        viewModel.message.observe(this) { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.isSaveSuccess.observe(this) { isSuccess ->
            if (isSuccess) {
                navigateToMainActivity()
            }
        }

        binding.saveButton.setOnClickListener {
            hideKeyboard(it)

            fun EditText.isEmpty(): Boolean {
                return this.text.toString().isEmpty()
            }

            fun areAllFieldsFilled(vararg fields: EditText): Boolean {
                return fields.all { !it.isEmpty() }
            }

            if (!areAllFieldsFilled(
                    binding.edRegFullname,
                    binding.edRegPhone,
                    binding.edRegProvince,
                    binding.edRegCity,
                    binding.edRegRegion,
                    binding.edRegAddress
                )
            ) {
                showFailedDialog("Pastikan semua data terisi!")
            } else {
                val nama = binding.edRegFullname.text.toString().trim()
                val noTelp = binding.edRegPhone.text.toString().trim()
                val provinsi = binding.edRegProvince.text.toString().trim()
                val kota = binding.edRegCity.text.toString().trim()
                val kecamatan = binding.edRegRegion.text.toString().trim()
                val alamat = binding.edRegAddress.text.toString().trim()
                viewModel.saveUserData(nama, noTelp, provinsi, kota, kecamatan, alamat)
            }
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

    private fun hideKeyboard(view: View) {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}