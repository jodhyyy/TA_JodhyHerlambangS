package com.example.guru.nilai

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.guru.R
import com.example.guru.databinding.ActivityUpdateNilaiBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

class UpdateNilai : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateNilaiBinding
    private lateinit var uri: Uri
    private var imageUrl: String? = null
    private lateinit var nama: String
    private lateinit var oldImageURL: String
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateNilaiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            val intent = Intent(this@UpdateNilai, DetailNilai::class.java)
            startActivity(intent)
            finish()
        }

        val activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                uri = data?.data!!
                binding.updateImage.setImageURI(uri)
            } else {
                Toast.makeText(this@UpdateNilai, "No Image Selected", Toast.LENGTH_SHORT).show()
            }
        }

        val bundle = intent.extras
        if (bundle != null) {
            Glide.with(this@UpdateNilai).load(bundle.getString("Image")).into(binding.updateImage)
            binding.updateNama.setText(bundle.getString("Nama"))
            binding.updateTopik.setText(bundle.getString("Topik"))
            binding.updateTanggal.setText(bundle.getString("Tanggal"))
            binding.updateNilai.setText(bundle.getString("Nilai"))
            nama = bundle.getString("Nama", "")
            oldImageURL = bundle.getString("Image", "")
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("Nilai").child(nama)

        binding.updateImage.setOnClickListener {
            val photoPicker = Intent(Intent.ACTION_PICK).apply {
                type = "image/*"
            }
            activityResultLauncher.launch(photoPicker)
        }

        binding.updateButton.setOnClickListener {
            saveData()
            val intent = Intent(this@UpdateNilai, Nilai::class.java)
            startActivity(intent)
        }
    }

    private fun saveData() {
        storageReference = FirebaseStorage.getInstance().getReference().child("Nilai Images").child(uri.lastPathSegment!!)

        val builder = AlertDialog.Builder(this@UpdateNilai).apply {
            setCancelable(false)
            setView(R.layout.progress_layout)
        }
        val dialog = builder.create()
        dialog.show()

        storageReference.putFile(uri).addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
            val uriTask = taskSnapshot.storage.downloadUrl
            while (!uriTask.isComplete);
            val urlImage = uriTask.result
            imageUrl = urlImage.toString()
            updateData()
            dialog.dismiss()
        }).addOnFailureListener(OnFailureListener { e ->
            dialog.dismiss()
        })
    }

    private fun updateData() {
        val nama = binding.updateNama.text.toString().trim()
        val topik = binding.updateTopik.text.toString().trim()
        val tanggal = binding.updateTanggal.text.toString().trim()
        val nilai = binding.updateNilai.text.toString().trim()

        val dataNilai = DataNilai(nama, topik, tanggal, imageUrl, nilai)

        databaseReference.setValue(dataNilai).addOnCompleteListener(OnCompleteListener<Void> { task ->
            if (task.isSuccessful) {
                val reference = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageURL)
                reference.delete()
                Toast.makeText(this@UpdateNilai, "Updated", Toast.LENGTH_SHORT).show()
                finish()
            }
        }).addOnFailureListener(OnFailureListener { e ->
            Toast.makeText(this@UpdateNilai, "Failed to Update", Toast.LENGTH_SHORT).show()
        })
    }
}