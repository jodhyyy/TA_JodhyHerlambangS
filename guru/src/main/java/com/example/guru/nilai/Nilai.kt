package com.example.guru.nilai

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.guru.Dashboard
import com.example.guru.R
import com.example.guru.databinding.ActivityNilaiBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.ArrayList
import java.util.Locale

class Nilai : AppCompatActivity() {
    private lateinit var binding: ActivityNilaiBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var eventListener: ValueEventListener
    private lateinit var dataList: MutableList<DataNilai>
    private lateinit var adapter: NilaiAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNilaiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            val intent = Intent(this@Nilai, Dashboard::class.java)
            startActivity(intent)
            finish()
        }

        val recyclerView = binding.recyclerView
        val fab = binding.fab
        val searchView = binding.search

        recyclerView.layoutManager = GridLayoutManager(this@Nilai, 1)

        val builder = AlertDialog.Builder(this@Nilai)
        builder.setCancelable(false)
        builder.setView(R.layout.progress_layout)
        val dialog = builder.create()

        dataList = ArrayList()
        adapter = NilaiAdapter(this@Nilai, dataList)
        recyclerView.adapter = adapter

        databaseReference = FirebaseDatabase.getInstance().getReference("Nilai")

        dialog.show()

        eventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dataList.clear()
                for (itemSnapshot in snapshot.children) {
                    val dataClass = itemSnapshot.getValue(DataNilai::class.java)
                    dataClass?.let { dataList.add(it) }
                }
                adapter.notifyDataSetChanged()
                dialog.dismiss()
            }

            override fun onCancelled(error: DatabaseError) {
                dialog.dismiss()
            }
        }

        databaseReference.addValueEventListener(eventListener)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                searchList(newText)
                return true
            }
        })

        fab.setOnClickListener {
            val intent = Intent(this@Nilai, UploadNilai::class.java)
            startActivity(intent)
        }
    }

    private fun searchList(text: String) {
        val searchList = ArrayList<DataNilai>()
        for (dataClass in dataList) {
            dataClass.nama?.let {
                if (it.toLowerCase(Locale.ROOT).contains(text.toLowerCase())) {
                    searchList.add(dataClass)
                }
            }
        }
        adapter.searchDataList(searchList)
    }

    override fun onDestroy() {
        super.onDestroy()
        databaseReference.removeEventListener(eventListener)
    }
}