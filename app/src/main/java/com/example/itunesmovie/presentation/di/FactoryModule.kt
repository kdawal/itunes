package com.example.itunesmovie.presentation.di

import com.example.itunesmovie.domain.usecase.*
import com.example.itunesmovie.presentation.viewmodel.TrackViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dependency injection for ViewModelFactory
 */
@Module
@InstallIn(SingletonComponent::class)
class FactoryModule {

 @Singleton
 @Provides
 fun providesTrackViewModelFactory(
  geTracksUseCase: GeTracksUseCase,
  getSearchTracksUseCase: GetSearchTracksUseCase,
  saveTrackUseCase: SaveTrackUseCase,
  getSavedTrackUseCase: GetSavedTrackUseCase,
  deleteSavedTrackUSeCase: DeleteSavedTrackUSeCase
 ): TrackViewModelFactory{
  return TrackViewModelFactory(
   geTracksUseCase,
   getSearchTracksUseCase,
   saveTrackUseCase,
   getSavedTrackUseCase,
   deleteSavedTrackUSeCase
  )
 }
}