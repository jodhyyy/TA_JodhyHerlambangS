package com.example.monitoradmin.user.guru

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
class GuruAdapter(private val context: Context, private var dataList: List<DataGuru>) :
    RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycle_item_guru, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(context).load(dataList[position].image).into(holder.recImage)
        holder.recNip.text = dataList[position].nip
        holder.recJabatan.text = dataList[position].jabatan
        holder.recNama.text = dataList[position].nama

        holder.recCardGuru.setOnClickListener {
            val intent = Intent(context, DetailGuru::class.java).apply {
                putExtra("Nama", dataList[holder.adapterPosition].nama)
                putExtra("Nip", dataList[holder.adapterPosition].nip)
                putExtra("Image", dataList[holder.adapterPosition].image)
                putExtra("Jabatan", dataList[holder.adapterPosition].jabatan)
                putExtra("NoHp", dataList[holder.adapterPosition].noHp)
                putExtra("Alamat", dataList[holder.adapterPosition].alamat)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun searchDataList(searchList: ArrayList<DataGuru>) {
        dataList = searchList
        notifyDataSetChanged()
    }
}

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val recImage: ImageView = itemView.findViewById(R.id.recImage)
    val recCardGuru: CardView = itemView.findViewById(R.id.recCardGuru)
    val recNama: TextView = itemView.findViewById(R.id.recNama)
    val recNip: TextView = itemView.findViewById(R.id.recNip)
    val recJabatan: TextView = itemView.findViewById(R.id.recJabatan)
}