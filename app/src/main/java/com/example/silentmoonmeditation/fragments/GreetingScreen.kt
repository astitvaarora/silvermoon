package com.example.silentmoonmeditation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import com.example.silentmoonmeditation.R
import com.example.silentmoonmeditation.databinding.FragmentGreetingScreenBinding

class GreetingScreen : Fragment() {
    private var _binding:FragmentGreetingScreenBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentGreetingScreenBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.blue)

        binding.btnGetStarted.setOnClickListener {
            replaceFragment(HomeFragment())
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = fragmentManager?.beginTransaction()
        transaction?.replace(R.id.fragmentCV, fragment)
        transaction?.commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Reset status bar color
        activity?.window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.white)
        _binding = null
    }

}