package com.example.kotlinbitirmevolkanergunfoodorderingapp.data.entity
import com.google.gson.annotations.SerializedName

data class CRUDCevap(
    @SerializedName("success")
    val success: Int,
    @SerializedName("message")
    val message: String?
)
