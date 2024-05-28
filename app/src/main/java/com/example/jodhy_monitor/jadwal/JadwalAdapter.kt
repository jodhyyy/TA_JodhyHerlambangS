package com.example.jodhy_monitor.jadwal

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.jodhy_monitor.R

class JadwalAdapter(private val context: Context, private var dataList: List<DataJadwal>) :
    RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycle_item_jadwal, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(context).load(dataList[position].image).into(holder.recImage)
        holder.recMatpel.text = dataList[position].mataPelajaran
        holder.recHari.text = dataList[position].hari
        holder.recJam.text = dataList[position].jam
        holder.recNamaGuru.text = dataList[position].namaGuru

        holder.recCardJadwal.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun searchDataList(searchList: ArrayList<DataJadwal>) {
        dataList = searchList
        notifyDataSetChanged()
    }
}

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val recImage: ImageView = itemView.findViewById(R.id.recImage)
    val recCardJadwal: CardView = itemView.findViewById(R.id.recCardJadwal)
    val recMatpel: TextView = itemView.findViewById(R.id.recMatpel)
    val recHari: TextView = itemView.findViewById(R.id.recHari)
    val recJam: TextView = itemView.findViewById(R.id.recJam)
    val recNamaGuru: TextView = itemView.findViewById(R.id.recNamaGuru)
}