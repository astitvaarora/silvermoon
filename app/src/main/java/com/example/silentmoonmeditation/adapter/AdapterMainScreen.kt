package com.example.silentmoonmeditation.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.silentmoonmeditation.databinding.LayoutCardMainScreenBinding
import com.example.silentmoonmeditation.fragments.MainScreen
import com.example.silentmoonmeditation.model.MainScreenCard

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
                //cardImg.setBackgroundResource(item.img)
                Glide.with(holder.itemView.context).load(item.img).into(cardImg)
                rvCardTitle.text = item.title
                rvCardTimetv.text = item.time
            }
        }
    }
}