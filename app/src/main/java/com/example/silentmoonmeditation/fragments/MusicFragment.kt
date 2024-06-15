package com.example.silentmoonmeditation.fragments

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import com.example.silentmoonmeditation.R
import com.example.silentmoonmeditation.databinding.FragmentMusicBinding
import android.os.Handler;
import android.provider.Settings
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.example.silentmoonmeditation.RetrofitApi.apiService
import com.example.silentmoonmeditation.model.EyeScanningRequest
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream


class MusicFragment : Fragment() {
    private var _binding: FragmentMusicBinding? = null
    private val binding get() = _binding!!
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var seekBar: SeekBar
    private lateinit var handler: Handler
    private lateinit var textViewMaxTime: TextView
    private lateinit var textViewCurrentTime: TextView
    private var song: String? = null
    private var title: String? = null
    private var multipart: MultipartBody.Part? = null

    private val TAG: String = "wfrr"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        arguments.let {
            song = it?.getString("song").toString()
            title = it?.getString("title").toString()

            Log.d("songurltest", "onCreateView: $song")
        }
        _binding = FragmentMusicBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.tvSongtitle.text = title

        binding.backBtn.setOnClickListener {
            replaceFragment(HomeFragment())
        }


        seekBar = binding.seekbar
        textViewMaxTime = binding.tvTimeMax
        textViewCurrentTime = binding.tvTimeCurrent
        binding.backBtn.setOnClickListener {
            replaceFragment(HomeFragment())
        }



        mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(song)
        mediaPlayer.setOnPreparedListener {
            // Start playback when prepared
            mediaPlayer.start()
            // Update SeekBar max value to duration of media
            seekBar.max = mediaPlayer.duration
            // Initialize handler for SeekBar updates
            handler = Handler(Looper.getMainLooper())
            // Update SeekBar progress every second
            handler.postDelayed(updateSeekBar, 1000)
            handler.postDelayed(updateCurrentTime, 1000)

            val maxtime = formatTime(mediaPlayer.duration)
            textViewMaxTime.text = maxtime
        }
        binding.ivPauseBtn.setOnClickListener {
            if(mediaPlayer.isPlaying){
                mediaPlayer.pause()
                binding.ivPauseBtn.setImageResource(R.drawable.play)
            }else{
                mediaPlayer.start()
                binding.ivPauseBtn.setImageResource(R.drawable.pause_icon_music)
            }
        }


        mediaPlayer.setOnErrorListener { mp, what, extra ->
            // Handle errors
            Log.e("MediaPlayer", "Error occurred: $what, $extra")
            false // Return false to indicate that the error is not handled here
        }

        // Prepare the MediaPlayer asynchronously
        mediaPlayer.prepareAsync()

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // No implementation needed
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // No implementation needed
            }
        })
    }

    private val updateSeekBar = object : Runnable {
        override fun run() {
            // Update SeekBar progress to current position of MediaPlayer
            seekBar.progress = mediaPlayer.currentPosition
            // Schedule next update in 1 second
            handler.postDelayed(this, 1000)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updateSeekBar)
        handler.removeCallbacks(updateCurrentTime)
        // Stop and release the MediaPlayer
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        mediaPlayer.release()
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = fragmentManager?.beginTransaction()
        transaction?.replace(R.id.main_fragment_CV, fragment)
        transaction?.commit()
    }

    private fun formatTime(timeInMillis: Int): String {
        val minutes = timeInMillis / 1000 / 60
        val seconds = timeInMillis / 1000 % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    private val updateCurrentTime = object : Runnable {
        override fun run() {
            // Update TextViewCurrentTime with the current time of the song
            val currentTime = formatTime(mediaPlayer.currentPosition)
            textViewCurrentTime.text = "$currentTime"
            // Schedule next update in 1 second
            handler.postDelayed(this, 1000)
        }
    }

    private fun createTempBitmapFile(bitmap: Bitmap): java.io.File? {
        val filename =
            "temp_image_${System.currentTimeMillis()}.jpeg" // Unique filename with timestamp
        val storageDir: java.io.File? =
            requireContext().cacheDir // Use app's cache directory for temporary files
        return try {
            if (storageDir != null && !storageDir.exists()) {
                storageDir.mkdirs() // Create the directory if it doesn't exist
            }
            val targetFile = java.io.File(storageDir, filename)
            val outputStream =
                FileOutputStream(targetFile)
            bitmap.compress(
                Bitmap.CompressFormat.JPEG,
                50,
                outputStream
            ) // Adjust format and quality as needed
            outputStream.flush()
            outputStream.close()
            targetFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    private fun requestAppPermissions(type: String) {
        Dexter.withActivity(requireActivity())

            .withPermissions(
                Manifest.permission.CAMERA
            )

            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        if (type == "1") {
                            CaptureFrontPhoto()
                        }
                    }

                    // check for permanent denial of any permission
                    if (report.isAnyPermissionPermanentlyDenied) {
                        // permission is denied permenantly, navigate user to app settings

                        showSettingsDialog()
                    }
                }


                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            })

            .onSameThread()

            .check()
    }


    private fun showSettingsDialog() {
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle("Need Permissions")

        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.")

        builder.setPositiveButton(
            "GOTO SETTINGS"
        ) { dialog, which ->
            dialog.cancel()
            openSettings()
        }

        builder.setNegativeButton(
            "Cancel"
        ) { dialog, which -> dialog.cancel() }

        builder.show()
    }


    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)

        val uri = Uri.fromParts("package", requireActivity().packageName, null)

        intent.setData(uri)

        startActivityForResult(intent, 101)
    }


    private fun CaptureFrontPhoto() {
        Log.d(TAG, "Preparing to take photo")

        var camera: Camera? = null


        val cameraInfo = Camera.CameraInfo()


        val frontCamera = 1


        //int backCamera=0;
        Camera.getCameraInfo(frontCamera, cameraInfo)



        try {
            camera = Camera.open(frontCamera)

            camera.enableShutterSound(false)
        } catch (e: RuntimeException) {
            Log.d(TAG, "Camera not available: " + 1)

            camera = null

            //e.printStackTrace();
        }

        try {
            if (null == camera) {
                Log.d(TAG, "Could not get camera instance")
            } else {
                Log.d(TAG, "Got the camera, creating the dummy surface texture")

                try {
                    camera.setPreviewTexture(SurfaceTexture(0))

                    camera.startPreview()
                } catch (e: Exception) {
                    Log.d(TAG, "Could not set the surface preview texture")

                    e.printStackTrace()
                }

                camera.takePicture(null, null,
                    Camera.PictureCallback { data, camera ->
                        try {
                            val bmp = BitmapFactory.decodeByteArray(data, 0, data.size)
                            val file = createTempBitmapFile(bmp)
                            multipart = createMultipartBodyPart(file!!)
                            lifecycleScope.launch(Dispatchers.IO) {
                                Log.d("taget-multi",multipart.toString())
                                val data = apiService.eyeImageUplaod(multipart!!)
                                Log.d("taget-data",data.toString())
                                if(data.isSuccessful){
                                    Log.d("taget",(data.body()?:"No Repsonse").toString())
                                }else{
                                    Log.e("taget",(data.errorBody()).toString())
                                }
                            }
                        } catch (e: Exception) {
                            println(e.message)
                        }
                        camera.release()
                    })
            }
        } catch (e: Exception) {
            camera!!.release()
        }
    }

    private fun createMultipartBodyPart(file: File): MultipartBody.Part {
        val requestFile =
            file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(
            "file",
            file.name,
            requestFile
        )
    }


}