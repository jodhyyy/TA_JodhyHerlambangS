package com.example.guru.absen

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
import com.example.guru.databinding.ActivityUpdateAbsensiBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

class UpdateAbsensi : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateAbsensiBinding
    private lateinit var uri: Uri
    private var imageUrl: String? = null
    private lateinit var nama: String
    private lateinit var oldImageURL: String
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateAbsensiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            val intent = Intent(this@UpdateAbsensi, DetailAbsensi::class.java)
            startActivity(intent)
            finish()
        }

        val activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                uri = data?.data!!
                binding.updateImage.setImageURI(uri)
            } else {
                Toast.makeText(this@UpdateAbsensi, "No Image Selected", Toast.LENGTH_SHORT).show()
            }
        }

        val bundle = intent.extras
        if (bundle != null) {
            Glide.with(this@UpdateAbsensi).load(bundle.getString("Image")).into(binding.updateImage)
            binding.updateNis.setText(bundle.getString("Nis"))
            binding.updateNama.setText(bundle.getString("Nama"))
            binding.updateAbsensi.setText(bundle.getString("Absensi"))
            binding.updateTanggal.setText(bundle.getString("Tanggal"))
            binding.updateKeterangan.setText(bundle.getString("Keterangan"))
            nama = bundle.getString("Nama", "")
            oldImageURL = bundle.getString("Image", "")
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("Absensi").child(nama)

        binding.updateImage.setOnClickListener {
            val photoPicker = Intent(Intent.ACTION_PICK).apply {
                type = "image/*"
            }
            activityResultLauncher.launch(photoPicker)
        }

        binding.updateButton.setOnClickListener {
            saveData()
            val intent = Intent(this@UpdateAbsensi, Absensi::class.java)
            startActivity(intent)
        }
    }

    private fun saveData() {
        storageReference = FirebaseStorage.getInstance().getReference().child("Absensi Images").child(uri.lastPathSegment!!)

        val builder = AlertDialog.Builder(this@UpdateAbsensi).apply {
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
        val nis = binding.updateNis.text.toString().trim()
        val nama = binding.updateNama.text.toString().trim()
        val absensi = binding.updateAbsensi.text.toString().trim()
        val tanggal = binding.updateTanggal.text.toString().trim()
        val keterangan = binding.updateKeterangan.text.toString().trim()

        val dataAbsensi = DataAbsensi(nis, nama, absensi, tanggal, imageUrl, keterangan)

        databaseReference.setValue(dataAbsensi).addOnCompleteListener(OnCompleteListener<Void> { task ->
            if (task.isSuccessful) {
                val reference = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageURL)
                reference.delete()
                Toast.makeText(this@UpdateAbsensi, "Updated", Toast.LENGTH_SHORT).show()
                finish()
            }
        }).addOnFailureListener(OnFailureListener { e ->
            Toast.makeText(this@UpdateAbsensi, "Failed to Update", Toast.LENGTH_SHORT).show()
        })
    }
}