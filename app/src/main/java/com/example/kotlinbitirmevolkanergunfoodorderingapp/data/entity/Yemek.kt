package com.example.kotlinbitirmevolkanergunfoodorderingapp.data.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Yemek(
    @SerializedName("yemek_id")
    val yemek_id: String,
    @SerializedName("yemek_adi")
    val yemek_adi: String,
    @SerializedName("yemek_resim_adi")
    val yemek_resim_adi: String,
    @SerializedName("yemek_fiyat")
    val yemek_fiyat: String
) : Parcelable {
    fun getFullImageUrl(): String {
        return "http://kasimadalan.pe.hu/yemekler/resimler/$yemek_resim_adi"
    }
}
