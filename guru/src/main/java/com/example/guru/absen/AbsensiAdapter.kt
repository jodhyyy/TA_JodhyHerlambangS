package com.example.guru.absen

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
import com.example.guru.R

@Suppress("DEPRECATION")
class AbsensiAdapter(private val context: Context, private var dataList: List<DataAbsensi>) :
    RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycle_item_absensi, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(context).load(dataList[position].image).into(holder.recImage)
        holder.recNama.text = dataList[position].nama
        holder.recNis.text = dataList[position].nis

        holder.recCardAbsensi.setOnClickListener {
            val intent = Intent(context, DetailAbsensi::class.java).apply {
                putExtra("Nama", dataList[holder.adapterPosition].nama)
                putExtra("Nis", dataList[holder.adapterPosition].nis)
                putExtra("Image", dataList[holder.adapterPosition].image)
                putExtra("Absensi", dataList[holder.adapterPosition].absensi)
                putExtra("Tanggal", dataList[holder.adapterPosition].tanggal)
                putExtra("Keterangan", dataList[holder.adapterPosition].keterangan)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun searchDataList(searchList: ArrayList<DataAbsensi>) {
        dataList = searchList
        notifyDataSetChanged()
    }
}

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val recImage: ImageView = itemView.findViewById(R.id.recImage)
    val recCardAbsensi: CardView = itemView.findViewById(R.id.recCardAbsensi)
    val recNama: TextView = itemView.findViewById(R.id.recNama)
    val recNis: TextView = itemView.findViewById(R.id.recNis)
}