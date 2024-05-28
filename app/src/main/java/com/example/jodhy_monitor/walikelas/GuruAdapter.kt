package com.example.jodhy_monitor.walikelas

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

class GuruAdapter(private val context: Context, private var dataList: List<DataGuru>) :
    RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycle_item_guru, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(context).load(dataList[position].image).into(holder.recImage)
        holder.recNip.text = dataList[position].nip
        holder.recnoHp.text = dataList[position].noHp
        holder.recNama.text = dataList[position].nama
        holder.recAlamat.text = dataList[position].alamat

        holder.recCardGuru.setOnClickListener {

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
    val recnoHp: TextView = itemView.findViewById(R.id.recnoHp)
    val recAlamat: TextView = itemView.findViewById(R.id.recAlamat)
}