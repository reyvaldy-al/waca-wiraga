package com.gachateam.wacawiraga.di

import com.google.mlkit.common.model.CustomRemoteModel
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.linkfirebase.FirebaseModelSource

object FireBaseModule {
    private const val CUSTOM_MODEL_NAME = "model name"

    // Specify the name you assigned in the Firebase console.
    val remoteModel =
        CustomRemoteModel
            .Builder(FirebaseModelSource.Builder(CUSTOM_MODEL_NAME).build())
            .build()

    val downloadConditions = DownloadConditions.Builder()
        .build()

    //download model if not exist or update
//     RemoteModelManager.getInstance().download(remoteModel, downloadConditions)
//        .addOnSuccessListener {
//            // Success.
//
//        }
}