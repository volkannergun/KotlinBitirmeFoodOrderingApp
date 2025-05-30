package com.example.kotlinbitirmevolkanergunfoodorderingapp.di
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import com.example.kotlinbitirmevolkanergunfoodorderingapp.data.datasource.YemeklerApiService
import com.example.kotlinbitirmevolkanergunfoodorderingapp.data.local.FavoriYemekDao
import com.example.kotlinbitirmevolkanergunfoodorderingapp.data.local.YemeklerDatabase

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val BASE_URL = "http://kasimadalan.pe.hu/"

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        return logging
    }
    
    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideYemeklerApiService(retrofit: Retrofit): YemeklerApiService {
        return retrofit.create(YemeklerApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideYemeklerDatabase(@ApplicationContext context: Context): YemeklerDatabase {
        return Room.databaseBuilder(
            context,
            YemeklerDatabase::class.java,
            "yemekler_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideFavoriYemekDao(database: YemeklerDatabase): FavoriYemekDao {
        return database.favoriYemekDao()
    }
}
