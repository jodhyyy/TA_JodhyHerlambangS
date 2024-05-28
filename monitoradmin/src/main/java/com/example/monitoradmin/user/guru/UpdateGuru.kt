package com.example.monitoradmin.user.guru

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.monitoradmin.R
import com.example.monitoradmin.databinding.ActivityUpdateGuruBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

class UpdateGuru : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateGuruBinding
    private lateinit var uri: Uri
    private var imageUrl: String? = null
    private lateinit var nama: String
    private lateinit var oldImageURL: String
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateGuruBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            val intent = Intent(this@UpdateGuru, DetailGuru::class.java)
            startActivity(intent)
            finish()
        }

        val activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                uri = data?.data!!
                binding.updateImage.setImageURI(uri)
            } else {
                Toast.makeText(this@UpdateGuru, "No Image Selected", Toast.LENGTH_SHORT).show()
            }
        }

        val bundle = intent.extras
        if (bundle != null) {
            Glide.with(this).load(bundle.getString("Image")).into(binding.updateImage)
            binding.updateNip.setText(bundle.getString("Nip"))
            binding.updateNama.setText(bundle.getString("Nama"))
            binding.updateJabatan.setText(bundle.getString("Jabatan"))
            binding.updateNoHp.setText(bundle.getString("NoHp"))
            binding.updateAlamat.setText(bundle.getString("Alamat"))
            nama = bundle.getString("Nama", "")
            oldImageURL = bundle.getString("Image", "")
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("DetailGuru").child(nama)

        binding.updateImage.setOnClickListener {
            val photoPicker = Intent(Intent.ACTION_PICK).apply {
                type = "image/*"
            }
            activityResultLauncher.launch(photoPicker)
        }

        binding.updateButton.setOnClickListener {
            saveData()
            val intent = Intent(this@UpdateGuru, Guru::class.java)
            startActivity(intent)
        }
    }

    private fun saveData() {
        storageReference = FirebaseStorage.getInstance().getReference().child("Guru Images").child(uri.lastPathSegment!!)

        val builder = AlertDialog.Builder(this@UpdateGuru).apply {
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
        val nip = binding.updateNip.text.toString().trim()
        val nama = binding.updateNama.text.toString().trim()
        val jabatan = binding.updateJabatan.text.toString().trim()
        val noHp = binding.updateNoHp.text.toString().trim()
        val alamat = binding.updateAlamat.text.toString().trim()

        val dataGuru = DataGuru(nama, nip, jabatan, imageUrl, noHp, alamat)

        databaseReference.setValue(dataGuru).addOnCompleteListener(OnCompleteListener<Void> { task ->
            if (task.isSuccessful) {
                val reference = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageURL)
                reference.delete()
                Toast.makeText(this@UpdateGuru, "Updated", Toast.LENGTH_SHORT).show()
                finish()
            }
        }).addOnFailureListener(OnFailureListener { e ->
            Toast.makeText(this@UpdateGuru, "Failed Update", Toast.LENGTH_SHORT).show()
        })
    }
}