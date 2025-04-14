package com.siternak.app.ui.profile.detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.siternak.app.data.local.AuthPreference
import com.siternak.app.data.response.UserDataClass
import com.siternak.app.databinding.ActivityDetailProfileBinding

class ProfileDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailProfileBinding
    private val viewModel: ProfileDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val authPreference = AuthPreference(this)
        val token = authPreference.getToken()
        if (token.isNullOrEmpty()) {
            Toast.makeText(this, "Token tidak ditemukan. Silakan login kembali.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        viewModel.getUserProfile(token)

        viewModel.userProfile.observe(this) { user ->
            user?.let { updateUI(it) }
        }

        viewModel.isLoading.observe(this, Observer { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        viewModel.message.observe(this, Observer { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })

        binding.btnBack.setOnClickListener {
            finish()
        }

        supportActionBar?.hide()
    }

    // Mengatur tata letak berdasarkan data yang didapat dari UserDataClass
    private fun updateUI(user: UserDataClass) {
        binding.tvNama.text = user.nama
        binding.tvNoTelp.text = user.noTelp
        binding.tvProvinsi.text = user.provinsi
        binding.tvKota.text = user.kota
        binding.tvKecamatan.text = user.kecamatan
        binding.tvAlamat.text = user.alamat
    }
}