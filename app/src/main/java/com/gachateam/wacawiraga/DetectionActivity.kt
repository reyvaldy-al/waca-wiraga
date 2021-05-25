package com.gachateam.wacawiraga

import android.Manifest.permission.*
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.gachateam.wacawiraga.databinding.ActivityDetectionBinding
import com.gachateam.wacawiraga.utils.GlideApp
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.annotations.AfterPermissionGranted
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.jar.Manifest

class DetectionActivity : AppCompatActivity(),EasyPermissions.PermissionCallbacks {

    private lateinit var binding : ActivityDetectionBinding
    private var uriImage :Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        uriImage = intent.getStringExtra(EXTRA_IMAGE_PATH)?.toUri()

        Log.i("tag", "uriImage in Detection Activity ${uriImage.toString()}")

        loadImageRequierePermissions()
    }

    @AfterPermissionGranted(REQUEST_CODE_STORAGE_PERMISSION)
    fun loadImageRequierePermissions() {
        if (EasyPermissions.hasPermissions(
                this,
                WRITE_EXTERNAL_STORAGE
            )
        ) {
            GlideApp.with(this)
                .load(uriImage)
                .dontAnimate()
                .listener(object : RequestListener<Drawable?>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable?>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        Timber.i("load image error ${e?.localizedMessage}")
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable?>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        Timber.i("load image succes")
                        return false
                    }

                })
                .into(binding.imageView2)


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
        Log.d("tag", "$requestCode $perms denied")
        if (EasyPermissions.somePermissionPermanentlyDenied(this@DetectionActivity, perms)) {
            SettingsDialog.Builder(this).build().show()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Log.d("tag","$requestCode $perms granted")
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
}