package com.example.itunesmovie.domain.usecase

import com.example.itunesmovie.data.model.APIResponse
import com.example.itunesmovie.domain.repository.TrackRepository
import io.reactivex.rxjava3.core.Observable

class GetSearchTracksUseCase(private val trackRepository: TrackRepository) {
 fun execute(term: String): Observable<APIResponse> = trackRepository.getSearchTrack(term)
}