package com.example.silentmoonmeditation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.silentmoonmeditation.databinding.MeditationCardBinding
import com.example.silentmoonmeditation.model.CourseCard
import com.example.silentmoonmeditation.model.MeditateCard

class AdapterMeditationScreen(
    private val ls: MutableList<MeditateCard>,
    private val listener : MyItemClickListener2

):RecyclerView.Adapter<AdapterMeditationScreen.ViewHolder>(){
    class ViewHolder(val binding:MeditationCardBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        MeditationCardBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun getItemCount(): Int {
        return ls.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = ls[position]
        with(holder.binding){
            Glide.with(holder.itemView.context).load(item.img).into(ivCard)
        }
        holder.itemView.setOnClickListener {
            listener.onItemClicked(item)
        }
    }
}

interface MyItemClickListener2 {
    fun onItemClicked(item: MeditateCard)

}