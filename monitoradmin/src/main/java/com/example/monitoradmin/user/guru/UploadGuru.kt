package com.example.monitoradmin.user.guru

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.example.monitoradmin.R
import com.example.monitoradmin.databinding.ActivityUploadGuruBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class UploadGuru : AppCompatActivity() {
    private lateinit var binding: ActivityUploadGuruBinding
    private var uri: Uri? = null
    private var imageURL: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadGuruBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            val intent = Intent(this@UploadGuru, Guru::class.java)
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
            Toast.makeText(this@UploadGuru, "No Image Selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveData() {
        uri?.let {
            val storageReference =
                it.lastPathSegment?.let { it1 ->
                    FirebaseStorage.getInstance().reference.child("Guru Image")
                        .child(it1)
                }

            val builder = AlertDialog.Builder(this@UploadGuru)
            builder.setCancelable(false)
            builder.setView(R.layout.progress_layout)
            val dialog = builder.create()
            dialog.show()

            storageReference?.putFile(it)?.addOnSuccessListener { taskSnapshot ->
                taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                    imageURL = uri.toString()
                    uploadData()
                    dialog.dismiss()
                }.addOnFailureListener {
                    dialog.dismiss()
                    Toast.makeText(this@UploadGuru, "Failed to upload image", Toast.LENGTH_SHORT).show()
                }
            }?.addOnFailureListener {
                dialog.dismiss()
                Toast.makeText(this@UploadGuru, "Failed to upload image", Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            Toast.makeText(this@UploadGuru, "No Image Selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadData() {
        val nip = binding.uploadNip.text.toString()
        val nama = binding.uploadNama.text.toString()
        val jabatan = binding.uploadJabatan.text.toString()
        val noHp = binding.uploadNohp.text.toString()
        val alamat = binding.uploadAlamat.text.toString()

        val dataGuru = DataGuru(nama, nip, jabatan, imageURL, noHp, alamat)

        FirebaseDatabase.getInstance().getReference("DetailGuru").child(nama)
            .setValue(dataGuru).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this@UploadGuru, "Saved", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@UploadGuru, "Failed to save: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}