package com.example.itunesmovie.domain.usecase

import com.example.itunesmovie.data.model.Track
import com.example.itunesmovie.domain.repository.TrackRepository

/**
 *  A use case that deletes the data that was saved locally
 */
class DeleteSavedTrackUSeCase(private val trackRepository: TrackRepository) {
  fun execute(track: Track) = trackRepository.deleteSavedTack(track)
}