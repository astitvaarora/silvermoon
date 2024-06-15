package com.example.silentmoonmeditation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.silentmoonmeditation.R
import com.example.silentmoonmeditation.databinding.FragmentSignUpScreenBinding
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.auth


class SignUpScreen : Fragment() {
    private var _binding : FragmentSignUpScreenBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth : FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001
    private val webClientId: String = "622447181397-82su9cd66gl4e2egi5g624jhu1upcd53.apps.googleusercontent.com"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        FirebaseApp.initializeApp(requireContext())
        _binding = FragmentSignUpScreenBinding.inflate(layoutInflater,container,false)
        return binding.root
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvLogin.setOnClickListener {
            replaceFragment(LoginScreen())
        }


        binding.btnGetStarted.setOnClickListener {
            binding.apply {
                if (edittextName.text.toString().isNotEmpty() &&
                    edittextEmail.text.toString().isNotEmpty() &&
                    edittextPassword.text.toString().isNotEmpty()){
                    registerUser(edittextEmail.text.toString(),edittextPassword.text.toString(),edittextName.text.toString())

                }else{
                    Toast.makeText(requireContext(),"Please fill all fields",Toast.LENGTH_SHORT).show()
                }}

        }


    }

    private fun registerUser(email: String, password: String, name: String) {
        val user = auth.currentUser
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(name)
            .build()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Registration successful", Toast.LENGTH_SHORT).show()
                    replaceFragment(GreetingScreen())
                } else {
                    task.exception?.let {
                        Toast.makeText(requireContext(), "Authentication failed: ${it.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
    }
//    private fun registerUser(email: String, password: String,name:String) {
//
//        auth.createUserWithEmailAndPassword(email, password)
//            .addOnCompleteListener(requireActivity()) { task ->
//                if (task.isSuccessful) {
//                    Toast.makeText(requireContext(), "Registration successful", Toast.LENGTH_SHORT).show()
//                    findNavController().navigate(R.id.action_signUpScreen_to_greetingScreen)
//
//                } else {
//                    Toast.makeText(requireContext(), "Authentication failed.",
//                        Toast.LENGTH_SHORT).show()
//                }
//            }
//    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = fragmentManager?.beginTransaction()
        transaction?.replace(R.id.fragmentCV, fragment)
        transaction?.commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}