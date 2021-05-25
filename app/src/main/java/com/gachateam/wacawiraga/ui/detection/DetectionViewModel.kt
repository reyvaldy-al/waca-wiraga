package com.gachateam.wacawiraga.ui.detection

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.gachateam.wacawiraga.di.FireBaseModule
import com.gachateam.wacawiraga.utils.Resource
import com.google.mlkit.vision.common.InputImage

class DetectionViewModel(application: Application) : AndroidViewModel(application) {

    private val _uriImage = MutableLiveData<Uri>()

    fun submitUri(uri: Uri) {
        _uriImage.value = uri
    }

    val uriImage = _uriImage


    private val objectDetector = FireBaseModule.objectDetector
    private val _processedTextResult = MutableLiveData<Resource<String>>()
    val processedTextResult = _processedTextResult
    fun processImage(uriImage : Uri)  {
        _processedTextResult.value = Resource.Loading
        val image = InputImage.fromFilePath(getApplication(), uriImage)
        objectDetector
            .process(image)
            .addOnSuccessListener { result ->
                var textResult = ""
                result.forEach { detectedObject ->
                    val boundingBox = detectedObject.boundingBox
                    detectedObject.labels.forEach { label ->
                        val text = label.text
                        val index = label.index
                        val confidence = label.confidence
                        textResult += "text = $text, index = $index, confidence = $confidence, boundingbox = $boundingBox\n"
                    }
                }
                _processedTextResult.value = Resource.Success(textResult)
            }.addOnFailureListener {
                it.printStackTrace()
                _processedTextResult.value = Resource.Error(it.localizedMessage)
            }
    }
}