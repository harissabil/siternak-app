package com.example.ternakapp.ui.post.detail

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.ternakapp.data.local.AuthPreference
import com.example.ternakapp.data.response.PostResponse
import com.example.ternakapp.databinding.ActivityDetailPostBinding
import com.example.ternakapp.ui.post.add.AddPostActivity
import com.example.ternakapp.utils.DateUtils

class PostDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPostBinding
    private val viewModel: PostDetailViewModel by viewModels()
    private var postId: String? = null
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        setupObservers()
        loadPostDetails()
        setupListeners()
    }

    private fun init() {
        // enable refresh layout (supaya bisa di-swipe ke bawah untuk refresh)
        swipeRefreshLayout = binding.swipeRefreshLayout
        postId = intent.getStringExtra("POST_ID")
        token = AuthPreference(this).getToken()
    }

    // update adapter apabila sudah ada data baru
    // untuk update UI, layout harus di-refresh terlebih dulu
    private fun setupObservers() {
        viewModel.post.observe(this) { post ->
            swipeRefreshLayout.isRefreshing = false
            post?.let { updateUI(it) }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.message.observe(this) { message ->
            swipeRefreshLayout.isRefreshing = false
            message?.let { showMessage(it) }
        }
    }

    private fun loadPostDetails() {
        if (token != null && postId != null) {
            viewModel.loadPostDetails(token!!, postId!!)
        }
    }

    // setup listeners untuk mengatur aksi yg dilakukan terhadap button back, delete, dan edit
    private fun setupListeners() {
        swipeRefreshLayout.setOnRefreshListener {
            loadPostDetails()
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnDelete.setOnClickListener {
            postId?.let { id -> viewModel.deletePost(token!!, id) }
        }

        // aktivitas edit menggunakan layout yang sama dengan activity_add_post
        // logic edit disimpan pada addPostActivity
        binding.btnEdit.setOnClickListener {
            val intent = Intent(this, AddPostActivity::class.java).apply {
                putExtra("POST_ID", postId)
            }
            startActivity(intent)
        }

        supportActionBar?.hide()
    }

    private fun updateUI(post: PostResponse) {
        binding.apply {
            edPetugas.text = post.data.petugas
            edJenisTernak.text = post.data.jenisTernak
            edJumlahTernak.text = post.data.jumlahTernak.toString()
            edJenisAksi.text = post.data.jenisAksi
            edKeteranganAksi.text = post.data.keteranganAksi
            edAlamatAksi.text = post.data.alamatAksi
            edTanggal.text = DateUtils.formatDate(post.data.createdAt)
            edStatus.text = post.data.status
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        if (message == "Post berhasil dihapus") {
            val reloadIntent = Intent().apply {
                putExtra("NEEDS_RELOAD", true)
            }
            setResult(RESULT_OK, reloadIntent)
            finish()
        }
    }
}