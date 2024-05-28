package com.example.guru.absen

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.guru.databinding.ActivityDetailAbsensiBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class DetailAbsensi : AppCompatActivity() {
    private lateinit var binding: ActivityDetailAbsensiBinding
    private var nama = ""
    private var imageUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailAbsensiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            val intent = Intent(this@DetailAbsensi, Absensi::class.java)
            startActivity(intent)
            finish()
        }

        val bundle = intent.extras
        bundle?.let {
            binding.detailNis.text = it.getString("Nis")
            binding.detailNama.text = it.getString("Nama")
            binding.detailAbsensi.text = it.getString("Absensi")
            binding.detailTanggal.text = it.getString("Tanggal")
            binding.detailKeterangan.text = it.getString("Keterangan")
            nama = it.getString("Nama") ?: ""
            imageUrl = it.getString("Image") ?: ""
            Glide.with(this@DetailAbsensi).load(it.getString("Image")).into(binding.detailImage)
        }

        binding.deleteButton.setOnClickListener {
            val reference = FirebaseDatabase.getInstance().getReference("Absensi")
            val storage = FirebaseStorage.getInstance()
            val storageReference: StorageReference = storage.getReferenceFromUrl(imageUrl)
            storageReference.delete().addOnSuccessListener {
                reference.child(nama).removeValue()
                Toast.makeText(this@DetailAbsensi, "Deleted", Toast.LENGTH_SHORT).show()
                startActivity(Intent(applicationContext, Absensi::class.java))
                finish()
            }
        }

        binding.editButton.setOnClickListener {
            val intent = Intent(this@DetailAbsensi, UpdateAbsensi::class.java)
                .putExtra("Nis", binding.detailNis.text.toString())
                .putExtra("Nama", binding.detailNama.text.toString())
                .putExtra("Absensi", binding.detailAbsensi.text.toString())
                .putExtra("Tanggal", binding.detailTanggal.text.toString())
                .putExtra("Keterangan", binding.detailKeterangan.text.toString())
                .putExtra("Image", imageUrl)
                .putExtra("Key", nama)
            startActivity(intent)
        }
    }
}