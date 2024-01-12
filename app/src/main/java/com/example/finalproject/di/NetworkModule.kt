package com.example.finalproject.di

import com.example.finalproject.BuildConfig
import com.example.finalproject.common.Const.Api.BASE_URL
import com.example.finalproject.data.network.api.MovieApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideMoviesApi(): MovieApi {
        val token = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2YzgxNTVmZDY0OTJhMmE0Y2YyZTdlZGJhYmRjOWJmNyIsInN1YiI6IjY1N2VkNmIyMzIzZWJhM2JlZTg3YTk4MSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.ecHadNWp42J40HSboI94YWy8WQocIhj-ZlYRgUAK2B0"
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(provideHttpLoggingInterceptor())
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                chain.proceed(request)
            }
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(MovieApi::class.java)
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level =
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }
        return loggingInterceptor
    }


}