package com.example.silentmoonmeditation.model

import okhttp3.MultipartBody

data class EyeScanningRequest(
    val file: MultipartBody.Part
)
