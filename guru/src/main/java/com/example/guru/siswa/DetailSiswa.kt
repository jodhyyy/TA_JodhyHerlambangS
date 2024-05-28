package com.example.guru.siswa

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.guru.databinding.ActivityDetailSiswaBinding

class DetailSiswa : AppCompatActivity() {
    private lateinit var binding: ActivityDetailSiswaBinding
    private var nama = ""
    private var imageUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailSiswaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            val intent = Intent(this@DetailSiswa, Siswa::class.java)
            startActivity(intent)
            finish()
        }

        val bundle = intent.extras
        bundle?.let {
            binding.detailNis.text = it.getString("Nis")
            binding.detailNama.text = it.getString("Nama")
            binding.detailJabatan.text = it.getString("Jabatan")
            binding.detailNamaWali.text = it.getString("NamaWali")
            binding.detailAlamat.text = it.getString("Alamat")
            nama = it.getString("Nama") ?: ""
            imageUrl = it.getString("Image") ?: ""
            Glide.with(this@DetailSiswa).load(it.getString("Image")).into(binding.detailImage)
        }
    }
}