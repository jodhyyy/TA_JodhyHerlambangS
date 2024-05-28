package com.example.monitoradmin.user.akunuser

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.monitoradmin.databinding.ActivityDetailAkunUserBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class DetailAkunUser : AppCompatActivity() {
    private lateinit var binding: ActivityDetailAkunUserBinding
    private var username = ""
    private var imageUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailAkunUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            val intent = Intent(this@DetailAkunUser, AkunUser::class.java)
            startActivity(intent)
            finish()
        }

        val bundle = intent.extras
        bundle?.let {
            binding.detailUsername.text = it.getString("Username")
            binding.detailNama.text = it.getString("Nama")
            binding.detailJabatan.text = it.getString("Jabatan")
            binding.detailPassword.text = it.getString("Password")
            username = it.getString("Username") ?: ""
            imageUrl = it.getString("Image") ?: ""
            Glide.with(this@DetailAkunUser).load(it.getString("Image")).into(binding.detailImage)
        }

        binding.deleteButton.setOnClickListener {
            val reference = FirebaseDatabase.getInstance().getReference("AkunUser")
            val storage = FirebaseStorage.getInstance()
            val storageReference: StorageReference = storage.getReferenceFromUrl(imageUrl)
            storageReference.delete().addOnSuccessListener {
                reference.child(username).removeValue()
                Toast.makeText(this@DetailAkunUser, "Deleted", Toast.LENGTH_SHORT).show()
                startActivity(Intent(applicationContext, AkunUser::class.java))
                finish()
            }
        }

        binding.editButton.setOnClickListener {
            val intent = Intent(this@DetailAkunUser, UpdateAkunUser::class.java)
                .putExtra("Username", binding.detailUsername.text.toString())
                .putExtra("Nama", binding.detailNama.text.toString())
                .putExtra("Jabatan", binding.detailJabatan.text.toString())
                .putExtra("Password", binding.detailPassword.text.toString())
                .putExtra("Image", imageUrl)
                .putExtra("Key", username)
            startActivity(intent)
        }
    }
}