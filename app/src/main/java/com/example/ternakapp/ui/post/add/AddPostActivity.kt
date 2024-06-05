package com.example.ternakapp.ui.post.add

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.ternakapp.data.response.PostResponse
import com.example.ternakapp.data.retrofit.ApiConfig
import com.example.ternakapp.databinding.ActivityAddPostBinding
import retrofit2.Call
import retrofit2.Callback
import okhttp3.ResponseBody
import retrofit2.Response

class AddPostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddPostBinding
    private val viewModel: AddPostViewModel by viewModels()
    private var postId:String? = null

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityAddPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        postId = intent.getStringExtra("POST_ID")
        if (postId != null) {
            viewModel.loadPostDetails(postId!!)
        }
        viewModel.post.observe(this) { post ->
            post?.let {
                binding.edJenisTernakLayout.editText?.setText(it.jenisTernak)
                binding.edJenisAksiLayout.editText?.setText(it.jenisAksi)
                binding.edKeterangan.editText?.setText(it.keterangan)
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.message.observe(this) { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnBack.setOnClickListener{
            finish()
        }

        binding.btnSend.setOnClickListener{
            val jenisTernak = binding.edJenisTernakLayout.editText?.text.toString().trim()
            val jenisAksi = binding.edJenisAksiLayout.editText?.text.toString().trim()
            val keterangan = binding.edKeterangan.editText?.text.toString().trim()

            if (postId != null) {
                viewModel.updatePost(postId!!, jenisTernak, jenisAksi, keterangan)
            } else {
                viewModel.addNewPost(jenisTernak, jenisAksi, keterangan)
            }
        }

        supportActionBar?.hide()
    }
}