package com.example.silentmoonmeditation.fragments

import SleepHomeScreen
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.example.silentmoonmeditation.R
import com.example.silentmoonmeditation.databinding.FragmentWelcomeSleepScreenBinding

class WelcomeSleepScreen : Fragment() {
    private var _binding :FragmentWelcomeSleepScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWelcomeSleepScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val window: Window = requireActivity().window
        window.statusBarColor = resources.getColor(R.color.sleepcolor)

        binding.btnGetStarted.setOnClickListener {
            replaceFragment(SleepHomeScreen())

        }

//
//        val nestedNavHostFragment = childFragmentManager.findFragmentById(R.id.welcomeSleepScreen) as NavHostFragment
//        val nestedNavController = nestedNavHostFragment.navController


    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = fragmentManager?.beginTransaction()
        transaction?.replace(R.id.main_fragment_CV, fragment)
        transaction?.commit()
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }


}