package com.example.ternakapp.ui.post.detail

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.ternakapp.data.local.AuthPreference
import com.example.ternakapp.databinding.ActivityDetailPostBinding
import com.example.ternakapp.ui.post.add.AddPostActivity
import com.example.ternakapp.utils.DateUtils

class PostDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPostBinding
    private val viewModel: PostDetailViewModel by viewModels()
    private var postId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        postId = intent.getStringExtra("POST_ID")

        val authPreference = AuthPreference(this)
        val token = authPreference.getToken()

        if (token != null && postId != null) {
            viewModel.loadPostDetails(token, postId!!)
        }

        viewModel.post.observe(this) { post ->
            post?.let {
                binding.edPetugas.text = it.data.petugas
                binding.edJenisTernak.text = it.data.jenisTernak
                binding.edJenisAksi.text = it.data.jenisAksi
                binding.edTanggal.text = DateUtils.formatDate(it.data.createdAt)
                binding.edKeterangan.text = it.data.keterangan
                binding.edStatus.text = it.data.status
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.message.observe(this) { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                if (it == "Post berhasil dihapus") {
                    // auto reload data
                    val reloadIntent = Intent().apply {
                        putExtra("NEEDS_RELOAD", true)
                    }
                    setResult(RESULT_OK, reloadIntent)
                    finish()
                }
            }
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnDelete.setOnClickListener {
            postId?.let { id -> viewModel.deletePost(token!!, id) }
        }

        binding.btnEdit.setOnClickListener {
            val intent = Intent(this, AddPostActivity::class.java)
            intent.putExtra("POST_ID", postId)
            startActivity(intent)
        }
        supportActionBar?.hide()
    }
}