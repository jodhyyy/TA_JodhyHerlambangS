package com.example.monitoradmin.softskill

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.monitoradmin.Dashboard
import com.example.monitoradmin.R
import com.example.monitoradmin.databinding.ActivitySoftSkillBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.ArrayList
import java.util.Locale

class SoftSkill : AppCompatActivity() {
    private lateinit var binding: ActivitySoftSkillBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var eventListener: ValueEventListener
    private lateinit var dataList: MutableList<SoftskillData>
    private lateinit var adapter: SoftSkillAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySoftSkillBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            val intent = Intent(this@SoftSkill, Dashboard::class.java)
            startActivity(intent)
            finish()
        }

        val recyclerView = binding.recyclerView
        val fab = binding.fab
        val searchView = binding.search

        recyclerView.layoutManager = GridLayoutManager(this@SoftSkill, 1)

        val builder = AlertDialog.Builder(this@SoftSkill)
        builder.setCancelable(false)
        builder.setView(R.layout.progress_layout)
        val dialog = builder.create()

        dataList = ArrayList()
        adapter = SoftSkillAdapter(this@SoftSkill, dataList)
        recyclerView.adapter = adapter

        databaseReference = FirebaseDatabase.getInstance().getReference("Softskill")

        dialog.show()

        eventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dataList.clear()
                for (itemSnapshot in snapshot.children) {
                    val dataClass = itemSnapshot.getValue(SoftskillData::class.java)
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
            val intent = Intent(this@SoftSkill, UploadSoftSkill::class.java)
            startActivity(intent)
        }
    }

    private fun searchList(text: String) {
        val searchList = ArrayList<SoftskillData>()
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