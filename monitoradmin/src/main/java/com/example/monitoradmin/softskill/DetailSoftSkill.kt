package com.example.monitoradmin.softskill

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.monitoradmin.databinding.ActivityDetailSoftSkillBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class DetailSoftSkill : AppCompatActivity() {
    private lateinit var binding: ActivityDetailSoftSkillBinding
    private var nama = ""
    private var imageUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailSoftSkillBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            val intent = Intent(this@DetailSoftSkill, SoftSkill::class.java)
            startActivity(intent)
            finish()
        }

        val bundle = intent.extras
        bundle?.let {
            binding.detailNis.text = it.getString("Nis")
            binding.detailNama.text = it.getString("Nama")
            binding.detailSoftskill.text = it.getString("Softskill")
            binding.detailKeterangan.text = it.getString("Keterangan")
            nama = it.getString("Nama") ?: ""
            imageUrl = it.getString("Image") ?: ""
            Glide.with(this@DetailSoftSkill).load(it.getString("Image")).into(binding.detailImage)
        }

        binding.deleteButton.setOnClickListener {
            val reference = FirebaseDatabase.getInstance().getReference("Softskill")
            val storage = FirebaseStorage.getInstance()
            val storageReference: StorageReference = storage.getReferenceFromUrl(imageUrl)
            storageReference.delete().addOnSuccessListener {
                reference.child(nama).removeValue()
                Toast.makeText(this@DetailSoftSkill, "Deleted", Toast.LENGTH_SHORT).show()
                startActivity(Intent(applicationContext, SoftSkill::class.java))
                finish()
            }
        }

        binding.editButton.setOnClickListener {
            val intent = Intent(this@DetailSoftSkill, UpdateSoftSkill::class.java)
                .putExtra("Nis", binding.detailNis.text.toString())
                .putExtra("Nama", binding.detailNama.text.toString())
                .putExtra("Softskill", binding.detailSoftskill.text.toString())
                .putExtra("Keterangan", binding.detailKeterangan.text.toString())
                .putExtra("Image", imageUrl)
                .putExtra("Key", nama)
            startActivity(intent)
        }
    }
}