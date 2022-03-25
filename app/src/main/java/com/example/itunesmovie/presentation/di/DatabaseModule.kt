package com.example.itunesmovie.presentation.di

import android.app.Application
import androidx.room.Room
import com.example.itunesmovie.data.db.TrackDB
import com.example.itunesmovie.data.db.TrackDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dependency injection for Room Database including DAO
 */
@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

 @Singleton
 @Provides
 fun providesTrackDatabase(app: Application): TrackDB{
  return Room.databaseBuilder(
   app,
   TrackDB::class.java,
  "track_db")
   .fallbackToDestructiveMigration()
   .build()
 }

 @Singleton
 @Provides
 fun getTrackDao(trackDB: TrackDB): TrackDao{
  return trackDB.getTrackDao()
 }
}