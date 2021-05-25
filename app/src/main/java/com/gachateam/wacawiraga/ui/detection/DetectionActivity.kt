package com.gachateam.wacawiraga.ui.detection

import android.Manifest.permission.*
import android.os.Bundle
import androidx.activity.viewModels
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

    private val viewmodel by viewModels<DetectionViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val uriImage = intent.getStringExtra(EXTRA_IMAGE_PATH)?.toUri()!!
        viewmodel.submitUri(uriImage)

        viewmodel.uriImage.observe(this){
            loadImageRequierePermissions()
        }

        binding.btnBack.setOnClickListener { onBackPressed() }
    }

    //required to not have param
    @AfterPermissionGranted(REQUEST_CODE_STORAGE_PERMISSION)
    fun loadImageRequierePermissions() {
        if (EasyPermissions.hasPermissions(this, WRITE_EXTERNAL_STORAGE)) {
            GlideApp.with(this)
                .load(viewmodel.uriImage.value)
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

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}