package com.example.itunesmovie.domain.usecase

import com.example.itunesmovie.data.model.Track
import com.example.itunesmovie.domain.repository.TrackRepository
import io.reactivex.rxjava3.core.Observable

/**
 * A use case that get data that was saved locally
 * */
class GetSavedTrackUseCase(private val trackRepository: TrackRepository) {
  fun execute(): Observable<List<Track>> = trackRepository.getSavedTracks()
}