package com.gachateam.wacawiraga.ui.detection

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.gachateam.wacawiraga.ImageLabel
import com.gachateam.wacawiraga.di.FireBaseModule
import com.gachateam.wacawiraga.ml.MetadataMobile
import com.gachateam.wacawiraga.ml.Model2
import com.gachateam.wacawiraga.utils.Helper
import com.gachateam.wacawiraga.utils.Resource
import com.google.mlkit.vision.common.InputImage
import org.tensorflow.lite.support.common.TensorProcessor
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.label.TensorLabel
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
        labeler
            .process(image)
            .addOnSuccessListener { result ->
               val listImageLabel =  result.map {
                    //Timber.d("bounding box ${detectedObject.boundingBox}")
//                      detectedObject.labels.map {
                        ImageLabel(
                            text = it.text,
                            index = it.index,
                            confidence = it.confidence
                        )
//                    }
                }
                _processedTextResult.value = Resource.Success(listImageLabel)
            }.addOnFailureListener {
                _processedTextResult.value = Resource.Error(it.localizedMessage)
            }
    }

    //java.lang.IllegalArgumentException: Label number 129 mismatch the shape on axis 1
    //with local model, model contain metadata, didnt pass
    val model = MetadataMobile.newInstance(getApplication())
    fun processBitmap(bitmap: Bitmap) {
        try {
            val image = TensorImage.fromBitmap(bitmap)
            val outputs = model.process(image)
            val probability = outputs.probabilityAsCategoryList
            Timber.i("probability $probability")
        } catch (e: Exception) {
            Timber.i("error ${e.localizedMessage}")
        }
    }

    val model2 = Model2.newInstance(getApplication())
    fun processBitmap2(bitmap: Bitmap) {
        try {
            val image = TensorImage.fromBitmap(bitmap).tensorBuffer
            val outputs = model2.process(image)
            val probability = outputs.outputFeature0AsTensorBuffer
            Timber.i("probability $probability")

            val probabilityProcessor = TensorProcessor.Builder()
                .add(NormalizeOp(0f,255f)).build()

            val labels = TensorLabel(Helper.labels, probabilityProcessor.process(probability))

            val floatmap = labels.mapWithFloatValue

            Timber.i("floatmap : $floatmap")

        } catch (e: Exception) {
            Timber.i("error processbitmap2 ${e.localizedMessage}")
        }
    }


    fun manualProcess(process: Resource<List<ImageLabel>>) {
        _processedTextResult.value = process
    }

    override fun onCleared() {
        super.onCleared()
        model.close()
    }
}