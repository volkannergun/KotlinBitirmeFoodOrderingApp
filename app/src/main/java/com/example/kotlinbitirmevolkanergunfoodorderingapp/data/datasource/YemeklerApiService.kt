package com.example.kotlinbitirmevolkanergunfoodorderingapp.data.datasource // << CHECK THIS PACKAGE NAME

import com.example.kotlinbitirmevolkanergunfoodorderingapp.data.entity.CRUDCevap
import com.example.kotlinbitirmevolkanergunfoodorderingapp.data.entity.SepetYemeklerResponse
import com.example.kotlinbitirmevolkanergunfoodorderingapp.data.entity.YemeklerResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface YemeklerApiService { // << IS IT AN INTERFACE?
    companion object {
        const val KULLANICI_ADI = "volkan_ergun"
    }

    @GET("yemekler/tumYemekleriGetir.php")
    suspend fun tumYemekleriGetir(): YemeklerResponse

    @POST("yemekler/sepeteYemekEkle.php")
    @FormUrlEncoded
    suspend fun sepeteYemekEkle(
        @Field("yemek_adi") yemekAdi: String,
        @Field("yemek_resim_adi") yemekResimAdi: String,
        @Field("yemek_fiyat") yemekFiyat: String,
        @Field("yemek_siparis_adet") yemekSiparisAdet: String,
        @Field("kullanici_adi") kullaniciAdi: String = KULLANICI_ADI
    ): CRUDCevap

    @POST("yemekler/sepettekiYemekleriGetir.php")
    @FormUrlEncoded
    suspend fun sepettekiYemekleriGetir(
        @Field("kullanici_adi") kullaniciAdi: String = KULLANICI_ADI
    ): SepetYemeklerResponse

    @POST("yemekler/sepettenYemekSil.php")
    @FormUrlEncoded
    suspend fun sepettenYemekSil(
        @Field("sepet_yemek_id") sepetYemekId: String,
        @Field("kullanici_adi") kullaniciAdi: String = KULLANICI_ADI
    ): CRUDCevap
}