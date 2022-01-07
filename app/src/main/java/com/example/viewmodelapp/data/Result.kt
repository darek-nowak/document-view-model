package com.example.viewmodelapp.data

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    object Error : Result<Nothing>()
}