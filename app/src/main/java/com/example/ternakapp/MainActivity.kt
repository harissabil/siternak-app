package com.example.ternakapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.ternakapp.data.local.AuthPreference
import com.example.ternakapp.databinding.ActivityMainBinding
import com.example.ternakapp.ui.login.LoginActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*
        * AuthPreference digunakan untuk menyimpan token yang didapat ketika user berhasil login.
        * Pada setiap page, dilakukan pengecekan autentikasi untuk memastikan bahwa user yang mengakses
        * aplikasi adalah user yang telah login.
        */

        val authPreference = AuthPreference(this)
        val token = authPreference.getToken()
        if (token.isNullOrEmpty()) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        /*
        * AppBarConfiguration digunakan untuk mengkonfigurasi navigasi yang ada pada aplikasi.
        * Konfigurasi ini digunakan untuk mengatur tata letak dan perilaku dari AppBar atau ActionBar.
        * navigation_home mengarahkan ke fragment HomeFragment
        * navigation_post mengarahkan ke fragment PostFragment
        * navigation_profile mengarahkan ke fragment ProfileFragment
        */

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_post, R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}