package com.example.jodhy_monitor.biodatasiswa

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.jodhy_monitor.databinding.FragmentBiodataSiswaBinding
import com.google.firebase.database.*

class BiodataSiswa : Fragment() {

    private var _binding: FragmentBiodataSiswaBinding? = null
    private val binding get() = _binding!!

    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentBiodataSiswaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        passUserData()
    }

    private fun setupViews() {
        databaseReference = FirebaseDatabase.getInstance().getReference("DetailSiswa")

        binding.apply {
            this.detailNama
            this.detailNis
            this.detailJabatan
            this.detailNamaWali
            this.detailAlamat
        }
    }

    private fun passUserData() {
        val detailNamaText = binding.detailNama.text.toString().trim()

        val query: Query = databaseReference.orderByChild("nama").equalTo(detailNamaText)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (data in snapshot.children) {
                        val detailNama = data.child("nama").getValue(String::class.java)
                        val detailNis = data.child("nis").getValue(String::class.java)
                        val detailJabatan = data.child("jabatan").getValue(String::class.java)
                        val detailNamaWali = data.child("namaWali").getValue(String::class.java)
                        val detailAlamat = data.child("alamat").getValue(String::class.java)

                        binding.detailNama.text = detailNama
                        binding.detailNis.text = detailNis
                        binding.detailJabatan.text = detailJabatan
                        binding.detailNamaWali.text = detailNamaWali
                        binding.detailAlamat.text = detailAlamat
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
