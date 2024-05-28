package com.example.monitoradmin.matpel

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
import com.example.monitoradmin.user.siswa.DetailSiswa

@Suppress("DEPRECATION")
class MatpelAdapter(private val context: Context, private var dataList: List<MatpelData>) :
    RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycle_item_matpel, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(context).load(dataList[position].image).into(holder.recImage)
        holder.recMataPelajaran.text = dataList[position].mataPelajaran

        holder.recCardMatpel.setOnClickListener {
            val intent = Intent(context, DetailMatpel::class.java).apply {
                putExtra("MataPelajaran", dataList[holder.adapterPosition].mataPelajaran)
                putExtra("Image", dataList[holder.adapterPosition].image)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun searchDataList(searchList: ArrayList<MatpelData>) {
        dataList = searchList
        notifyDataSetChanged()
    }
}

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val recImage: ImageView = itemView.findViewById(R.id.recImage)
    val recCardMatpel: CardView = itemView.findViewById(R.id.recCardMatpel)
    val recMataPelajaran: TextView = itemView.findViewById(R.id.recMataPelajaran)
}