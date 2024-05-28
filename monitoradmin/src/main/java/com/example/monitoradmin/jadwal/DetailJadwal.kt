package com.example.monitoradmin.jadwal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.monitoradmin.databinding.ActivityDetailJadwalBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class DetailJadwal : AppCompatActivity() {
    private lateinit var binding: ActivityDetailJadwalBinding
    private var hari = ""
    private var imageUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailJadwalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            val intent = Intent(this@DetailJadwal, Jadwal::class.java)
            startActivity(intent)
            finish()
        }

        val bundle = intent.extras
        bundle?.let {
            binding.detailMatpel.text = it.getString("MataPelajaran")
            binding.detailNamaGuru.text = it.getString("NamaGuru")
            binding.detailHari.text = it.getString("Hari")
            binding.detailJam.text = it.getString("Jam")
            hari = it.getString("Hari") ?: ""
            imageUrl = it.getString("Image") ?: ""
            Glide.with(this@DetailJadwal).load(it.getString("Image")).into(binding.detailImage)
        }

        binding.deleteButton.setOnClickListener {
            val reference = FirebaseDatabase.getInstance().getReference("Jadwal")
            val storage = FirebaseStorage.getInstance()
            val storageReference: StorageReference = storage.getReferenceFromUrl(imageUrl)
            storageReference.delete().addOnSuccessListener {
                reference.child(hari).removeValue()
                Toast.makeText(this@DetailJadwal, "Deleted", Toast.LENGTH_SHORT).show()
                startActivity(Intent(applicationContext, Jadwal::class.java))
                finish()
            }
        }

        binding.editButton.setOnClickListener {
            val intent = Intent(this@DetailJadwal, UpdateJadwal::class.java)
                .putExtra("MataPelajaran", binding.detailMatpel.text.toString())
                .putExtra("NamaGuru", binding.detailNamaGuru.text.toString())
                .putExtra("Hari", binding.detailHari.text.toString())
                .putExtra("Jam", binding.detailJam.text.toString())
                .putExtra("Image", imageUrl)
                .putExtra("Key", hari)
            startActivity(intent)
        }
    }
}