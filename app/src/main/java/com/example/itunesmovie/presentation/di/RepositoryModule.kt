package com.example.itunesmovie.presentation.di

import com.example.itunesmovie.data.repository.TrackRepositoryImpl
import com.example.itunesmovie.data.repository.datasource.TrackLocalDataSource
import com.example.itunesmovie.data.repository.datasource.TrackRemoteDataSource
import com.example.itunesmovie.domain.repository.TrackRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

 @Singleton
 @Provides
 fun providesTrackRepository(
  trackRemoteDataSource: TrackRemoteDataSource,
  trackLocalDataSource: TrackLocalDataSource
 ): TrackRepository{
  return TrackRepositoryImpl(trackRemoteDataSource, trackLocalDataSource)
 }

}