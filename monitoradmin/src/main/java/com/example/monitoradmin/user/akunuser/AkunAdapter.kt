package com.example.monitoradmin.user.akunuser

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
class AkunAdapter (private val context: Context, private var dataList: List<DataAkun>) :
    RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycle_item_user, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(context).load(dataList[position].image).into(holder.recImage)
        holder.recUsername.text = dataList[position].username
        holder.recJabatan.text = dataList[position].jabatan
        holder.recNama.text = dataList[position].nama

        holder.recCard.setOnClickListener {
            val intent = Intent(context, DetailAkunUser::class.java).apply {
                putExtra("Username", dataList[holder.adapterPosition].username)
                putExtra("Image", dataList[holder.adapterPosition].image)
                putExtra("Nama", dataList[holder.adapterPosition].nama)
                putExtra("Jabatan", dataList[holder.adapterPosition].jabatan)
                putExtra("Password", dataList[holder.adapterPosition].password)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun searchDataList(searchList: ArrayList<DataAkun>) {
        dataList = searchList
        notifyDataSetChanged()
    }
}

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val recImage: ImageView = itemView.findViewById(R.id.recImage)
    val recCard: CardView = itemView.findViewById(R.id.recCard)
    val recUsername: TextView = itemView.findViewById(R.id.recUsername)
    val recNama: TextView = itemView.findViewById(R.id.recNama)
    val recJabatan: TextView = itemView.findViewById(R.id.recJabatan)
}