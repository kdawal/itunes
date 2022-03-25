package com.example.itunesmovie.presentation.di

import com.example.itunesmovie.data.api.TrackAPIService
import com.example.itunesmovie.data.repository.datasource.TrackRemoteDataSource
import com.example.itunesmovie.data.repository.datasourceimpl.TrackRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dependency injection for remote datasource
 */
@Module
@InstallIn(SingletonComponent::class)
class RemoteDataSourceModule {

 @Singleton
 @Provides
 fun providesTracksRemoteDataSource(
  trackAPIService: TrackAPIService
 ): TrackRemoteDataSource{
  return TrackRemoteDataSourceImpl(trackAPIService)
 }
}