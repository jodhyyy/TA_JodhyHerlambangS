package com.example.monitoradmin.matpel

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.example.monitoradmin.R
import com.example.monitoradmin.databinding.ActivityUploadMatpelBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class UploadMatpel : AppCompatActivity() {
    private lateinit var binding: ActivityUploadMatpelBinding
    private var uri: Uri? = null
    private var imageURL: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadMatpelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            val intent = Intent(this@UploadMatpel, MataPelajaran::class.java)
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
            Toast.makeText(this@UploadMatpel, "No Image Selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveData() {
        uri?.let {
            val storageReference =
                it.lastPathSegment?.let { it1 ->
                    FirebaseStorage.getInstance().reference.child("Matpel Image")
                        .child(it1)
                }

            val builder = AlertDialog.Builder(this@UploadMatpel)
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
                    Toast.makeText(this@UploadMatpel, "Failed to upload image", Toast.LENGTH_SHORT).show()
                }
            }?.addOnFailureListener {
                dialog.dismiss()
                Toast.makeText(this@UploadMatpel, "Failed to upload image", Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            Toast.makeText(this@UploadMatpel, "No Image Selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadData() {
        val mataPelajaran = binding.uploadMatpel.text.toString()

        val matpelData = MatpelData(mataPelajaran, imageURL)

        FirebaseDatabase.getInstance().getReference("MataPelajaran").child(mataPelajaran)
            .setValue(matpelData).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this@UploadMatpel, "Saved", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@UploadMatpel, "Failed to save: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}