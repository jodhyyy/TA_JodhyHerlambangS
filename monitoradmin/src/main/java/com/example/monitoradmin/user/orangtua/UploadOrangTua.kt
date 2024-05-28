package com.example.monitoradmin.user.orangtua

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.example.monitoradmin.R
import com.example.monitoradmin.databinding.ActivityUploadOrangTuaBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class UploadOrangTua : AppCompatActivity() {
    private lateinit var binding: ActivityUploadOrangTuaBinding
    private var uri: Uri? = null
    private var imageURL: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadOrangTuaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            val intent = Intent(this@UploadOrangTua, OrangTua::class.java)
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
            Toast.makeText(this@UploadOrangTua, "No Image Selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveData() {
        uri?.let {
            val storageReference =
                it.lastPathSegment?.let { it1 ->
                    FirebaseStorage.getInstance().reference.child("OrangTua Image")
                        .child(it1)
                }

            val builder = AlertDialog.Builder(this@UploadOrangTua)
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
                    Toast.makeText(this@UploadOrangTua, "Failed to upload image", Toast.LENGTH_SHORT).show()
                }
            }?.addOnFailureListener {
                dialog.dismiss()
                Toast.makeText(this@UploadOrangTua, "Failed to upload image", Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            Toast.makeText(this@UploadOrangTua, "No Image Selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadData() {
        val nama = binding.uploadNama.text.toString()
        val namaSiswa = binding.uploadNamaSiswa.text.toString()
        val jabatan = binding.uploadJabatan.text.toString()
        val pekerjaan = binding.uploadPekerjaan.text.toString()
        val alamat = binding.uploadAlamat.text.toString()

        val dataOrangTua = DataOrangTua(nama, namaSiswa, jabatan, pekerjaan, imageURL, alamat)

        FirebaseDatabase.getInstance().getReference("DetailOrangTua").child(nama)
            .setValue(dataOrangTua).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this@UploadOrangTua, "Saved", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@UploadOrangTua, "Failed to save: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}