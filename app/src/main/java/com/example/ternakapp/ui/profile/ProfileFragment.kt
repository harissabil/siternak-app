package com.example.ternakapp.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.ternakapp.R
import com.example.ternakapp.databinding.FragmentProfileBinding
import com.example.ternakapp.ui.profile.detail.ProfileDetailActivity
import com.example.ternakapp.utils.NavigationUtils

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? AppCompatActivity)?.supportActionBar?.hide()

        // Header pada laman profil
        Glide.with(this)
            .load("https://i2.wp.com/zubnylekarpodolsky.sk/wp-content/uploads/2013/06/blank_profile.png")
            .circleCrop()
            .into(binding.ivPengguna)
        profileViewModel.loadUserName(requireContext())
        profileViewModel.userName.observe(viewLifecycleOwner) { userName ->
            binding.tvNamaPengguna.text = userName
        }

        binding.tvDataPengguna.setOnClickListener {
            val intent = Intent(requireContext(), ProfileDetailActivity::class.java)
            startActivity(intent)
        }

        binding.tvAturSandi.setOnClickListener {
            Toast.makeText(requireContext(), "Fitur belum tersedia", Toast.LENGTH_SHORT).show()
        }

        binding.tvBantuan.setOnClickListener {
            Toast.makeText(requireContext(), "Fitur belum tersedia", Toast.LENGTH_SHORT).show()
        }

        binding.btnLogout.setOnClickListener {
            profileViewModel.logoutUser(requireContext())
            NavigationUtils.navigateToLogin(requireContext())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}