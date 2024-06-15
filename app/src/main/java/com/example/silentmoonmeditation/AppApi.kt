package com.example.silentmoonmeditation

import com.example.silentmoonmeditation.model.EyeResponse
import com.example.silentmoonmeditation.model.EyeScanningRequest
import okhttp3.MultipartBody
import okhttp3.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AppApi {

    @Multipart
    @POST("predict")
    suspend fun eyeImageUplaod(@Part file: MultipartBody.Part): retrofit2.Response<EyeResponse>
}