package com.example.monitoradmin.softskill

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
class SoftSkillAdapter(private val context: Context, private var dataList: List<SoftskillData>) :
    RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycle_item_softskill, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(context).load(dataList[position].image).into(holder.recImage)
        holder.recNama.text = dataList[position].nama
        holder.recNis.text = dataList[position].nis

        holder.recCardSoftskill.setOnClickListener {
            val intent = Intent(context, DetailSoftSkill::class.java).apply {
                putExtra("Nama", dataList[holder.adapterPosition].nama)
                putExtra("Nis", dataList[holder.adapterPosition].nis)
                putExtra("Image", dataList[holder.adapterPosition].image)
                putExtra("Softskill", dataList[holder.adapterPosition].softSkill)
                putExtra("Keterangan", dataList[holder.adapterPosition].keterangan)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun searchDataList(searchList: ArrayList<SoftskillData>) {
        dataList = searchList
        notifyDataSetChanged()
    }
}

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val recImage: ImageView = itemView.findViewById(R.id.recImage)
    val recCardSoftskill: CardView = itemView.findViewById(R.id.recCardSoftskill)
    val recNama: TextView = itemView.findViewById(R.id.recNama)
    val recNis: TextView = itemView.findViewById(R.id.recNis)
}