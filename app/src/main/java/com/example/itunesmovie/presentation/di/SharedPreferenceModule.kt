package com.example.itunesmovie.presentation.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dependency injection for SharedPreference
 */
@Module
@InstallIn(SingletonComponent::class)
class SharedPreferenceModule {

 @Singleton
 @Provides
 fun providesSharedPreference(app: Application): SharedPreferences{
  return app.getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE)
 }
}