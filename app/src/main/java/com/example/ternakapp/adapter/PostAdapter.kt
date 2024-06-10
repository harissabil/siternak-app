package com.example.ternakapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ternakapp.data.response.PostResponse
import com.example.ternakapp.databinding.ItemPostBinding
import com.example.ternakapp.ui.post.detail.PostDetailActivity

class PostAdapter(
    private val context: Context,
    private var post: PostResponse?,
    private val onItemClick: (PostResponse) -> Unit
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    inner class PostViewHolder(private val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.root.setOnClickListener(this)
        }

        fun bind(post: PostResponse) {
            binding.jenisAksiTv.text = post.jenisAksi
            binding.createdDateTv.text = post.createdAt
            binding.statusTv.text = post.status
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                post?.let { onItemClick(it) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostAdapter.PostViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPostBinding.inflate(inflater, parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostAdapter.PostViewHolder, position: Int) {
        post?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int = if (post == null) 0 else 1

    @SuppressLint("NotifyDataSetChanged")
    fun updatePost(newPost: PostResponse) {
        post = newPost
        notifyDataSetChanged()
    }
}