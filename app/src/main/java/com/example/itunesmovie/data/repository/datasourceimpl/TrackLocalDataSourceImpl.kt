package com.example.itunesmovie.data.repository.datasourceimpl

import com.example.itunesmovie.data.db.TrackDao
import com.example.itunesmovie.data.model.Track
import com.example.itunesmovie.data.repository.datasource.TrackLocalDataSource
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

/**
 * Implements local datasource for interacting with DAO
 */
class TrackLocalDataSourceImpl(
  private val trackDao: TrackDao,
) : TrackLocalDataSource {
  override fun saveTrack(track: Track) {
    trackDao.saveTrack(track)
  }

  override fun getSavedTrack(): Observable<List<Track>> {
    return trackDao.getAllTracks()
  }

  override fun deleteSavedTrack(track: Track) {
    trackDao.deleteTrack(track)
  }
}