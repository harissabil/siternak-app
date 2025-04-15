package com.siternak.app.ui.profile.detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.Insets
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.siternak.app.databinding.ActivityDetailProfileBinding
import com.siternak.app.domain.model.UserData
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailProfileBinding
    private val viewModel: ProfileDetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        binding = ActivityDetailProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars: Insets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            WindowInsetsCompat.CONSUMED
        }

        viewModel.getUserProfile()

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
    private fun updateUI(user: UserData) {
        binding.tvNama.text = user.nama
        binding.tvNoTelp.text = user.nomorHp
        binding.tvProvinsi.text = user.provinsi
        binding.tvKota.text = user.kota
        binding.tvKecamatan.text = user.kecamatan
        binding.tvAlamat.text = user.alamat
    }
}