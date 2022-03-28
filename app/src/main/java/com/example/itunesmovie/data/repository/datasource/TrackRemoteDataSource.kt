package com.example.itunesmovie.data.repository.datasource

import com.example.itunesmovie.data.model.APIResponse
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer

/**
 * Datasource for interacting with Retrofit API calls
 */
interface TrackRemoteDataSource {
  fun getTracks(): Observable<APIResponse>
  fun searchTracks(term: String): Observable<APIResponse>
}