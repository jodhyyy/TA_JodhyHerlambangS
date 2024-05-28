package com.example.monitoradmin.user.siswa

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
import com.example.monitoradmin.R

@Suppress("DEPRECATION")
class SiswaAdapter(private val context: Context, private var dataList: List<DataSiswa>) :
    RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycle_item_siswa, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(context).load(dataList[position].image).into(holder.recImage)
        holder.recNis.text = dataList[position].nis
        holder.recJabatan.text = dataList[position].jabatan
        holder.recNama.text = dataList[position].nama

        holder.recCardSiswa.setOnClickListener {
            val intent = Intent(context, DetailSiswa::class.java).apply {
                putExtra("Nama", dataList[holder.adapterPosition].nama)
                putExtra("Nis", dataList[holder.adapterPosition].nis)
                putExtra("Image", dataList[holder.adapterPosition].image)
                putExtra("Jabatan", dataList[holder.adapterPosition].jabatan)
                putExtra("NamaWali", dataList[holder.adapterPosition].namaWali)
                putExtra("Alamat", dataList[holder.adapterPosition].alamat)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun searchDataList(searchList: ArrayList<DataSiswa>) {
        dataList = searchList
        notifyDataSetChanged()
    }
}

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val recImage: ImageView = itemView.findViewById(R.id.recImage)
    val recCardSiswa: CardView = itemView.findViewById(R.id.recCardSiswa)
    val recNama: TextView = itemView.findViewById(R.id.recNama)
    val recNis: TextView = itemView.findViewById(R.id.recNis)
    val recJabatan: TextView = itemView.findViewById(R.id.recJabatan)
}