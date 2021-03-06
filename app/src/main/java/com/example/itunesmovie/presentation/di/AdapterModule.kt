package com.example.itunesmovie.presentation.di

import com.example.itunesmovie.presentation.adapter.HeaderAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dependency injection for Adapter classes
 */
@Module
@InstallIn(SingletonComponent::class)
class AdapterModule {

  @Singleton
  @Provides
  fun providesHeaderAdapter(): HeaderAdapter {
    return HeaderAdapter()
  }
}