package com.example.itunesmovie.presentation.di

import com.example.itunesmovie.data.db.TrackDao
import com.example.itunesmovie.data.db.UserActivityDao
import com.example.itunesmovie.data.repository.datasource.TrackLocalDataSource
import com.example.itunesmovie.data.repository.datasource.UserActivityLocalDataSource
import com.example.itunesmovie.data.repository.datasourceimpl.TrackLocalDataSourceImpl
import com.example.itunesmovie.data.repository.datasourceimpl.UserActivityLocalDataSourceImpl
import com.example.itunesmovie.domain.repository.UserActivityRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalDataSource {

 @Singleton
 @Provides
 fun providesTrackLocalDataSource(trackDao: TrackDao): TrackLocalDataSource{
  return TrackLocalDataSourceImpl(trackDao)
 }

 @Singleton
 @Provides
 fun providesUserActivityLocalDataSource(userActivityDao: UserActivityDao): UserActivityLocalDataSource{
  return UserActivityLocalDataSourceImpl(userActivityDao)
 }
}