package com.example.silentmoonmeditation

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitApi {
    private val retrofit = Retrofit.Builder().apply {
        baseUrl("http://192.168.123.106:5000/")
        addConverterFactory(GsonConverterFactory.create())
    }.build()
    val apiService = retrofit.create(AppApi::class.java)
}