package com.example.monitoradmin.softskill

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.example.monitoradmin.R
import com.example.monitoradmin.databinding.ActivityUploadSoftSkillBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class UploadSoftSkill : AppCompatActivity() {
    private lateinit var binding: ActivityUploadSoftSkillBinding
    private var uri: Uri? = null
    private var imageURL: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadSoftSkillBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            val intent = Intent(this@UploadSoftSkill, SoftSkill::class.java)
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
            Toast.makeText(this@UploadSoftSkill, "No Image Selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveData() {
        uri?.let {
            val storageReference =
                it.lastPathSegment?.let { it1 ->
                    FirebaseStorage.getInstance().reference.child("Softskill Image")
                        .child(it1)
                }

            val builder = AlertDialog.Builder(this@UploadSoftSkill)
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
                    Toast.makeText(this@UploadSoftSkill, "Failed to upload image", Toast.LENGTH_SHORT).show()
                }
            }?.addOnFailureListener {
                dialog.dismiss()
                Toast.makeText(this@UploadSoftSkill, "Failed to upload image", Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            Toast.makeText(this@UploadSoftSkill, "No Image Selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadData() {
        val nis = binding.uploadNis.text.toString()
        val nama = binding.uploadNama.text.toString()
        val softSkill = binding.uploadSoftskill.text.toString()
        val keterangan = binding.uploadKeterangan.text.toString()

        val softskillData = SoftskillData(nis, nama, softSkill, imageURL, keterangan)

        FirebaseDatabase.getInstance().getReference("Softskill").child(nama)
            .setValue(softskillData).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this@UploadSoftSkill, "Saved", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@UploadSoftSkill, "Failed to save: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}