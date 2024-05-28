package com.example.guru.nilai

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.guru.R
import com.example.guru.databinding.ActivityUploadNilaiBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class UploadNilai : AppCompatActivity() {
    private lateinit var binding: ActivityUploadNilaiBinding
    private var uri: Uri? = null
    private var imageURL: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadNilaiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            val intent = Intent(this@UploadNilai, Nilai::class.java)
            startActivity(intent)
            finish()
        }

        binding.uploadImage.setOnClickListener {
            val photoPicker = Intent(Intent.ACTION_PICK)
            photoPicker.type = "image/*"
            activityResultLauncher.launch(photoPicker)
        }

        binding.saveButton.setOnClickListener {
            saveData()
        }
    }

    private val activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            uri = data?.data
            binding.uploadImage.setImageURI(uri)
        } else {
            Toast.makeText(this@UploadNilai, "No Image Selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveData() {
        uri?.let {
            val storageReference =
                it.lastPathSegment?.let { it1 ->
                    FirebaseStorage.getInstance().reference.child("Nilai Image")
                        .child(it1)
                }

            val builder = AlertDialog.Builder(this@UploadNilai)
            builder.setCancelable(false)
            builder.setView(R.layout.progress_layout)
            val dialog = builder.create()
            dialog.show()

            storageReference?.putFile(it)?.continueWithTask { task ->
                if (!task.isSuccessful) {
                    throw task.exception!!
                }
                task.result.storage.downloadUrl
            }?.addOnSuccessListener { uri ->
                imageURL = uri.toString()
                uploadData()
                dialog.dismiss()
            }?.addOnFailureListener {
                dialog.dismiss()
                Toast.makeText(this@UploadNilai, "Failed to upload image", Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            Toast.makeText(this@UploadNilai, "No Image Selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadData() {
        val nama = binding.uploadNama.text.toString()
        val topik = binding.uploadTopik.text.toString()
        val tanggal = binding.uploadTanggal.text.toString()
        val nilai = binding.uploadNilai.text.toString()

        val dataNilai = DataNilai(nama, topik, tanggal, imageURL, nilai)

        FirebaseDatabase.getInstance().getReference("Nilai").child(nama)
            .setValue(dataNilai).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this@UploadNilai, "Saved", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@UploadNilai, "Failed to save: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}