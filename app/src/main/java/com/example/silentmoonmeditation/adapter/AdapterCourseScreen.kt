package com.example.silentmoonmeditation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.silentmoonmeditation.databinding.CourseSongListLayoutBinding
import com.example.silentmoonmeditation.model.CourseCard
import com.example.silentmoonmeditation.model.SleepHomeCard

class AdapterCourseScreen(
    private val ls  : List<CourseCard>,
    private val listener : MyItemClickListener

):RecyclerView.Adapter<AdapterCourseScreen.ViewHolder>(){
    class ViewHolder(val binding: CourseSongListLayoutBinding):RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        CourseSongListLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun getItemCount(): Int {
        return ls.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = ls[position]
        with(holder){
            binding.title.text = item.title
        }
        holder.itemView.setOnClickListener {
            listener.onItemClicked(item)
        }

    }
}

interface MyItemClickListener {
    fun onItemClicked(item: CourseCard)
}