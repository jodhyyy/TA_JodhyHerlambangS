package com.example.guru.ortu

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.guru.databinding.ActivityDetailOrtuBinding

class DetailOrtu : AppCompatActivity() {
    private lateinit var binding: ActivityDetailOrtuBinding
    private var nama = ""
    private var imageUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailOrtuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            val intent = Intent(this@DetailOrtu, OrangTua::class.java)
            startActivity(intent)
            finish()
        }

        val bundle = intent.extras
        bundle?.let {
            binding.detailNama.text = it.getString("Nama")
            binding.detailNamaSiswa.text = it.getString("NamaSiswa")
            binding.detailJabatan.text = it.getString("Jabatan")
            binding.detailPekerjaan.text = it.getString("Pekerjaan")
            binding.detailAlamat.text = it.getString("Alamat")
            nama = it.getString("Nama") ?: ""
            imageUrl = it.getString("Image") ?: ""
            Glide.with(this@DetailOrtu).load(it.getString("Image")).into(binding.detailImage)
        }
    }
}