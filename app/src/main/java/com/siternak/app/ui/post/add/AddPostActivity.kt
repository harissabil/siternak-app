package com.siternak.app.ui.post.add

import android.Manifest
//noinspection SuspiciousImport
import android.R
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.graphics.Insets
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.siternak.app.databinding.ActivityAddPostBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddPostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddPostBinding
    private val viewModel: AddPostViewModel by viewModel()
    private var postId: String? = null
    private var counterValue = 1
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        binding = ActivityAddPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars: Insets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            WindowInsetsCompat.CONSUMED
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    Toast.makeText(this, "Izin lokasi diberikan", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Izin lokasi ditolak", Toast.LENGTH_SHORT).show()
                }
            }

        postId = intent.getStringExtra("POST_ID")

        // Load post details if postId is not null
        if (postId != null) {
            viewModel.loadPostDetails(postId!!)
        }

        // Binding layout
        viewModel.post.observe(this) { post ->
            post?.let {
                binding.autoCompleteTextViewJenisTernak.setText(it.jenisTernak, false)
                binding.jumlahTernakLayout.editText?.setText(it.jumlahTernak.toString())
                binding.autoCompleteTextViewJenisAksi.setText(it.jenisAksi, false)
                binding.keteranganLayout.editText?.setText(it.keteranganAksi)
                binding.alamatLayout.editText?.setText(it.alamatAksi)
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        viewModel.message.observe(this) { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                if (it == "Data berhasil ditambahkan" || it == "Data berhasil diperbarui") {
                    finish()
                }
            }
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnSend.setOnClickListener {
            hideKeyboard(it)

            fun EditText.isEmpty(): Boolean {
                return this.text.toString().isEmpty()
            }

            fun areAllFieldsFilled(vararg fields: EditText): Boolean {
                return fields.all { !it.isEmpty() }
            }

            if (!areAllFieldsFilled(
                    binding.autoCompleteTextViewJenisTernak,
                    binding.autoCompleteTextViewJenisAksi,
                    binding.keteranganLayout.editText!!,
                    binding.alamatLayout.editText!!
                )
            ) {
                showFailedDialog("Pastikan semua data terisi!")
            } else {
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
                        // ambil long dan lat
                        val latitude = it.latitude
                        val longitude = it.longitude

                        // menentukan fungsi yang akan dipanggil pada viewmodel
                        // jika postId tidak null, maka fungsi updatePost dipanggil
                        // jika postId null maka fungsi addNewPost dipanggil
                        if (postId != null) {
                            viewModel.updatePost(
                                postId!!,
                                jenisTernak,
                                jumlahTernak,
                                jenisAksi,
                                keteranganAksi,
                                alamatAksi
                            )
                        } else {
                            viewModel.addNewPost(
                                jenisTernak,
                                jumlahTernak,
                                jenisAksi,
                                keteranganAksi,
                                alamatAksi,
                                latitude,
                                longitude
                            )
                        }
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
        val jenisTernakArray = arrayOf(
            "Sapi",
            "Kambing",
            "Domba",
            "Kerbau",
            "Kuda",
            "Babi",
            "Ayam",
            "Bebek",
            "Lainnya"
        )
        val jenisAksiArray = arrayOf("Pelaporan Penyakit", "Permintaan Vaksin")

        val ternakAdapter =
            ArrayAdapter(this, R.layout.simple_dropdown_item_1line, jenisTernakArray)
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

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun hideKeyboard(view: View) {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
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
}