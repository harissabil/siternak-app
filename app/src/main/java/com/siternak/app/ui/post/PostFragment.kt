package com.siternak.app.ui.post

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.siternak.app.adapter.PostAdapter
import com.siternak.app.data.local.AuthPreference
import com.siternak.app.databinding.FragmentPostBinding
import com.siternak.app.ui.post.add.AddPostActivity
import com.siternak.app.ui.post.detail.PostDetailActivity
import com.siternak.app.utils.NavigationUtils

class PostFragment : Fragment() {
    private lateinit var postDetailLauncher: ActivityResultLauncher<Intent>
    private var _binding: FragmentPostBinding? = null
    private val viewModel: PostViewModel by viewModels()
    private val binding get() = _binding!!
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.hide()

        setupSwipeRefreshLayout()
        setupPostDetailLauncher()
        checkAuthAndLoadPosts()

        val adapter = PostAdapter(requireContext(), emptyList()) { post ->
            val intent = Intent(requireContext(), PostDetailActivity::class.java).apply {
                putExtra("POST_ID", post.postId)
            }
            startActivity(intent)
        }

        binding.rvStory.layoutManager = LinearLayoutManager(requireContext())
        binding.rvStory.adapter = adapter

        binding.fab.setOnClickListener {
            val intent = Intent(requireContext(), AddPostActivity::class.java)
            startActivity(intent)
        }

        viewModel.posts.observe(viewLifecycleOwner) { posts ->
            swipeRefreshLayout.isRefreshing = false
            if (posts.isNotEmpty()) {
                binding.rvStory.visibility = View.VISIBLE
                binding.tvNoPosts.visibility = View.GONE
                adapter.updatePosts(posts)
            } else {
                binding.rvStory.visibility = View.GONE
                binding.tvNoPosts.visibility = View.VISIBLE
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.message.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupSwipeRefreshLayout() {
        swipeRefreshLayout = binding.swipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            reloadPosts()
        }
    }

    private fun reloadPosts() {
        AuthPreference(requireContext()).getToken()?.let {
            viewModel.reloadPosts(it)
        }
    }

    private fun setupPostDetailLauncher() {
        postDetailLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                result.data?.getBooleanExtra("NEEDS_RELOAD", false)?.let {
                    if (it) reloadPosts()
                }
            }
        }
    }

    private fun checkAuthAndLoadPosts() {
        val token = AuthPreference(requireContext()).getToken()
        if (token.isNullOrEmpty()) {
            NavigationUtils.navigateToLogin(requireContext())
        } else {
            viewModel.loadPosts(token)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}