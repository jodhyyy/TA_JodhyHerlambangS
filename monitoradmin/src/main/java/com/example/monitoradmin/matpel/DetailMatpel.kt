package com.example.monitoradmin.matpel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.monitoradmin.databinding.ActivityDetailMatpelBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class DetailMatpel : AppCompatActivity() {
    private lateinit var binding: ActivityDetailMatpelBinding
    private var mataPelajaran = ""
    private var imageUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMatpelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            val intent = Intent(this@DetailMatpel, MataPelajaran::class.java)
            startActivity(intent)
            finish()
        }

        val bundle = intent.extras
        bundle?.let {
            binding.detailMatpel.text = it.getString("MataPelajaran")
            mataPelajaran = it.getString("MataPelajaran") ?: ""
            imageUrl = it.getString("Image") ?: ""
            Glide.with(this@DetailMatpel).load(it.getString("Image")).into(binding.detailImage)
        }

        binding.deleteButton.setOnClickListener {
            val reference = FirebaseDatabase.getInstance().getReference("MataPelajaran")
            val storage = FirebaseStorage.getInstance()
            val storageReference: StorageReference = storage.getReferenceFromUrl(imageUrl)
            storageReference.delete().addOnSuccessListener {
                reference.child(mataPelajaran).removeValue()
                Toast.makeText(this@DetailMatpel, "Deleted", Toast.LENGTH_SHORT).show()
                startActivity(Intent(applicationContext, MataPelajaran::class.java))
                finish()
            }
        }

        binding.editButton.setOnClickListener {
            val intent = Intent(this@DetailMatpel, UpdateMatpel::class.java)
                .putExtra("MataPelajaran", binding.detailMatpel.text.toString())
                .putExtra("Image", imageUrl)
                .putExtra("Key", mataPelajaran)
            startActivity(intent)
        }
    }
}