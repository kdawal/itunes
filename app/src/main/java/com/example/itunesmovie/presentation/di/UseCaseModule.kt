package com.example.itunesmovie.presentation.di

import com.example.itunesmovie.domain.repository.TrackRepository
import com.example.itunesmovie.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

 @Singleton
 @Provides
 fun providesGetTracksUseCase(
  trackRepository: TrackRepository
 ): GeTracksUseCase{
  return GeTracksUseCase(trackRepository)
 }

 @Singleton
 @Provides
 fun providesGetSearchTracksUseCase(
  trackRepository: TrackRepository
 ): GetSearchTracksUseCase{
  return GetSearchTracksUseCase(trackRepository)
 }

 @Singleton
 @Provides
 fun providesSavedTrackUseCase(
  trackRepository: TrackRepository
 ): SaveTrackUseCase{
  return SaveTrackUseCase(trackRepository)
 }

 @Singleton
 @Provides
 fun providesGetSavedTrackUseCase(
  trackRepository: TrackRepository
 ): GetSavedTrackUseCase{
  return GetSavedTrackUseCase(trackRepository)
 }

 @Singleton
 @Provides
 fun providesDeleteSavedTrackUseCase(
  trackRepository: TrackRepository
 ): DeleteSavedTrackUSeCase{
  return DeleteSavedTrackUSeCase(trackRepository)
 }

}