package com.example.monitoradmin.user.orangtua

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.monitoradmin.databinding.ActivityDetailOrangTuaBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class DetailOrangTua : AppCompatActivity() {
    private lateinit var binding: ActivityDetailOrangTuaBinding
    private var nama = ""
    private var imageUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailOrangTuaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            val intent = Intent(this@DetailOrangTua, OrangTua::class.java)
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
            Glide.with(this@DetailOrangTua).load(it.getString("Image")).into(binding.detailImage)
        }

        binding.deleteButton.setOnClickListener {
            val reference = FirebaseDatabase.getInstance().getReference("DetailOrangTua")
            val storage = FirebaseStorage.getInstance()
            val storageReference: StorageReference = storage.getReferenceFromUrl(imageUrl)
            storageReference.delete().addOnSuccessListener {
                reference.child(nama).removeValue()
                Toast.makeText(this@DetailOrangTua, "Deleted", Toast.LENGTH_SHORT).show()
                startActivity(Intent(applicationContext, OrangTua::class.java))
                finish()
            }
        }

        binding.editButton.setOnClickListener {
            val intent = Intent(this@DetailOrangTua, UpdateOrangTua::class.java)
                .putExtra("Nama", binding.detailNama.text.toString())
                .putExtra("NamaSiswa", binding.detailNamaSiswa.text.toString())
                .putExtra("Jabatan", binding.detailJabatan.text.toString())
                .putExtra("Pekerjaan", binding.detailPekerjaan.text.toString())
                .putExtra("Alamat", binding.detailAlamat.text.toString())
                .putExtra("Image", imageUrl)
                .putExtra("Key", nama)
            startActivity(intent)
        }
    }
}