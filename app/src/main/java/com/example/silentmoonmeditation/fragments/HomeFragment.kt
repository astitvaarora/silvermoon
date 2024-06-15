package com.example.silentmoonmeditation.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import com.example.silentmoonmeditation.R
import com.example.silentmoonmeditation.databinding.FragmentHomeBinding
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val window: Window = requireActivity().window
        window.statusBarColor = resources.getColor(R.color.white)
        val bottomNav = binding.bottomNav
        replaceFragment(MainScreen())

        bottomNav.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home_icon -> {
                    replaceFragment(MainScreen())
                    bottomNav.setBackgroundColor(resources.getColor(R.color.white))
                }
                R.id.sleep_icon -> {
                    replaceFragment(WelcomeSleepScreen())
                    bottomNav.setBackgroundColor(resources.getColor(R.color.sleepcolor))
                }
                R.id.meditate_icon -> {
                    replaceFragment(MeditateFragment())
                    bottomNav.setBackgroundColor(resources.getColor(R.color.white))
                }

                R.id.music_icon -> {
                    val bundle = Bundle().apply {
                        putString("title", "relaxation")
                        putString("song","https://firebasestorage.googleapis.com/v0/b/silentmoonmeditation.appspot.com/o/Songs%2Fautumn-vibe.mp3?alt=media&token=9cc23d92-9105-4a65-9761-6839a987171a")
                    }
                    val fragment = MusicFragment().apply {
                        arguments = bundle
                    }
                    replaceFragment(fragment)
                    bottomNav.setBackgroundColor(resources.getColor(R.color.white))

                }
                R.id.profile_icon -> {
                    replaceFragment(ProfileFragment())
                    bottomNav.setBackgroundColor(resources.getColor(R.color.white))
                }
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = fragmentManager?.beginTransaction()
        transaction?.replace(binding.mainFragmentCV.id, fragment)
        transaction?.commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
