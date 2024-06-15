package com.example.silentmoonmeditation.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.silentmoonmeditation.R
import com.example.silentmoonmeditation.databinding.FragmentLoginScreenBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth

class LoginScreen : Fragment() {

    private var _binding: FragmentLoginScreenBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth : FirebaseAuth
    private val RC_SIGN_IN = 9001
    private val webClientId: String = "622447181397-82su9cd66gl4e2egi5g624jhu1upcd53.apps.googleusercontent.com"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        FirebaseApp.initializeApp(requireContext())
        _binding = FragmentLoginScreenBinding.inflate(layoutInflater,container,false)
        return binding.root
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvSignUp.setOnClickListener {
            replaceFragment(SignUpScreen())
        }

        binding.btnLogin.setOnClickListener {
            binding.apply {
                if(edittextEmail.text.toString().isNotEmpty() && edittextPassword.text.toString().isNotEmpty()){
                    loginUser(edittextEmail.text.toString(),edittextPassword.text.toString())
                }else{
                    Toast.makeText(requireContext(),"Please fill all the fields",Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnGoogleLogin.setOnClickListener {
            googleSignIn()
        }



        binding.tvSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_loginScreen_to_signUpScreen)
        }
        binding.btnLogin.setOnClickListener {
            if (binding.edittextEmail.text.toString().isNotEmpty() && binding.edittextPassword.text.toString().isNotEmpty()){
                loginUser(binding.edittextEmail.text.toString(),binding.edittextPassword.text.toString())
            }else{
                Toast.makeText(requireContext(),"Please fill all the fields",Toast.LENGTH_SHORT).show()
            }

        }
    }
    fun loginUser(email: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Login successful, handle the authenticated user
                    val user = FirebaseAuth.getInstance().currentUser
                    val displayName = user?.email ?: "User not found"
                    Toast.makeText(requireContext(), "Signed in as $displayName", Toast.LENGTH_SHORT).show()
                    replaceFragment(GreetingScreen())
                    //findNavController().navigate(R.id.action_logInFragment_to_homeFragment)
                } else {
                    // Login failed, handle the error
                    Toast.makeText(requireContext(), "User not found" , Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun googleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(webClientId)
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent,RC_SIGN_IN)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
                findNavController().navigate(R.id.action_loginScreen_to_greetingScreen)
            } catch (e: ApiException) {
                Toast.makeText(requireContext(), "google sign in failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(requireContext(), "Signed in as ${user?.displayName}", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

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