package com.example.ternakapp.ui.post

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ternakapp.adapter.PostAdapter
import com.example.ternakapp.databinding.FragmentPostBinding
import com.example.ternakapp.ui.post.add.AddPostActivity
import com.example.ternakapp.ui.post.detail.PostDetailActivity

class PostFragment : Fragment() {

    private var _binding: FragmentPostBinding? = null
    private val viewModel: PostViewModel by viewModels()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = PostAdapter(requireContext(), null) { post ->
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

        viewModel.posts.observe(viewLifecycleOwner, Observer { post ->
            if (post != null) {
                binding.rvStory.visibility = View.GONE
                binding.tvNoPosts.visibility = View.VISIBLE
            } else {
                binding.rvStory.visibility = View.VISIBLE
                binding.tvNoPosts.visibility = View.GONE
            }
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        viewModel.message.observe(viewLifecycleOwner, Observer { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.loadPosts()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}