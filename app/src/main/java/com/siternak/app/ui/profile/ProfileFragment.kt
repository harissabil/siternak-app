package com.siternak.app.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.siternak.app.R
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

        profileViewModel.loadUserName()
        profileViewModel.userName.observe(viewLifecycleOwner) { userName ->
            binding.tvNamaPengguna.text = userName
        }
        profileViewModel.photoUrl.observe(viewLifecycleOwner) { photoUrl ->
            Glide.with(this)
                .load(photoUrl)
                .circleCrop()
                .error(R.drawable.blank_profile)
                .into(binding.ivPengguna)
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