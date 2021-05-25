package com.gachateam.wacawiraga.ui.detection

import android.Manifest.permission.*
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.gachateam.wacawiraga.databinding.ActivityDetectionBinding
import com.gachateam.wacawiraga.utils.GlideApp
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.annotations.AfterPermissionGranted
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import timber.log.Timber

class DetectionActivity : AppCompatActivity(),EasyPermissions.PermissionCallbacks {

    private lateinit var binding : ActivityDetectionBinding
    private var uriImage :Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        uriImage = intent.getStringExtra(EXTRA_IMAGE_PATH)?.toUri()
        loadImageRequierePermissions()
    }

    @AfterPermissionGranted(REQUEST_CODE_STORAGE_PERMISSION)
    fun loadImageRequierePermissions() {
        if (EasyPermissions.hasPermissions(this, WRITE_EXTERNAL_STORAGE)) {
            GlideApp.with(this)
                .load(uriImage)
                .dontAnimate()
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
}