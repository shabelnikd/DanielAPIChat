package com.shabelnikd.danielapichat.utils

import android.util.Log
import retrofit2.Response


suspend fun <T> safeApiCall(
    call: suspend () -> Response<T>,
    errorMessage: String
): Result<Response<T>> =
    try {
        val result = call()
        Result.Companion.success(result)
    } catch (e: Exception) {
        Log.e("AllSD", errorMessage, e)
        Result.Companion.failure(e)
    }
