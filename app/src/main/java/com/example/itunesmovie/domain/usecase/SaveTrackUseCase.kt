package com.example.itunesmovie.domain.usecase

import com.example.itunesmovie.data.model.Track
import com.example.itunesmovie.domain.repository.TrackRepository

class SaveTrackUseCase(private val trackRepository: TrackRepository) {
 fun execute(track: Track) = trackRepository.saveTrack(track)
}