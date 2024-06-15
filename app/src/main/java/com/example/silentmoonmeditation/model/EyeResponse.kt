package com.example.silentmoonmeditation.model

import com.google.gson.annotations.SerializedName

data class EyeResponse(
    @SerializedName("prediction")
    val predition : Float? = null
)
