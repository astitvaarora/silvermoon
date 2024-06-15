package com.example.silentmoonmeditation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import com.example.silentmoonmeditation.R
import com.example.silentmoonmeditation.databinding.FragmentProfileBinding
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        FirebaseApp.initializeApp(requireActivity())
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val window: Window = requireActivity().window
        window.statusBarColor = resources.getColor(R.color.white)

        binding.ivForwardPersonalInformation.setOnClickListener {
            replaceFragment(EditProfileFragment())
        }


        binding.tvUserName.text = auth.currentUser?.displayName

        binding.ivLogout.setOnClickListener {
            Toast.makeText(requireActivity(), "Logout", Toast.LENGTH_SHORT).show()
            auth.signOut()
            replaceFragment(LoginScreen())
        }

    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = fragmentManager?.beginTransaction()
        transaction?.replace(R.id.main_fragment_CV, fragment)
        transaction?.commit()
    }

}