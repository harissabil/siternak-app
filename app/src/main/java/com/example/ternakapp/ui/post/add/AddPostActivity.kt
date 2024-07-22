package com.example.ternakapp.ui.post.add

import android.Manifest
//noinspection SuspiciousImport
import android.R
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.ternakapp.data.local.AuthPreference
import com.example.ternakapp.databinding.ActivityAddPostBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class AddPostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddPostBinding
    private val viewModel: AddPostViewModel by viewModels()
    private var postId: String? = null
    private var counterValue = 1
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityAddPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Izin lokasi diberikan", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Izin lokasi ditolak", Toast.LENGTH_SHORT).show()
            }
        }

        postId = intent.getStringExtra("POST_ID")

        // Cek token
        val authPreference = AuthPreference(this)
        val token = authPreference.getToken()
        if (token !== null && postId != null) {
            viewModel.loadPostDetails(token, postId!!)
        }

        // Binding layout
        viewModel.post.observe(this) { post ->
            post?.let {
                binding.autoCompleteTextViewJenisTernak.setText(it.data.jenisTernak, false)
                binding.jumlahTernakLayout.editText?.setText(it.data.jumlahTernak.toString())
                binding.autoCompleteTextViewJenisAksi.setText(it.data.jenisAksi, false)
                binding.keteranganLayout.editText?.setText(it.data.keteranganAksi)
                binding.alamatLayout.editText?.setText(it.data.alamatAksi)
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.message.observe(this) { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                if (it == "Data berhasil ditambahkan" || it == "Data berhasil diperbarui") {
                    finish()
                }
            }
        }

        binding.btnBack.setOnClickListener{
            finish()
        }

        binding.btnSend.setOnClickListener{
            val jenisTernak = binding.autoCompleteTextViewJenisTernak.text.toString().trim()
            val jumlahTernak = counterValue.toString()
            val jenisAksi = binding.autoCompleteTextViewJenisAksi.text.toString().trim()
            val keteranganAksi = binding.keteranganLayout.editText?.text.toString().trim()
            val alamatAksi = binding.alamatLayout.editText?.text.toString().trim()

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestLocationPermission()
                return@setOnClickListener
            }

            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    // ambil long dan lat, ubah ke string karena tipe data long & lat yang diminta pada backend adalah string
                    val latitude = it.latitude.toString()
                    val longitude = it.longitude.toString()

                    // menentukan fungsi yang akan dipanggil pada viewmodel
                    // jika postId tidak null, maka fungsi updatePost dipanggil
                    // jika postId null maka fungsi addNewPost dipanggil
                    if (token != null) {
                        if (postId != null) {
                            viewModel.updatePost(token, postId!!, jenisTernak, jumlahTernak, jenisAksi, keteranganAksi, alamatAksi)
                        } else {
                            viewModel.addNewPost(token, jenisTernak, jumlahTernak, jenisAksi, keteranganAksi, alamatAksi, latitude, longitude)
                        }
                    } else {
                        Toast.makeText(this, "Gagal mendapatkan lokasi", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // fungsi dropdown untuk field jenis ternak dan jenis aksi
        initializeDropdowns()

        // fungsi counter value untuk field jumlah ternak
        setupCounterButtons()

        supportActionBar?.hide()
    }

    private fun requestLocationPermission() {
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun initializeDropdowns() {
        // atur manual tipe yang akan ditampilkan di dropdown
        val jenisTernakArray = arrayOf("Sapi", "Kambing", "Domba", "Kerbau", "Kuda", "Babi", "Ayam", "Bebek", "Lainnya")
        val jenisAksiArray = arrayOf("Pelaporan Penyakit", "Permintaan Vaksin")

        val ternakAdapter = ArrayAdapter(this, R.layout.simple_dropdown_item_1line, jenisTernakArray)
        binding.autoCompleteTextViewJenisTernak.setAdapter(ternakAdapter)
        val aksiAdapter = ArrayAdapter(this, R.layout.simple_dropdown_item_1line, jenisAksiArray)
        binding.autoCompleteTextViewJenisAksi.setAdapter(aksiAdapter)
    }

    private fun setupCounterButtons() {
        binding.btnDecrease.setOnClickListener {
            if (counterValue > 1) { // Menghindari nilai kurang dari 1
                counterValue--
                binding.tvCounter.text = counterValue.toString()
            }
        }

        binding.btnIncrease.setOnClickListener {
            counterValue++
            binding.tvCounter.text = counterValue.toString()
        }
    }
}