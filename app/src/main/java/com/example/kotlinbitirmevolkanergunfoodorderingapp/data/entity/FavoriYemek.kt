package com.example.kotlinbitirmevolkanergunfoodorderingapp.data.entity
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favori_yemekler")
data class FavoriYemek(
    @PrimaryKey
    val yemek_id: String,
    val yemek_adi: String,
    val yemek_resim_adi: String,
    val yemek_fiyat: String
) {
    fun getFullImageUrl(): String {
        return "http://kasimadalan.pe.hu/yemekler/resimler/$yemek_resim_adi"
    }
}
