package com.example.guru.nilai

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.guru.databinding.ActivityDetailNilaiBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class DetailNilai : AppCompatActivity() {
    private lateinit var binding: ActivityDetailNilaiBinding
    private var nama = ""
    private var imageUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailNilaiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            val intent = Intent(this@DetailNilai, Nilai::class.java)
            startActivity(intent)
            finish()
        }

        val bundle = intent.extras
        bundle?.let {
            binding.detailNama.text = it.getString("Nama")
            binding.detailTopik.text = it.getString("Topik")
            binding.detailTanggal.text = it.getString("Tanggal")
            binding.detailNilai.text = it.getString("Nilai")
            nama = it.getString("Nama") ?: ""
            imageUrl = it.getString("Image") ?: ""
            Glide.with(this@DetailNilai).load(it.getString("Image")).into(binding.detailImage)
        }

        binding.deleteButton.setOnClickListener {
            val reference = FirebaseDatabase.getInstance().getReference("Nilai")
            val storage = FirebaseStorage.getInstance()
            val storageReference: StorageReference = storage.getReferenceFromUrl(imageUrl)
            storageReference.delete().addOnSuccessListener {
                reference.child(nama).removeValue()
                Toast.makeText(this@DetailNilai, "Deleted", Toast.LENGTH_SHORT).show()
                startActivity(Intent(applicationContext, Nilai::class.java))
                finish()
            }
        }

        binding.editButton.setOnClickListener {
            val intent = Intent(this@DetailNilai, UpdateNilai::class.java)
                .putExtra("Nama", binding.detailNama.text.toString())
                .putExtra("Topik", binding.detailTopik.text.toString())
                .putExtra("Tanggal", binding.detailTanggal.text.toString())
                .putExtra("Nilai", binding.detailNilai.text.toString())
                .putExtra("Image", imageUrl)
                .putExtra("Key", nama)
            startActivity(intent)
        }
    }
}