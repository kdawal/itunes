package com.example.itunesmovie.domain.repository

import com.example.itunesmovie.data.model.APIResponse
import com.example.itunesmovie.data.model.Track
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.core.Single
/**
 * Repository layout where functions needed for implementing use cases found.
 */
interface TrackRepository {
fun getTracks(): Observable<APIResponse>
fun getSearchTrack(term: String): Observable<APIResponse>
fun saveTrack(track: Track): Completable
fun getSavedTracks(): Observable<List<Track>>
fun deleteSavedTack(track: Track): Completable

}