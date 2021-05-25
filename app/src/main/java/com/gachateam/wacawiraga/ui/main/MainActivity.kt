package com.gachateam.wacawiraga.ui.main

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.gachateam.wacawiraga.R
import com.gachateam.wacawiraga.ui.detection.DetectionActivity.Companion.EXTRA_IMAGE_PATH
import com.gachateam.wacawiraga.databinding.ActivityMainBinding
import com.gachateam.wacawiraga.ui.detection.DetectionActivity
import com.gachateam.wacawiraga.utils.Helper
import com.gachateam.wacawiraga.utils.Resource
import com.gachateam.wacawiraga.utils.toast
import com.github.dhaval2404.imagepicker.ImagePicker
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cvTakePicture.setOnClickListener {
           /* if (viewModel.listeningDownloadModelProgress.value !is Resource.Success) {
                toast("model is not ready")
                return@setOnClickListener
            }*/

            ImagePicker.with(this)
                .compress(1024)
                .maxResultSize(
                    1080,
                    1080
                )
                .start()
        }

        binding.textView.text = Helper.getGreetingMessage()

        viewModel.listeningDownloadModelProgress.observe(this){
            when (it) {
                is Resource.Loading->{
                    toast("downloading model")

                }
                is Resource.Success->{
                    toast("model downloaded")
                }
                is Resource.Error->{
                    toast("failed to download model ${it.exception}")
                    Timber.e("model ${it.exception}")
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                val uri: Uri = data?.data!!
                Timber.i( "uri = $uri")
                val intent = Intent(this@MainActivity, DetectionActivity::class.java).apply {
                    putExtra(EXTRA_IMAGE_PATH,uri.toString())
                }
                startActivity(intent)
            }
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }
}