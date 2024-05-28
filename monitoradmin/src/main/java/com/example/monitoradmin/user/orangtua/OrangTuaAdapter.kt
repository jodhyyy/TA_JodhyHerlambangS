package com.example.monitoradmin.user.orangtua

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
class OrangTuaAdapter(private val context: Context, private var dataList: List<DataOrangTua>) :
    RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycle_item_orangtua, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(context).load(dataList[position].image).into(holder.recImage)
        holder.recPekerjaan.text = dataList[position].pekerjaan
        holder.recJabatan.text = dataList[position].jabatan
        holder.recNama.text = dataList[position].nama

        holder.recCardOrtu.setOnClickListener {
            val intent = Intent(context, DetailOrangTua::class.java).apply {
                putExtra("Nama", dataList[holder.adapterPosition].nama)
                putExtra("NamaSiswa", dataList[holder.adapterPosition].namaSiswa)
                putExtra("Image", dataList[holder.adapterPosition].image)
                putExtra("Jabatan", dataList[holder.adapterPosition].jabatan)
                putExtra("Pekerjaan", dataList[holder.adapterPosition].pekerjaan)
                putExtra("Alamat", dataList[holder.adapterPosition].alamat)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun searchDataList(searchList: ArrayList<DataOrangTua>) {
        dataList = searchList
        notifyDataSetChanged()
    }
}

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val recImage: ImageView = itemView.findViewById(R.id.recImage)
    val recCardOrtu: CardView = itemView.findViewById(R.id.recCardOrtu)
    val recNama: TextView = itemView.findViewById(R.id.recNama)
    val recPekerjaan: TextView = itemView.findViewById(R.id.recPekerjaan)
    val recJabatan: TextView = itemView.findViewById(R.id.recJabatan)
}