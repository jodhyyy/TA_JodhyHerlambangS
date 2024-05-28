package com.example.guru.siswa

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.guru.Dashboard
import com.example.guru.R
import com.example.guru.databinding.ActivitySiswaBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.ArrayList
import java.util.Locale

class Siswa : AppCompatActivity() {
    private lateinit var binding: ActivitySiswaBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var eventListener: ValueEventListener
    private lateinit var dataList: MutableList<DataSiswa>
    private lateinit var adapter: SiswaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySiswaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            val intent = Intent(this@Siswa, Dashboard::class.java)
            startActivity(intent)
            finish()
        }

        val recyclerView = binding.recyclerView
        val searchView = binding.search

        recyclerView.layoutManager = GridLayoutManager(this@Siswa, 1)

        val builder = AlertDialog.Builder(this@Siswa)
        builder.setCancelable(false)
        builder.setView(R.layout.progress_layout)
        val dialog = builder.create()

        dataList = ArrayList()
        adapter = SiswaAdapter(this@Siswa, dataList)
        recyclerView.adapter = adapter

        databaseReference = FirebaseDatabase.getInstance().getReference("DetailSiswa")
        dialog.show()

        eventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dataList.clear()
                for (itemSnapshot in snapshot.children) {
                    val dataClass = itemSnapshot.getValue(DataSiswa::class.java)
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
    }
    private fun searchList(text: String) {
        val searchList = ArrayList<DataSiswa>()
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