package com.example.kotlinbitirmevolkanergunfoodorderingapp.data.repo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.example.kotlinbitirmevolkanergunfoodorderingapp.data.datasource.YemeklerApiService
import com.example.kotlinbitirmevolkanergunfoodorderingapp.data.entity.FavoriYemek
import com.example.kotlinbitirmevolkanergunfoodorderingapp.data.entity.SepetYemek
import com.example.kotlinbitirmevolkanergunfoodorderingapp.data.entity.Yemek
import com.example.kotlinbitirmevolkanergunfoodorderingapp.data.local.FavoriYemekDao

class YemeklerRepository @Inject constructor(
    private val apiService: YemeklerApiService,
    private val favoriYemekDao: FavoriYemekDao
) {

    suspend fun tumYemekleriGetir(): List<Yemek> {
        return try {
            val response = apiService.tumYemekleriGetir()
            if (response.success == 1) response.yemekler else emptyList()
        } catch (e: Exception) {
            // Log error e.g. Timber.e(e)
            emptyList()
        }
    }

    suspend fun sepeteYemekEkle(yemek: Yemek, adet: Int): Boolean {
        return try {
            val response = apiService.sepeteYemekEkle(
                yemekAdi = yemek.yemek_adi,
                yemekResimAdi = yemek.yemek_resim_adi,
                yemekFiyat = yemek.yemek_fiyat,
                yemekSiparisAdet = adet.toString()
            )
            response.success == 1
        } catch (e: Exception) {
            false
        }
    }
    
    suspend fun sepettekiYemekleriGetir(): List<SepetYemek> {
        return try {
            val response = apiService.sepettekiYemekleriGetir()
            if (response.success == 1) response.sepet_yemekler else emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun sepettenYemekSil(sepetYemekId: String): Boolean {
         return try {
            val response = apiService.sepettenYemekSil(sepetYemekId)
            response.success == 1
        } catch (e: Exception) {
            false
        }
    }

    // Favorites
    fun tumFavorileriGetir(): Flow<List<FavoriYemek>> = favoriYemekDao.tumFavorileriGetir()

    suspend fun favoriyeEkle(yemek: Yemek) {
        val favoriYemek = FavoriYemek(
            yemek_id = yemek.yemek_id,
            yemek_adi = yemek.yemek_adi,
            yemek_resim_adi = yemek.yemek_resim_adi,
            yemek_fiyat = yemek.yemek_fiyat
        )
        favoriYemekDao.favoriyeEkle(favoriYemek)
    }
    
    suspend fun favoriyeEkle(favoriYemek: FavoriYemek) {
        favoriYemekDao.favoriyeEkle(favoriYemek)
    }

    suspend fun favoridenSil(yemekId: String) {
        val favoriYemek = FavoriYemek(yemek_id = yemekId, "", "", "") 
        favoriYemekDao.favoridenSil(favoriYemek)
    }
    
    suspend fun favoridenSil(favoriYemek: FavoriYemek) {
        favoriYemekDao.favoridenSil(favoriYemek)
    }

    fun isFavori(yemekId: String): Flow<Boolean> = favoriYemekDao.isFavoriKontrol(yemekId)
}
