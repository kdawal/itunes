package com.example.itunesmovie.presentation.di

import com.example.itunesmovie.data.db.TrackDao
import com.example.itunesmovie.data.repository.datasource.TrackLocalDataSource
import com.example.itunesmovie.data.repository.datasourceimpl.TrackLocalDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dependency injection for local datasource
 */
@Module
@InstallIn(SingletonComponent::class)
class LocalDataSource {

  @Singleton
  @Provides
  fun providesTrackLocalDataSource(trackDao: TrackDao): TrackLocalDataSource {
    return TrackLocalDataSourceImpl(trackDao)
  }

}