package com.example.jodhy_monitor.walikelas

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.jodhy_monitor.R
import com.example.jodhy_monitor.databinding.FragmentWaliKelasBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Locale

class WaliKelas : Fragment() {
    private var _binding: FragmentWaliKelasBinding? = null
    private val binding get() = _binding!!
    private lateinit var databaseReference: DatabaseReference
    private lateinit var eventListener: ValueEventListener
    private lateinit var dataList: MutableList<DataGuru>
    private lateinit var adapter: GuruAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentWaliKelasBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = binding.recyclerView
        val searchView = binding.search

        recyclerView.layoutManager = GridLayoutManager(requireContext(), 1)

        val builder = AlertDialog.Builder(requireContext())
        builder.setCancelable(false)
        builder.setView(R.layout.progress_layout)
        val dialog = builder.create()

        dataList = ArrayList()
        adapter = GuruAdapter(requireContext(), dataList)
        recyclerView.adapter = adapter

        databaseReference = FirebaseDatabase.getInstance().getReference("DetailGuru")

        dialog.show()

        eventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dataList.clear()
                for (itemSnapshot in snapshot.children) {
                    val dataClass = itemSnapshot.getValue(DataGuru::class.java)
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

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
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
        val searchList = ArrayList<DataGuru>()
        for (dataClass in dataList) {
            dataClass.nama?.let {
                if (it.toLowerCase(Locale.ROOT).contains(text.toLowerCase())) {
                    searchList.add(dataClass)
                }
            }
        }
        adapter.searchDataList(searchList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        databaseReference.removeEventListener(eventListener)
    }
}