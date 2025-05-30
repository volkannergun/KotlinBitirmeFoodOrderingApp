package com.example.kotlinbitirmevolkanergunfoodorderingapp.data.local
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.kotlinbitirmevolkanergunfoodorderingapp.data.entity.FavoriYemek

@Database(entities = [FavoriYemek::class], version = 1, exportSchema = false)
abstract class YemeklerDatabase : RoomDatabase() {
    abstract fun favoriYemekDao(): FavoriYemekDao
}
