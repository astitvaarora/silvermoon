package com.example.silentmoonmeditation.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.silentmoonmeditation.R
import com.example.silentmoonmeditation.adapter.AdapterMeditationScreen
import com.example.silentmoonmeditation.adapter.MyItemClickListener2
import com.example.silentmoonmeditation.databinding.FragmentMeditateBinding
import com.example.silentmoonmeditation.model.MeditateCard
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MeditateFragment : Fragment() ,MyItemClickListener2{
    private var _binding: FragmentMeditateBinding? = null
    private val binding get() = _binding!!
    private var isActive = false
    private lateinit var db : FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        db = FirebaseFirestore.getInstance()
        _binding = FragmentMeditateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val window: Window = requireActivity().window
        window.statusBarColor = resources.getColor(R.color.white)

        lifecycleScope.launch{
            FetchDataMeditate(db)
        }
        binding.allTag.setOnClickListener {
            toggleBackgroundColor(it as Button)
        }
        binding.myTag.setOnClickListener {
            toggleBackgroundColor(it as Button)
        }
        binding.anxiousTag.setOnClickListener {
            toggleBackgroundColor(it as Button)
        }
        binding.sleepTag.setOnClickListener {
            toggleBackgroundColor(it as Button)
        }

    }

    private fun toggleBackgroundColor(btn: Button){
        if(isActive){
            btn.setBackgroundColor(resources.getColor(R.color.meditate_tag_active))
        }else{
            btn.setBackgroundColor(resources.getColor(R.color.meditate_tag_not_active))
        }
        isActive = !isActive

    }

    private suspend fun FetchDataMeditate(db : FirebaseFirestore){
        val docRef = db.collection("SilentMoon").document("MeditationScreen")
        val ls  = mutableListOf<MeditateCard>()

        withContext(Dispatchers.IO){
            docRef.get().addOnSuccessListener {document ->
                if(document.exists()){
                    val data = document.data
                    val autumnVibes = data?.get("autumnVibes") as? Map<*,*>
                    val beachVibes = data?.get("beachVibes") as? Map<*,*>
                    val calmVibes = data?.get("calmVibes") as? Map<*,*>
                    val springVibes = data?.get("springVibes") as? Map<*,*>

                    ls.add(MeditateCard("autumnVibes", autumnVibes?.get("song") as String, autumnVibes?.get("illustration") as String))
                    ls.add(MeditateCard("beachVibes", beachVibes?.get("song") as String,beachVibes?.get("illustration") as String))
                    ls.add(MeditateCard("calmVibes", calmVibes?.get("song") as String,calmVibes?.get("illustration") as String))
                    ls.add(MeditateCard("springVibes", springVibes?.get("song") as String,springVibes?.get("illustration") as String))

                    Log.d("data", "FetchDataMeditate: $beachVibes")

                }

                binding.rvMeditate.apply {
                    setHasFixedSize(true)
                    adapter = AdapterMeditationScreen(ls,this@MeditateFragment)
                    layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                }
            }
        }
    }

    override fun onItemClicked(item: MeditateCard) {
        val bundle = Bundle().apply {
            putString("title",item.title)
            putString("song",item.url)
        }
        val musicFragment = MusicFragment().apply {
            arguments = bundle
        }
        replaceFragment(musicFragment)

    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = fragmentManager?.beginTransaction()
        transaction?.replace(R.id.main_fragment_CV, fragment)
        transaction?.commit()
    }

}
