import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.silentmoonmeditation.R
import com.example.silentmoonmeditation.adapter.AdapterSleepHome
import com.example.silentmoonmeditation.adapter.MyItemClickListener3
import com.example.silentmoonmeditation.databinding.FragmentSleepHomeScreenBinding
import com.example.silentmoonmeditation.fragments.MusicFragment
import com.example.silentmoonmeditation.model.SleepHomeCard
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SleepHomeScreen : Fragment(),MyItemClickListener3 {
    private var _binding: FragmentSleepHomeScreenBinding? = null
    private val binding get() = _binding!!
    private lateinit var db : FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        db = FirebaseFirestore.getInstance()
        _binding = FragmentSleepHomeScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            getSleepHomeData(db)
        }

        binding.cardStartButton.setOnClickListener {
            val fetch = db.collection("SilentMoon").document("SleepScreen")
            fetch.get().addOnSuccessListener { document ->
                val data = document.data
                val home = data?.get("home") as Map<*, *>
                val song = home["song"] as String
                val bundle = Bundle().apply {
                    putString("title","Sleep Song")
                    putString("song",song)
                }
                val musicFragment = MusicFragment().apply {
                    arguments = bundle
                }
                replaceFragment(musicFragment)



            }
        }

    }

    private suspend fun getSleepHomeData(db : FirebaseFirestore) {
        val docRef = db.collection("SilentMoon").document("SleepScreen")
        val ls = mutableListOf<SleepHomeCard>()

        withContext(Dispatchers.IO){
            docRef.get().addOnSuccessListener { docuemnt ->
                if(docuemnt.exists()){
                    val data = docuemnt.data
                    val home = data?.get("home") as Map<*, *>
                    val deepSleep = home["deep-sleep"] as Map<*, *>
                    val nightIsland = home["night-island"] as Map<*, *>
                    val sweetSleep = home["sweet-sleep"] as Map<*, *>
                    val thoughtLess = home["thought-less"] as Map<*, *>

                    val img1 = deepSleep["illustration"] as String
                    val img2 = nightIsland["illustration"] as String
                    val img3 = sweetSleep["illustration"] as String
                    val img4 = thoughtLess["illustration"] as String

                    Log.d("noeneo2", "getSleepHomeData: $img1")

                    ls.add(SleepHomeCard(img1,"Deep Sleep","3 min",home["song"] as String))
                    ls.add(SleepHomeCard(img2,"Night Island","3 min",home["song"] as String))
                    ls.add(SleepHomeCard(img3,"Sweet Sleep","3 min",home["song"] as String))
                    ls.add(SleepHomeCard(img4,"Thought Less","3 min",home["song"] as String))

                    val img = home["illustration"] as String
                    loadImageIntoBackground(img,binding.cardSleepHome)

                }
                binding.rvSleephome.apply {
                    setHasFixedSize(true)
                    layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                    adapter = AdapterSleepHome(ls,this@SleepHomeScreen)
                }
            }
        }

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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = fragmentManager?.beginTransaction()
        transaction?.replace(R.id.main_fragment_CV, fragment)
        transaction?.commit()
    }

    override fun onItemClicked(item: SleepHomeCard) {
        val bundle = Bundle().apply {
            putString("title",item.title)
            putString("song",item.song)
        }
        val musicFragment = MusicFragment().apply {
            arguments = bundle
        }
        replaceFragment(musicFragment)

    }
}
