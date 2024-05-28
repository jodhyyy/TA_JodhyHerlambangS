package com.example.monitoradmin.user.akunuser

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.monitoradmin.R
import com.example.monitoradmin.databinding.ActivityAkunUserBinding
import com.example.monitoradmin.user.User
import com.google.firebase.database.*
import java.util.*

class AkunUser : AppCompatActivity() {
    private lateinit var binding: ActivityAkunUserBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var eventListener: ValueEventListener
    private lateinit var dataList: MutableList<DataAkun>
    private lateinit var adapter: AkunAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAkunUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            val intent = Intent(this@AkunUser, User::class.java)
            startActivity(intent)
            finish()
        }

        val recyclerView = binding.recyclerView
        val fab = binding.fab
        val searchView = binding.search

        recyclerView.layoutManager = GridLayoutManager(this@AkunUser, 1)

        val builder = AlertDialog.Builder(this@AkunUser)
        builder.setCancelable(false)
        builder.setView(R.layout.progress_layout)
        val dialog = builder.create()

        dataList = ArrayList()
        adapter = AkunAdapter(this@AkunUser, dataList)
        recyclerView.adapter = adapter

        databaseReference = FirebaseDatabase.getInstance().getReference("AkunUser")

        dialog.show()

        eventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dataList.clear()
                for (itemSnapshot in snapshot.children) {
                    val dataClass = itemSnapshot.getValue(DataAkun::class.java)
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
            val intent = Intent(this@AkunUser, UploadAkunUser::class.java)
            startActivity(intent)
        }
    }

    private fun searchList(text: String) {
        val searchList = ArrayList<DataAkun>()
        for (dataClass in dataList) {
            dataClass.username?.let {
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