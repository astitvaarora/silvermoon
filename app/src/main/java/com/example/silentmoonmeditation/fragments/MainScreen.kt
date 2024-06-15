package com.example.silentmoonmeditation.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.silentmoonmeditation.R
import com.example.silentmoonmeditation.adapter.AdapterMainScreen
import com.example.silentmoonmeditation.databinding.FragmentMainScreenBinding
import com.example.silentmoonmeditation.model.MainScreenCard
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Delay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainScreen : Fragment() {
    private var _binding:FragmentMainScreenBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        db = FirebaseFirestore.getInstance()
        _binding = FragmentMainScreenBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        lifecycleScope.launch {
            fetchDataRV(db)
            fetchDataBasicCourse(db)
            fetchDataRelaxation(db)
            fetchDataDailyThought(db)
        }

        val window: Window = requireActivity().window
        window.statusBarColor = resources.getColor(R.color.white)

        binding.card1Btn.setOnClickListener {
            replaceFragment(CourseFragment())
        }

        binding.btnThought.setOnClickListener {
            val bundle = Bundle().apply {
                putString("title","Daily Thought")
                putString("song","https://firebasestorage.googleapis.com/v0/b/silentmoonmeditation.appspot.com/o/Songs%2Fthought.mp3?alt=media&token=7931a816-5e4e-4d60-ae67-06be4fb684d2")
            }
            val fragment = MusicFragment().apply {
                arguments = bundle
            }
            replaceFragment(fragment)

        }

    }
    private fun fetchDataMainScreen(db:FirebaseFirestore): DocumentReference {
        val docRef = db.collection("SilentMoon").document("MainScreen")
        docRef.get().addOnSuccessListener {document->
            if(document.exists()){
                Log.d("ne2nf2", "fetchDataMainScreen:${document.data} ")
            }else{
                Log.d("ne2nf2", "fetchData: Main Screen Data Failed")
            }

        }.addOnFailureListener {
            Log.d("Firestore", "Error getting document")
        }
        return docRef
    }
    private fun fetchDataBasicCourse(db:FirebaseFirestore){
        val docRef = db.collection("SilentMoon").document("MainScreen")
        docRef.get().addOnSuccessListener {document->
            if(document.exists()){
                val data = document.data
                val basicCourse = data?.get("basicCourse") as? Map<*, *>
                if(basicCourse!=null){
                    val url = basicCourse.get("illustration") as? String
                    loadImageIntoBackground(url!!,binding.card1)
                }
            }else{
                Log.d("ploki", "fetchData: Main Screen Data Failed")
            }
        }
    }
    private suspend fun fetchDataRV(db:FirebaseFirestore) {
        val docRef = db.collection("SilentMoon").document("MainScreen")
        val ls = mutableListOf<MainScreenCard>()

        withContext(Dispatchers.IO){
            docRef.get().addOnSuccessListener {document->
                Log.d("www", "fetchDataRV: 104")
                if(document.exists()){
                    val data = document.data
                    val happiness = data?.get("happiness") as? Map<*, *>
                    val focus  = data?.get("focus") as? Map<*, *>
                    if(happiness!=null){
                        val url = happiness["illustration"] as? String
                        Log.d("jpwo", "fetchDataRV: $url")
                        ls.add(MainScreenCard(url!!,"Happiness","3-10 MIN"))
                    }
                    if(focus!=null){
                        val url = focus["illustration"] as? String
                        ls.add(MainScreenCard(url!!,"Focus","3-10 MIN"))

                    }
                    binding.apply {
                        Log.d("www", "fetchDataRV: 120")
                        rvMainScreen.adapter = AdapterMainScreen(ls)
                        rvMainScreen.layoutManager = LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
                    }
                }else{
                    Log.d("ploki", "fetchData: Main Screen Data Failed")
                }
            }
        }
    }
    private fun fetchDataRelaxation(db:FirebaseFirestore){
        val docRef = db.collection("SilentMoon").document("MainScreen")
        docRef.get().addOnSuccessListener {document->
            if(document.exists()){
                val data = document.data
                val relaxation = data?.get("relaxation") as? Map<*, *>
                if(relaxation!=null){
                    val url = relaxation.get("illustration") as? String
                    loadImageIntoBackground(url!!,binding.card2)
                    val song = relaxation["song"] as? String
                            binding.card2btn.setOnClickListener {
                            val bundle = Bundle().apply {
                                putString("song",song)
                                putString("title","Relaxation Music")
                            }
                            val fragment = MusicFragment().apply {
                                arguments = bundle
                            }
                            replaceFragment(fragment)
                        }
                }
            }else{
                Log.d("ploki", "fetchData: Main Screen Data Failed")
            }
        }
    }
    private fun fetchDataDailyThought(db:FirebaseFirestore){
        val docRef = db.collection("SilentMoon").document("MainScreen")
        docRef.get().addOnSuccessListener {document->
            if(document.exists()){
                val data = document.data
                val dailyThought = data?.get("dailyThought") as? Map<*, *>
                if(dailyThought!=null){
                    val url = dailyThought.get("illustration") as? String
                    loadImageIntoBackground(url!!,binding.card3)
                }
            }else{
                Log.d("ploki", "fetchData: Main Screen Data Failed")
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = fragmentManager?.beginTransaction()
        transaction?.replace(R.id.main_fragment_CV, fragment)
        transaction?.commit()
    }

    private fun loadImageIntoBackground(url: String, layout: ConstraintLayout) {
        Glide.with(this)
            .load(url)
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    layout.background = resource
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // Handle the placeholder if needed
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    Toast.makeText(requireContext(), "Failed to load image", Toast.LENGTH_SHORT).show()
                }
            })
    }




}