package com.example.silentmoonmeditation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.silentmoonmeditation.R
import com.example.silentmoonmeditation.databinding.FragmentWelcomeScreenBinding
import com.google.firebase.firestore.FirebaseFirestore

class WelcomeScreen : Fragment() {
    private var _binding : FragmentWelcomeScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentWelcomeScreenBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginTV.setOnClickListener {
            it.findNavController().navigate(R.id.action_welcomeScreen_to_loginScreen)
        }
        binding.signinBtn.setOnClickListener {
            it.findNavController().navigate(R.id.action_welcomeScreen_to_signUpScreen)
        }
    }

}