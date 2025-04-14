package com.siternak.app.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.siternak.app.core.utils.NavigationUtils
import com.siternak.app.databinding.FragmentProfileBinding
import com.siternak.app.ui.profile.detail.ProfileDetailActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val profileViewModel: ProfileViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
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
            profileViewModel.logoutUser()
            NavigationUtils.navigateToLogin(requireContext())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}