package com.example.itunesmovie.presentation.di

import com.example.itunesmovie.BuildConfig
import com.example.itunesmovie.data.api.TrackAPIService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Dependency injection for Retrofit and APIService class
 */
@Module
@InstallIn(SingletonComponent::class)
class NetModule {

 @Singleton
 @Provides
 fun providesRetrofit(): Retrofit{
  val interceptor = HttpLoggingInterceptor().apply {
   level = HttpLoggingInterceptor.Level.BODY
  }
  val client = OkHttpClient.Builder().apply {
   addInterceptor(interceptor)
   connectTimeout(30, TimeUnit.SECONDS)
   readTimeout(20, TimeUnit.SECONDS)
   writeTimeout(25, TimeUnit.SECONDS)
  }.build()

  return Retrofit.Builder()
   .addConverterFactory(GsonConverterFactory.create())
   .baseUrl(BuildConfig.BASE_URL)
   .client(client)
   .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
   .build()

 }

 @Singleton
 @Provides
 fun providesTracksAPIService(retrofit: Retrofit):TrackAPIService{
 return retrofit.create(TrackAPIService::class.java)
 }
}