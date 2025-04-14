package com.siternak.app.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.siternak.app.data.local.AuthPreference
import com.siternak.app.data.response.PostLoc
import com.siternak.app.databinding.FragmentHomeBinding
import com.siternak.app.utils.DateUtils
import com.siternak.app.utils.NavigationUtils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
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

        // Pengaturan greeting pada HomeFragment
        homeViewModel.loadUserName(requireContext())
        homeViewModel.userName.observe(viewLifecycleOwner) { userName ->
            binding.tvHello.text = "Halo, $userName \uD83D\uDC4B"
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        // Pengaturan untuk meluncurkan permintaan izin lokasi
        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    getCurrentLocation()
                } else {
                    Toast.makeText(requireContext(), "Izin lokasi ditolak", Toast.LENGTH_SHORT).show()
                }
            }

        // Cek izin lokasi. Jika diizinkan, ambil lokasi sekarang. Jika tidak, tampilkan requestPermissionLauncher
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            getCurrentLocation()
        }

        binding.map.setMultiTouchControls(true)

        // Memuat data post dari API untuk ditampilkan dalam bentuk marker pada peta
        homeViewModel.posts.observe(viewLifecycleOwner) { posts ->
            posts?.let { updateMapWithPosts(it) }
        }

        homeViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Cek token. Jika ada, marker akan ditampilkan. Jika tidak, navigasikan ke halaman login.
        val authPreference = AuthPreference(requireContext())
        val token = authPreference.getToken()
        if (token != null) {
            homeViewModel.getAllPostsWithLoc(token)
        } else {
            NavigationUtils.navigateToLogin(requireContext())
        }
    }

    // Fungsi untuk mendapatkan lokasi sekarang, bisa dipisahkan ke dalam ViewModel jika perlu
    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
    private fun updateMapWithPosts(posts: List<PostLoc>) {
        for (post in posts) {
            val geoPoint = GeoPoint(post.latitude ?: 0.0, post.longitude ?: 0.0)
            val marker = Marker(binding.map)
            marker.position = geoPoint
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            marker.title = "${post.jenisAksi}\n" +
                    "Jenis ternak: ${post.jenisTernak}\n" +
                    "Tanggal: ${DateUtils.formatDate(post.createdAt)}\n" +
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