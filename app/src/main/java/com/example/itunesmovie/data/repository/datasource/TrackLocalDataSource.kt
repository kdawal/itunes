package com.example.itunesmovie.data.repository.datasource

import com.example.itunesmovie.data.model.Track
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer

interface TrackLocalDataSource {
 fun saveTrack(track: Track)
 fun getSavedTrack(): Observable<List<Track>>
 fun deleteSavedTrack(track: Track)
}