package com.example.kotlinbitirmevolkanergunfoodorderingapp.data.local
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.kotlinbitirmevolkanergunfoodorderingapp.data.entity.FavoriYemek
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriYemekDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun favoriyeEkle(favoriYemek: FavoriYemek)

    @Delete
    suspend fun favoridenSil(favoriYemek: FavoriYemek)

    @Query("SELECT * FROM favori_yemekler")
    fun tumFavorileriGetir(): Flow<List<FavoriYemek>>

    @Query("SELECT * FROM favori_yemekler WHERE yemek_id = :yemekId")
    fun favoriMi(yemekId: String): Flow<FavoriYemek?>
    
    @Query("SELECT EXISTS(SELECT * FROM favori_yemekler WHERE yemek_id = :yemekId)")
    fun isFavoriKontrol(yemekId: String): Flow<Boolean>
}
