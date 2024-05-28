package com.example.monitoradmin.user.guru

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.monitoradmin.databinding.ActivityDetailGuruBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class DetailGuru : AppCompatActivity() {
    private lateinit var binding: ActivityDetailGuruBinding
    private var nama = ""
    private var imageUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailGuruBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            val intent = Intent(this@DetailGuru, Guru::class.java)
            startActivity(intent)
            finish()
        }

        val bundle = intent.extras
        bundle?.let {
            binding.detailNama.text = it.getString("Nama")
            binding.detailNip.text = it.getString("Nip")
            binding.detailJabatan.text = it.getString("Jabatan")
            binding.detailNohp.text = it.getString("NoHp")
            binding.detailAlamat.text = it.getString("Alamat")
            nama = it.getString("Nama") ?: ""
            imageUrl = it.getString("Image") ?: ""
            Glide.with(this@DetailGuru).load(it.getString("Image")).into(binding.detailImage)
        }

        binding.deleteButton.setOnClickListener {
            val reference = FirebaseDatabase.getInstance().getReference("DetailGuru")
            val storage = FirebaseStorage.getInstance()
            val storageReference: StorageReference = storage.getReferenceFromUrl(imageUrl)
            storageReference.delete().addOnSuccessListener {
                reference.child(nama).removeValue()
                Toast.makeText(this@DetailGuru, "Deleted", Toast.LENGTH_SHORT).show()
                startActivity(Intent(applicationContext, Guru::class.java))
                finish()
            }
        }

        binding.editButton.setOnClickListener {
            val intent = Intent(this@DetailGuru, UpdateGuru::class.java)
                .putExtra("Nip", binding.detailNip.text.toString())
                .putExtra("Nama", binding.detailNama.text.toString())
                .putExtra("Jabatan", binding.detailJabatan.text.toString())
                .putExtra("NoHp", binding.detailNohp.text.toString())
                .putExtra("Alamat", binding.detailAlamat.text.toString())
                .putExtra("Image", imageUrl)
                .putExtra("Key", nama)
            startActivity(intent)
        }
    }
}