package com.example.monitoradmin.user.siswa

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.monitoradmin.R
import com.example.monitoradmin.databinding.ActivityUpdateSiswaBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

class UpdateSiswa : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateSiswaBinding
    private lateinit var uri: Uri
    private var imageUrl: String? = null
    private lateinit var nama: String
    private lateinit var oldImageURL: String
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateSiswaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            val intent = Intent(this@UpdateSiswa, DetailSiswa::class.java)
            startActivity(intent)
            finish()
        }

        val activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                uri = data?.data!!
                binding.updateImage.setImageURI(uri)
            } else {
                Toast.makeText(this@UpdateSiswa, "No Image Selected", Toast.LENGTH_SHORT).show()
            }
        }

        val bundle = intent.extras
        if (bundle != null) {
            Glide.with(this).load(bundle.getString("Image")).into(binding.updateImage)
            binding.updateNis.setText(bundle.getString("Nis"))
            binding.updateNama.setText(bundle.getString("Nama"))
            binding.updateJabatan.setText(bundle.getString("Jabatan"))
            binding.updateNamaWali.setText(bundle.getString("NamaWali"))
            binding.updateAlamat.setText(bundle.getString("Alamat"))
            nama = bundle.getString("Nama", "")
            oldImageURL = bundle.getString("Image", "")
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("DetailSiswa").child(nama)

        binding.updateImage.setOnClickListener {
            val photoPicker = Intent(Intent.ACTION_PICK).apply {
                type = "image/*"
            }
            activityResultLauncher.launch(photoPicker)
        }

        binding.updateButton.setOnClickListener {
            saveData()
            val intent = Intent(this@UpdateSiswa, Siswa::class.java)
            startActivity(intent)
        }
    }

    private fun saveData() {
        storageReference = FirebaseStorage.getInstance().getReference().child("Siswa Images").child(uri.lastPathSegment!!)

        val builder = AlertDialog.Builder(this@UpdateSiswa).apply {
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
        val jabatan = binding.updateJabatan.text.toString().trim()
        val namaWali = binding.updateNamaWali.text.toString().trim()
        val alamat = binding.updateAlamat.text.toString().trim()

        val dataSiswa = DataSiswa(nis, nama, jabatan, namaWali, imageUrl, alamat)

        databaseReference.setValue(dataSiswa).addOnCompleteListener(OnCompleteListener<Void> { task ->
            if (task.isSuccessful) {
                val reference = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageURL)
                reference.delete()
                Toast.makeText(this@UpdateSiswa, "Updated", Toast.LENGTH_SHORT).show()
                finish()
            }
        }).addOnFailureListener(OnFailureListener { e ->
            Toast.makeText(this@UpdateSiswa, "Failed to Update", Toast.LENGTH_SHORT).show()
        })
    }
}