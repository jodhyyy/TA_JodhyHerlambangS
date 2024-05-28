package com.example.monitoradmin.user.akunuser

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.monitoradmin.R
import com.example.monitoradmin.databinding.ActivityUploadAkunUserBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class UploadAkunUser : AppCompatActivity() {
    private lateinit var binding: ActivityUploadAkunUserBinding
    private var uri: Uri? = null
    private var imageURL: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadAkunUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            val intent = Intent(this@UploadAkunUser, AkunUser::class.java)
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
            Toast.makeText(this@UploadAkunUser, "No Image Selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveData() {
        uri?.let {
            val storageReference =
                it.lastPathSegment?.let { it1 ->
                    FirebaseStorage.getInstance().reference.child("AkunUser")
                        .child(it1)
                }

            val builder = AlertDialog.Builder(this@UploadAkunUser)
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
                    Toast.makeText(this@UploadAkunUser, "Failed to upload image", Toast.LENGTH_SHORT).show()
                }
            }?.addOnFailureListener {
                dialog.dismiss()
                Toast.makeText(this@UploadAkunUser, "Failed to upload image", Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            Toast.makeText(this@UploadAkunUser, "No Image Selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadData() {
        val username = binding.uploadUsername.text.toString()
        val nama = binding.uploadNama.text.toString()
        val jabatan = binding.uploadJabatan.text.toString()
        val password = binding.uploadPassword.text.toString()

        val dataAkun = DataAkun(username, nama, jabatan, imageURL, password)

        FirebaseDatabase.getInstance().getReference("AkunUser").child(username)
            .setValue(dataAkun).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this@UploadAkunUser, "Saved", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@UploadAkunUser, "Failed to save: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}