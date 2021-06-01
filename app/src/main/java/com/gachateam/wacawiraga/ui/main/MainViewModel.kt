package com.gachateam.wacawiraga.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.gachateam.wacawiraga.di.FireBaseModule
import com.gachateam.wacawiraga.utils.Resource
import com.google.mlkit.common.model.CustomRemoteModel
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class MainViewModel : ViewModel() {

    private var remoteModel :CustomRemoteModel? = FireBaseModule.remoteModel
    private var downloadCondition : DownloadConditions? = FireBaseModule.downloadConditions

    private val _listeningDownloadModelProgress = liveData<Resource<Void>> {
        emit(Resource.Loading)
        try {
            val data =
                RemoteModelManager.getInstance().download(remoteModel, downloadCondition).await()
            Timber.d("download model $data")
            emit(Resource.Success(data))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage))
        }
    }

    val listeningDownloadModelProgress = _listeningDownloadModelProgress

    override fun onCleared() {
        remoteModel = null
        downloadCondition = null
        super.onCleared()
    }
}