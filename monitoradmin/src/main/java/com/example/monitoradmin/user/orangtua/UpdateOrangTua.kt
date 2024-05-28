package com.example.monitoradmin.user.orangtua

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
import com.example.monitoradmin.databinding.ActivityUpdateOrangTuaBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

class UpdateOrangTua : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateOrangTuaBinding
    private lateinit var uri: Uri
    private var imageUrl: String? = null
    private lateinit var nama: String
    private lateinit var oldImageURL: String
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateOrangTuaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            val intent = Intent(this@UpdateOrangTua, DetailOrangTua::class.java)
            startActivity(intent)
            finish()
        }

        val activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                uri = data?.data!!
                binding.updateImage.setImageURI(uri)
            } else {
                Toast.makeText(this@UpdateOrangTua, "No Image Selected", Toast.LENGTH_SHORT).show()
            }
        }

        val bundle = intent.extras
        if (bundle != null) {
            Glide.with(this@UpdateOrangTua).load(bundle.getString("Image")).into(binding.updateImage)
            binding.updateNama.setText(bundle.getString("Nama"))
            binding.updateNamaSiswa.setText(bundle.getString("NamaSiswa"))
            binding.updateJabatan.setText(bundle.getString("Jabatan"))
            binding.updatePekerjaan.setText(bundle.getString("Pekerjaan"))
            binding.updateAlamat.setText(bundle.getString("Alamat"))
            nama = bundle.getString("Nama", "")
            oldImageURL = bundle.getString("Image", "")
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("DetailOrangTua").child(nama)

        binding.updateImage.setOnClickListener {
            val photoPicker = Intent(Intent.ACTION_PICK).apply {
                type = "image/*"
            }
            activityResultLauncher.launch(photoPicker)
        }

        binding.updateButton.setOnClickListener {
            saveData()
            val intent = Intent(this@UpdateOrangTua, OrangTua::class.java)
            startActivity(intent)
        }
    }

    private fun saveData() {
        storageReference = FirebaseStorage.getInstance().getReference().child("OrangTua Images").child(uri.lastPathSegment!!)

        val builder = AlertDialog.Builder(this@UpdateOrangTua).apply {
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
        val namaSiswa = binding.updateNamaSiswa.text.toString().trim()
        val jabatan = binding.updateJabatan.text.toString().trim()
        val pekerjaan = binding.updatePekerjaan.text.toString().trim()
        val alamat = binding.updateAlamat.text.toString().trim()

        val dataOrangTua = DataOrangTua(nama, namaSiswa, jabatan, pekerjaan, imageUrl, alamat)

        databaseReference.setValue(dataOrangTua).addOnCompleteListener(OnCompleteListener<Void> { task ->
            if (task.isSuccessful) {
                val reference = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageURL)
                reference.delete()
                Toast.makeText(this@UpdateOrangTua, "Updated", Toast.LENGTH_SHORT).show()
                finish()
            }
        }).addOnFailureListener(OnFailureListener { e ->
            Toast.makeText(this@UpdateOrangTua, "Failed to Update", Toast.LENGTH_SHORT).show()
        })
    }
}