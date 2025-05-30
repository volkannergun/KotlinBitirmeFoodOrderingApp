package com.example.kotlinbitirmevolkanergunfoodorderingapp.data.entity
import com.google.gson.annotations.SerializedName

data class SepetYemeklerResponse(
    @SerializedName("sepet_yemekler")
    val sepet_yemekler: List<SepetYemek>,
    @SerializedName("success")
    val success: Int
)
