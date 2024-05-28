package com.example.monitoradmin.user.siswa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.monitoradmin.databinding.ActivityDetailSiswaBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

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

        binding.deleteButton.setOnClickListener {
            val reference = FirebaseDatabase.getInstance().getReference("DetailSiswa")
            val storage = FirebaseStorage.getInstance()
            val storageReference: StorageReference = storage.getReferenceFromUrl(imageUrl)
            storageReference.delete().addOnSuccessListener {
                reference.child(nama).removeValue()
                Toast.makeText(this@DetailSiswa, "Deleted", Toast.LENGTH_SHORT).show()
                startActivity(Intent(applicationContext, Siswa::class.java))
                finish()
            }
        }

        binding.editButton.setOnClickListener {
            val intent = Intent(this@DetailSiswa, UpdateSiswa::class.java)
                .putExtra("Nis", binding.detailNis.text.toString())
                .putExtra("Nama", binding.detailNama.text.toString())
                .putExtra("Jabatan", binding.detailJabatan.text.toString())
                .putExtra("NamaWali", binding.detailNamaWali.text.toString())
                .putExtra("Alamat", binding.detailAlamat.text.toString())
                .putExtra("Image", imageUrl)
                .putExtra("Key", nama)
            startActivity(intent)
        }
    }
}