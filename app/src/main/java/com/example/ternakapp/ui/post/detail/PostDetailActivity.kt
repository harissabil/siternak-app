package com.example.ternakapp.ui.post.detail

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.ternakapp.data.local.AuthPreference
import com.example.ternakapp.data.response.PostResponse
import com.example.ternakapp.data.retrofit.ApiConfig
import com.example.ternakapp.databinding.ActivityAddPostBinding
import com.example.ternakapp.databinding.ActivityDetailPostBinding
import com.example.ternakapp.ui.post.add.AddPostActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPostBinding
    private val viewModel: PostDetailViewModel by viewModels()
    private var postId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        postId = intent.getStringExtra("POST_ID")

        if (postId != null) {
            viewModel.loadPostDetails(postId!!)
        }

        viewModel.post.observe(this, Observer { post ->
            post?.let {
                binding.edPostId.text = it.data.postId
                binding.edPetugas.text = it.data.petugas
                binding.edJenisTernak.text = it.data.jenisTernak
                binding.edJenisAksi.text = it.data.jenisAksi
                binding.tvPostTanggal.text = it.data.createdAt
                binding.tvActionKeterangan.text = it.data.keterangan
                binding.tvActionStatus.text = it.data.status
            }
        })

        viewModel.isLoading.observe(this, Observer { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        viewModel.message.observe(this, Observer { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                if (it == "Post berhasil dihapus") {
                    finish()
                }
            }
        })

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnDelete.setOnClickListener {
            postId?.let { id ->
                val authPreference = AuthPreference(this)
                val token = authPreference.getToken()
                if (token != null) {
                    viewModel.deletePost(token, id)
                } else {
                    Toast.makeText(this, "Token tidak ditemukan. Silakan login kembali.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnEdit.setOnClickListener {
            val intent = Intent(this, AddPostActivity::class.java)
            intent.putExtra("POST_ID", postId)
            startActivity(intent)
        }

        supportActionBar?.title = "Detail Post"
    }
}