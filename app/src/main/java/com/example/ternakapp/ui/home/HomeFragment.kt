package com.example.ternakapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.ternakapp.data.response.PostResponse
import com.example.ternakapp.databinding.FragmentHomeBinding
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        // Konfigurasi OSMDroid
        Configuration.getInstance().load(
            requireContext(),
            androidx.preference.PreferenceManager.getDefaultSharedPreferences(requireContext()))
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Aktivasi multi-touch control
        binding.map.setMultiTouchControls(true)

        // Atur peta
        val mapController = binding.map.controller
        mapController.setZoom(15.0)
        val startPoint = GeoPoint(-6.556731, 106.725945) // set koordinat awal
        mapController.setCenter(startPoint)

        // Tambahkan marker pada lokasi awal
        val marker = Marker(binding.map)
        marker.position = startPoint
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.title = "Lokasi Anda"
        binding.map.overlays.add(marker)

//        homeViewModel.posts.observe(viewLifecycleOwner, Observer { posts ->
//            posts?.let { updateMapWithPosts(it) }
//        })

        homeViewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            binding.progressBar.visibility = if (isLoading) {
                android.view.View.VISIBLE
            } else {
                android.view.View.GONE
            }
        })

        homeViewModel.getAllPostsWithLoc()
    }

//    private fun updateMapWithPosts(posts: List<PostResponse>) {
//        for (post in posts) {
//            val geoPoint = GeoPoint(post.latitude ?: 0.0, post.longitude ?: 0.0)
//            val marker = Marker(binding.map)
//            marker.position = geoPoint
//            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
//            marker.title = post.jenisTernak
//            binding.map.overlays.add(marker)
//        }
//    }

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