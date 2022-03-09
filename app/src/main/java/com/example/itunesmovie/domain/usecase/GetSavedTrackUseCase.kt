package com.example.itunesmovie.domain.usecase

import com.example.itunesmovie.domain.repository.TrackRepository

class GetSavedTrackUseCase(private val trackRepository: TrackRepository) {
 fun execute() = trackRepository.getSavedTracks()
}