package com.example.monitoradmin.user.akunuser

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
import com.example.monitoradmin.databinding.ActivityUpdateAkunUserBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

class UpdateAkunUser : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateAkunUserBinding
    private lateinit var uri: Uri
    private var imageUrl: String? = null
    private lateinit var username: String
    private lateinit var oldImageURL: String
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateAkunUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            val intent = Intent(this@UpdateAkunUser, DetailAkunUser::class.java)
            startActivity(intent)
            finish()
        }

        val activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                uri = data?.data!!
                binding.updateImage.setImageURI(uri)
            } else {
                Toast.makeText(this@UpdateAkunUser, "No Image Selected", Toast.LENGTH_SHORT).show()
            }
        }

        val bundle = intent.extras
        if (bundle != null) {
            Glide.with(this@UpdateAkunUser).load(bundle.getString("Image")).into(binding.updateImage)
            binding.updateUsername.setText(bundle.getString("Username"))
            binding.updateNama.setText(bundle.getString("Nama"))
            binding.updateJabatan.setText(bundle.getString("Jabatan"))
            binding.updatePassword.setText(bundle.getString("Password"))
            username = bundle.getString("Username", "")
            oldImageURL = bundle.getString("Image", "")
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("AkunUser").child(username)

        binding.updateImage.setOnClickListener {
            val photoPicker = Intent(Intent.ACTION_PICK).apply {
                type = "image/*"
            }
            activityResultLauncher.launch(photoPicker)
        }

        binding.updateButton.setOnClickListener {
            saveData()
            val intent = Intent(this@UpdateAkunUser, AkunUser::class.java)
            startActivity(intent)
        }
    }

    private fun saveData() {
        storageReference = FirebaseStorage.getInstance().getReference().child("User Images").child(uri.lastPathSegment!!)

        val builder = AlertDialog.Builder(this@UpdateAkunUser).apply {
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
        val username = binding.updateUsername.text.toString().trim()
        val nama = binding.updateNama.text.toString().trim()
        val jabatan = binding.updateJabatan.text.toString()
        val password = binding.updatePassword.text.toString()

        val dataAkun = DataAkun(username, nama, jabatan, imageUrl, password)

        databaseReference.setValue(dataAkun).addOnCompleteListener(OnCompleteListener<Void> { task ->
            if (task.isSuccessful) {
                val reference = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageURL)
                reference.delete()
                Toast.makeText(this@UpdateAkunUser, "Updated", Toast.LENGTH_SHORT).show()
                finish()
            }
        }).addOnFailureListener(OnFailureListener { e ->
            Toast.makeText(this@UpdateAkunUser, e.message.toString(), Toast.LENGTH_SHORT).show()
        })
    }
}