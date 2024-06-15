package com.example.silentmoonmeditation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.silentmoonmeditation.databinding.SleepHomeRvCardLayoutBinding
import com.example.silentmoonmeditation.model.MeditateCard
import com.example.silentmoonmeditation.model.SleepHomeCard

class AdapterSleepHome(
    private val ls:List<SleepHomeCard>,
    private val listner : MyItemClickListener3

):RecyclerView.Adapter<AdapterSleepHome.ViewHolder>(){
    class ViewHolder(val binding:SleepHomeRvCardLayoutBinding):RecyclerView.ViewHolder(binding.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        SleepHomeRvCardLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
    )



    override fun getItemCount(): Int {
        return ls.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = ls[position]
        with(holder){
            Glide.with(holder.itemView.context).load(item.img).into(binding.ivSleepHomeRvCardImg)
            binding.apply {
                tvTitle.text = item.title
                tvDesc.text = item.desc
            }
        }
        holder.itemView.setOnClickListener{
            listner.onItemClicked(item)
        }
    }
}

interface MyItemClickListener3 {
    fun onItemClicked(item: SleepHomeCard)

}