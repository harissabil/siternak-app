package com.siternak.app.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.siternak.app.core.utils.toDateYyyyMmDd
import com.siternak.app.databinding.FragmentHomeBinding
import com.siternak.app.domain.model.Post
import com.siternak.app.domain.repository.AuthRepository
import com.siternak.app.ui.user_form.UserFormActivity
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModel()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    private val authRepository: AuthRepository by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Konfigurasi OSMDroid
        Configuration.getInstance().load(
            requireContext(),
            androidx.preference.PreferenceManager.getDefaultSharedPreferences(requireContext())
        )

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? AppCompatActivity)?.supportActionBar?.hide()

        val isLoggedIn = authRepository.isUserLoggedIn()
        if (!isLoggedIn) return

        // Setup observers and UI once
        setupObservers()
        setupMap()

        // Only initialize data if needed
        homeViewModel.loadUserName()
    }

    @SuppressLint("SetTextI18n")
    private fun setupObservers() {
        homeViewModel.isFormUserAlreadyFilled.observe(viewLifecycleOwner) { isFormUserAlreadyFilled ->
            if (isFormUserAlreadyFilled == false) {
                val intent = Intent(requireContext(), UserFormActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                requireContext().startActivity(intent)
            }
        }

        homeViewModel.userName.observe(viewLifecycleOwner) { userName ->
            binding.tvHello.text = "Halo, $userName \uD83D\uDC4B"
        }

        homeViewModel.posts.observe(viewLifecycleOwner) { posts ->
            Log.d("HomeFragment", "Posts: $posts")
            posts?.let {
                // Clear existing markers before adding new ones
                binding.map.overlays.clear()
                updateMapWithPosts(it)
            }
        }

        homeViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun setupMap() {
        binding.map.setMultiTouchControls(true)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                getCurrentLocation()
            } else {
                Toast.makeText(requireContext(), "Izin lokasi ditolak", Toast.LENGTH_SHORT).show()
            }
        }

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            getCurrentLocation()
        }
    }

    // Fungsi untuk mendapatkan lokasi sekarang, bisa dipisahkan ke dalam ViewModel jika perlu
    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                val mapController = binding.map.controller
                mapController.setZoom(15.0)
                val startPoint = GeoPoint(location.latitude, location.longitude)
                mapController.setCenter(startPoint)
                binding.map.invalidate()
            } else {
                Toast.makeText(requireContext(), "Gagal mendapatkan lokasi", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    // Fungsi untuk menambahkan dan mengatur marker pada peta
    private fun updateMapWithPosts(posts: List<Post>) {
        for (post in posts) {
            val geoPoint = GeoPoint(post.latitude!!, post.longitude!!)
            val marker = Marker(binding.map)
            marker.position = geoPoint
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            marker.title = "${post.jenisAksi}\n" +
                    "Jenis ternak: ${post.jenisTernak}\n" +
                    "Tanggal: ${post.createdAt?.toDateYyyyMmDd()}\n" +
                    "Status: ${post.status}"
            binding.map.overlays.add(marker)
        }
        binding.map.invalidate()
    }

    override fun onResume() {
        super.onResume()
        binding.map.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.map.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}