package com.example.itunesmovie.domain.usecase

import com.example.itunesmovie.data.model.APIResponse
import com.example.itunesmovie.domain.repository.TrackRepository
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer

/**
 * A use case that request a data from the server
 */
class GeTracksUseCase(
 private val trackRepository: TrackRepository,
) {
  fun execute(): Observable<APIResponse> = trackRepository.getTracks()
}