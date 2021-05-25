package com.gachateam.wacawiraga

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.gachateam.wacawiraga.DetectionActivity.Companion.EXTRA_IMAGE_PATH
import com.gachateam.wacawiraga.databinding.ActivityMainBinding
import com.gachateam.wacawiraga.utils.Helper
import com.github.dhaval2404.imagepicker.ImagePicker

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
                //Image Uri will not be null for RESULT_OK
                val uri: Uri = data?.data!!
                Log.d("Take", "uri = $uri")
                Toast.makeText(this, "Task Success", Toast.LENGTH_SHORT).show()
                // Use Uri object instead of File to avoid storage permissions
                val intent = Intent(this@MainActivity,DetectionActivity::class.java).apply {
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