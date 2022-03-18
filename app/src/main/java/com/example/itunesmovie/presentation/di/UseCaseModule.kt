package com.example.itunesmovie.presentation.di

import com.example.itunesmovie.domain.repository.TrackRepository
import com.example.itunesmovie.domain.repository.UserActivityRepository
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
  trackRepository: TrackRepository,
 ): GeTracksUseCase {
  return GeTracksUseCase(trackRepository)
 }

 @Singleton
 @Provides
 fun providesGetSearchTracksUseCase(
  trackRepository: TrackRepository,
 ): GetSearchTracksUseCase {
  return GetSearchTracksUseCase(trackRepository)
 }

 @Singleton
 @Provides
 fun providesSavedTrackUseCase(
  trackRepository: TrackRepository,
 ): SaveTrackUseCase {
  return SaveTrackUseCase(trackRepository)
 }

 @Singleton
 @Provides
 fun providesGetSavedTrackUseCase(
  trackRepository: TrackRepository,
 ): GetSavedTrackUseCase {
  return GetSavedTrackUseCase(trackRepository)
 }

 @Singleton
 @Provides
 fun providesDeleteSavedTrackUseCase(
  trackRepository: TrackRepository,
 ): DeleteSavedTrackUSeCase {
  return DeleteSavedTrackUSeCase(trackRepository)
 }

 @Singleton
 @Provides
 fun providesSaveUserActivityUseCase(
  userActivityRepository: UserActivityRepository,
 ): SaveUserActivityUseCase {
  return SaveUserActivityUseCase(userActivityRepository)
 }

 @Singleton
 @Provides
 fun providesGetUserActivityUseCase(
  userActivityRepository: UserActivityRepository
 ): GetUserActivityUseCase{
  return GetUserActivityUseCase(userActivityRepository)
 }

 @Singleton
 @Provides
 fun providesUpdateUserActivityUseCase(
  userActivityRepository: UserActivityRepository
 ): UpdateUserActivityUseCase{
  return UpdateUserActivityUseCase(userActivityRepository)
 }


}