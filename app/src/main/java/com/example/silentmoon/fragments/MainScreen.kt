package com.example.silentmoon.fragments

import android.graphics.drawable.GradientDrawable.Orientation
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.silentmoon.R
import com.example.silentmoon.adapter.AdapterMainScreen
import com.example.silentmoon.databinding.FragmentMainScreenBinding
import com.example.silentmoon.model.MainScreenCard


class MainScreen : Fragment() {
    private var _binding:FragmentMainScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMainScreenBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val ls = listOf<MainScreenCard>(
            MainScreenCard(R.drawable.rv_card1_background,"Focus","3-10 MIN"),
            MainScreenCard(R.drawable.rv_card2_background,"Meditate","3-10 MIN"),
            MainScreenCard(R.drawable.rv_card1_background,"Focus","3-10 MIN"),
            MainScreenCard(R.drawable.rv_card2_background,"Meditate","3-10 MIN"),
            MainScreenCard(R.drawable.rv_card1_background,"Focus","3-10 MIN"),

        )
        binding.apply {
            rvMainScreen.adapter = AdapterMainScreen(ls)
            rvMainScreen.layoutManager = LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
        }

    }

}