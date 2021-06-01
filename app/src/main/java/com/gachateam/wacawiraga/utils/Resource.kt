package com.gachateam.wacawiraga.utils

sealed class Resource<out T> {
    object Loading : Resource<Nothing>()
    data class Success<out T>(val succesData : T) :Resource<T>()
    data class Error<out T>(val exception : String?) : Resource<T>()
}