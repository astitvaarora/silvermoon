package com.example.silentmoonmeditation.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.silentmoonmeditation.R
import com.example.silentmoonmeditation.adapter.AdapterCourseScreen
import com.example.silentmoonmeditation.adapter.MyItemClickListener
import com.example.silentmoonmeditation.databinding.FragmentCourseBinding
import com.example.silentmoonmeditation.model.CourseCard
import com.example.silentmoonmeditation.model.SleepHomeCard
import com.google.firebase.firestore.FirebaseFirestore

class CourseFragment : Fragment(),MyItemClickListener {
    private var _binding: FragmentCourseBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        db = FirebaseFirestore.getInstance()
        _binding = FragmentCourseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val docRef = db.collection("SilentMoon").document("MainScreen")
        docRef.get().addOnSuccessListener {
            val map = it.data?.get("basicCourse") as? Map<*, *>
            if(map!=null){
                val courseData = map["insideCourse"] as? Map<*,*>
                val img = courseData?.get("illustration")
                Log.d("ni3o", "onViewCreated: $img")
                Glide.with(requireContext()).load(img.toString()).into(binding.ivTop)
                val media1 = courseData?.get("song1")
                val media2 = courseData?.get("song2")
                val media3 = courseData?.get("song3")
                Log.d("coursesong", "onViewCreated: $media1")
                val ls = listOf(
                    CourseCard("Focus Attention","10 MIN", media1.toString()),
                    CourseCard("Body Scan","10 MIN",media2.toString()),
                    CourseCard("Making Happiness","10 MIN",media3.toString()),
                )
                binding.rvCourse.apply {
                    adapter = AdapterCourseScreen(ls,this@CourseFragment)
                    layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
                }
            }
        }

    }

    override fun onItemClicked(item: CourseCard) {
        val bundle = Bundle().apply {
            putString("song",item.url)
            putString("title",item.title)
        }
        val fragment = MusicFragment().apply {
            arguments = bundle
        }
        replaceFragment(fragment)

    }
    private fun replaceFragment(fragment: Fragment) {
        val transaction = fragmentManager?.beginTransaction()
        transaction?.replace(R.id.main_fragment_CV, fragment)
        transaction?.commit()
    }

}

