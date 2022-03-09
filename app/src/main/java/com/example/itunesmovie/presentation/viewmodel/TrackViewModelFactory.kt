package com.example.itunesmovie.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.itunesmovie.domain.usecase.*

class TrackViewModelFactory(
 private val geTracksUseCase: GeTracksUseCase,
 private val getSearchTracksUseCase: GetSearchTracksUseCase,
 private val saveTrackUseCase: SaveTrackUseCase,
 private val getSavedTrackUseCase: GetSavedTrackUseCase,
 private val deleteSavedTrackUSeCase: DeleteSavedTrackUSeCase
): ViewModelProvider.Factory {
 override fun <T : ViewModel> create(modelClass: Class<T>): T {
  return TrackViewModel(
   geTracksUseCase,
   getSearchTracksUseCase,
   saveTrackUseCase,
   getSavedTrackUseCase,
   deleteSavedTrackUSeCase
  ) as T
 }
}