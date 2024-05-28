package com.example.guru.nilai

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
class NilaiAdapter(private val context: Context, private var dataList: List<DataNilai>) :
    RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycle_item_nilai, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(context).load(dataList[position].image).into(holder.recImage)
        holder.recNama.text = dataList[position].nama

        holder.recCardNilai.setOnClickListener {
            val intent = Intent(context, DetailNilai::class.java).apply {
                putExtra("Nama", dataList[holder.adapterPosition].nama)
                putExtra("Topik", dataList[holder.adapterPosition].topik)
                putExtra("Image", dataList[holder.adapterPosition].image)
                putExtra("Tanggal", dataList[holder.adapterPosition].tanggal)
                putExtra("Nilai", dataList[holder.adapterPosition].nilai)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun searchDataList(searchList: ArrayList<DataNilai>) {
        dataList = searchList
        notifyDataSetChanged()
    }
}

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val recImage: ImageView = itemView.findViewById(R.id.recImage)
    val recCardNilai: CardView = itemView.findViewById(R.id.recCardNilai)
    val recNama: TextView = itemView.findViewById(R.id.recNama)
}