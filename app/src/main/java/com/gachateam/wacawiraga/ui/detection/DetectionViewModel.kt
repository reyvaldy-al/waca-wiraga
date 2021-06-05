package com.gachateam.wacawiraga.ui.detection

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.gachateam.wacawiraga.ImageLabel
import com.gachateam.wacawiraga.di.FireBaseModule
import com.gachateam.wacawiraga.utils.Resource
import com.gachateam.wacawiraga.utils.getImageTextFromIndex
import com.google.mlkit.vision.common.InputImage
import timber.log.Timber

class DetectionViewModel(application: Application) : AndroidViewModel(application) {

    private val _uriImage = MutableLiveData<Uri>()

    fun submitUri(uri: Uri) {
        _uriImage.value = uri
    }

    val uriImage = _uriImage

    private val labeler = FireBaseModule.labeler
    private val objectDetection = FireBaseModule.objectDetector

    private val _processedTextResult = MutableLiveData<Resource<List<ImageLabel>>>()
    val processedTextResult = _processedTextResult
    fun processImage(uriImage: Uri) {
        _processedTextResult.value = Resource.Loading
        val image = InputImage.fromFilePath(getApplication(), uriImage)
        objectDetection
            .process(image)
            .addOnSuccessListener { result ->
               val listImageLabel =  result.map { detectedObject ->
                    Timber.d("bounding box ${detectedObject.boundingBox}")
                      detectedObject.labels.map {
                        ImageLabel(
                            text = it.text,
                            index = it.index,
                            confidence = it.confidence
                        )
                    }
                }.flatten()
                _processedTextResult.value = Resource.Success(listImageLabel)
            }.addOnFailureListener {
                _processedTextResult.value = Resource.Error(it.localizedMessage)
            }
    }
}