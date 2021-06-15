package com.gachateam.wacawiraga.ui.detection

import android.Manifest.permission.*
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.gachateam.wacawiraga.databinding.ActivityDetectionBinding
import com.gachateam.wacawiraga.utils.GlideApp
import com.gachateam.wacawiraga.utils.Resource
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.annotations.AfterPermissionGranted
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import timber.log.Timber
import java.lang.Error

class DetectionActivity : AppCompatActivity(),EasyPermissions.PermissionCallbacks {

    private lateinit var binding : ActivityDetectionBinding

    private val viewmodel by viewModels<DetectionViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        listenbackPressed()
        val uriImage = intent.getStringExtra(EXTRA_IMAGE_PATH)?.toUri()!!
        viewmodel.submitUri(uriImage)

        viewmodel.uriImage.observe(this) {
            loadImageRequierePermissions()
            viewmodel.processImage(it)
        }
        val adapter = ResultDetectionAdapter()
        binding.resultDetectionRv.adapter = adapter
        binding.resultDetectionRv.layoutManager = LinearLayoutManager(this)
        binding.resultDetectionRv.setHasFixedSize(true)

        viewmodel.processedTextResult.observe(this) {
            when (it) {
                is Resource.Loading -> {

                    binding.tvLabel.isVisible = true
                    binding.tvLabel.text = "memproses..."
                }
                is Resource.Success -> {
                    binding.tvLabel.isVisible = false
                    adapter.submitList(it.succesData)
                }
                is Resource.Error -> {
                    binding.tvLabel.isVisible = true
                    binding.tvLabel.text = it.exception
                }
            }
        }
    }

    //required to not have param
    @AfterPermissionGranted(REQUEST_CODE_STORAGE_PERMISSION)
    fun loadImageRequierePermissions() {

        if (EasyPermissions.hasPermissions(this, READ_EXTERNAL_STORAGE)) {
            GlideApp.with(this)
                .load(viewmodel.uriImage.value)
                .dontAnimate()
                .listener(object :RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean = true

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        if (resource == null){
                            viewmodel.manualProcess(Resource.Error("image is not loaded"))
                            return false
                        }
                        //viewmodel.processBitmap(resource.toBitmap())
                        return false
                    }

                })
                .into(binding.ivOriginal)

        } else {
            EasyPermissions.requestPermissions(
                host = this@DetectionActivity,
                rationale = "minta ijin ngakses gambar",
                requestCode = REQUEST_CODE_STORAGE_PERMISSION,
                READ_EXTERNAL_STORAGE
            )
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        Timber.d( "$requestCode $perms denied")
        if (EasyPermissions.somePermissionPermanentlyDenied(this@DetectionActivity, perms)) {
            SettingsDialog.Builder(this).build().show()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Timber.d("$requestCode $perms granted")
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
    companion object {
        const val EXTRA_IMAGE_PATH = "extra_image_path"
        const val REQUEST_CODE_STORAGE_PERMISSION = 213
    }

    fun listenbackPressed() = binding.btnBack.setOnClickListener { onBackPressed() }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}