package com.example.monitoradmin.matpel

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
import com.example.monitoradmin.databinding.ActivityUpdateMatpelBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

class UpdateMatpel : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateMatpelBinding
    private lateinit var uri: Uri
    private var imageUrl: String? = null
    private lateinit var mataPelajaran: String
    private lateinit var oldImageURL: String
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateMatpelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            val intent = Intent(this@UpdateMatpel, DetailMatpel::class.java)
            startActivity(intent)
            finish()
        }

        val activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                uri = data?.data!!
                binding.updateImage.setImageURI(uri)
            } else {
                Toast.makeText(this@UpdateMatpel, "No Image Selected", Toast.LENGTH_SHORT).show()
            }
        }

        val bundle = intent.extras
        if (bundle != null) {
            Glide.with(this@UpdateMatpel).load(bundle.getString("Image")).into(binding.updateImage)
            binding.updateMatpel.setText(bundle.getString("MataPelajaran"))
            mataPelajaran = bundle.getString("MataPelajaran", "")
            oldImageURL = bundle.getString("Image", "")
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("MataPelajaran").child(mataPelajaran)

        binding.updateImage.setOnClickListener {
            val photoPicker = Intent(Intent.ACTION_PICK).apply {
                type = "image/*"
            }
            activityResultLauncher.launch(photoPicker)
        }

        binding.updateButton.setOnClickListener {
            saveData()
            val intent = Intent(this@UpdateMatpel, MataPelajaran::class.java)
            startActivity(intent)
        }
    }

    private fun saveData() {
        storageReference = FirebaseStorage.getInstance().getReference().child("Matpel Images").child(uri.lastPathSegment!!)

        val builder = AlertDialog.Builder(this@UpdateMatpel).apply {
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
        val mataPelajaran = binding.updateMatpel.text.toString().trim()

        val matpelData = MatpelData(mataPelajaran, imageUrl)

        databaseReference.setValue(matpelData).addOnCompleteListener(OnCompleteListener<Void> { task ->
            if (task.isSuccessful) {
                val reference = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageURL)
                reference.delete()
                Toast.makeText(this@UpdateMatpel, "Updated", Toast.LENGTH_SHORT).show()
                finish()
            }
        }).addOnFailureListener(OnFailureListener { e ->
            Toast.makeText(this@UpdateMatpel, e.message.toString(), Toast.LENGTH_SHORT).show()
        })
    }
}