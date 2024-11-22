package com.example.samojlov_av_homework_module_16_number_1__1_koala

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.samojlov_av_homework_module_16_number_1__1_koala.databinding.ActivityMainBinding
import com.example.samojlov_av_homework_module_16_number_1__1_koala.models.VideoInternal
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File

const val GALLERY_REQUEST = 26

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var videoViewVW: VideoView
    private lateinit var addVideoBT: FloatingActionButton

    private var currentVideo = 0
    private var internalVideo: Uri? = null
    private var mediaController: MediaController? = null
    private var externalVideo: Uri? = null
    private var nameVideo = VideoInternal.videoInternalList[0].description

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
//        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        init()
        initPermission()
    }


    @SuppressLint("InlinedApi")
    private fun initPermission() {
        val permission = arrayOf(
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        permissionLauncher.launch(permission)
    }

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { }

    private fun init() {
        videoViewVW = binding.videoViewVW
        addVideoBT = binding.addVideoBT

        addVideoBT.setOnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_GET_CONTENT)
            photoPickerIntent.type = "video/*"
            startActivityForResult(photoPickerIntent, GALLERY_REQUEST)
        }


        videoBuildUri()

        if (internalVideo != null) {
            videoPlayer()
        }

    }

    private fun videoBuildUri() {
        internalVideo =
            Uri.parse(
                "android.resource://$packageName/${VideoInternal.videoInternalList[currentVideo].video}"
            )
    }

    private fun videoPlayer() {
        mediaController = MediaController(this)
        mediaController?.setAnchorView(mediaController)
        mediaController?.setBackgroundColor(getResources().getColor(R.color.Blue))
        mediaController?.setPrevNextListeners({
            nextCurrentVideo()
            switching()
        }, {
            previsionCurrentVideo()
            switching()
        })

        videoViewVW.setMediaController(mediaController)
        videoViewVW.setVideoURI(internalVideo)
        videoViewVW.requestFocus()

        mediaController?.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                addVideoBT.visibility = View.VISIBLE
                Toast.makeText(this@MainActivity,nameVideo, Toast.LENGTH_LONG).show()
            }

            override fun onViewDetachedFromWindow(v: View) {
                addVideoBT.visibility = View.INVISIBLE
            }


        })

    }

    private fun switching() {
        videoBuildUri()
        videoViewVW.stopPlayback()
        videoViewVW.setVideoURI(internalVideo)
        nameVideo = VideoInternal.videoInternalList[currentVideo].description
        externalVideo = null
    }

    private fun previsionCurrentVideo() {
        if (currentVideo == 0) {
            currentVideo = VideoInternal.videoInternalList.size - 1
        } else {
            currentVideo--
        }
    }

    private fun nextCurrentVideo() {
        if (currentVideo == VideoInternal.videoInternalList.size - 1) {
            currentVideo = 0
        } else {
            currentVideo++
        }
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    @Suppress("DEPRECATED_IDENTITY_EQUALS")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            GALLERY_REQUEST -> if (resultCode === RESULT_OK) {
                externalVideo = data?.data
                val f = File("" + externalVideo)
                nameVideo = f.name
                videoViewVW.setVideoURI(externalVideo)
            }
        }
    }
}