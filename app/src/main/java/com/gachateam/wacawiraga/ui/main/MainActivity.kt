package com.gachateam.wacawiraga.ui.main

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gachateam.wacawiraga.R
import com.gachateam.wacawiraga.ui.detection.DetectionActivity.Companion.EXTRA_IMAGE_PATH
import com.gachateam.wacawiraga.databinding.ActivityMainBinding
import com.gachateam.wacawiraga.ui.detection.DetectionActivity
import com.gachateam.wacawiraga.utils.Helper
import com.github.dhaval2404.imagepicker.ImagePicker
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cvTakePicture.setOnClickListener {
            ImagePicker.with(this)
                .compress(1024)
                .maxResultSize(
                    1080,
                    1080
                )
                .start()
        }

        binding.textView.text = Helper.getGreetingMessage()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                val uri: Uri = data?.data!!
                Timber.i( "uri = $uri")
                Toast.makeText(this, "Task Success", Toast.LENGTH_SHORT).show()
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