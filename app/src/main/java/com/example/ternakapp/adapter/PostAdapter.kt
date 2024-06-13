package com.example.ternakapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ternakapp.data.response.PostItem
import com.example.ternakapp.databinding.ItemPostBinding
import com.example.ternakapp.utils.DateUtils

class PostAdapter(
    private val context: Context,
    private var posts: List<PostItem>,
    private val onItemClick: (PostItem) -> Unit
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    inner class PostViewHolder(private val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.root.setOnClickListener(this)
        }

        fun bind(post: PostItem) {
            binding.jenisAksiTv.text = post.jenisAksi
            binding.createdDateTv.text = DateUtils.formatDate(post.createdAt)
            binding.statusTv.text = post.status
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                posts[position].let { onItemClick(it) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPostBinding.inflate(inflater, parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        holder.bind(post)
    }

    override fun getItemCount(): Int = posts.size

    @SuppressLint("NotifyDataSetChanged")
    fun updatePosts(newPosts: List<PostItem>) {
        posts = newPosts
        notifyDataSetChanged()
    }
}