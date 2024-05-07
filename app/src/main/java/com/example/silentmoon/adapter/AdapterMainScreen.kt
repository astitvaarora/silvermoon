package com.example.silentmoon.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.silentmoon.databinding.LayoutCardMainScreenBinding
import com.example.silentmoon.model.MainScreenCard

class AdapterMainScreen(private val ls:List<MainScreenCard>):RecyclerView.Adapter<AdapterMainScreen.ViewHolder>(){
    class ViewHolder(val binding:LayoutCardMainScreenBinding):RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutCardMainScreenBinding.inflate(LayoutInflater.from(parent.context),parent,false))


    override fun getItemCount(): Int {
        return ls.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = ls[position]
        with(holder){
            binding.apply{
                cardImg.setBackgroundResource(item.img)
                rvCardTitle.text = item.title
                rvCardTimetv.text = item.time
            }
        }
    }
}