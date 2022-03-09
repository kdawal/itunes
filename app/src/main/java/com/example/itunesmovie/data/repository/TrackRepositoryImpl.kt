package com.example.itunesmovie.data.repository

import com.example.itunesmovie.data.model.APIResponse
import com.example.itunesmovie.data.model.Track
import com.example.itunesmovie.data.repository.datasource.TrackLocalDataSource
import com.example.itunesmovie.data.repository.datasource.TrackRemoteDataSource
import com.example.itunesmovie.domain.repository.TrackRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers

class TrackRepositoryImpl(
 private val trackRemoteDataSource: TrackRemoteDataSource,
 private val trackLocalDataSource: TrackLocalDataSource
): TrackRepository {
 override fun getTracks(): Observable<APIResponse> {
   return trackRemoteDataSource.getTracks()
    .subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread())
 }

 override fun getSearchTrack(term: String): Observable<APIResponse> {
  return trackRemoteDataSource.searchTracks(term)
   .subscribeOn(Schedulers.io())
   .observeOn(AndroidSchedulers.mainThread())
 }

 override fun saveTrack(track: Track): Completable {
  return Completable.fromAction{
   trackLocalDataSource.saveTrack(track)
  }.subscribeOn(Schedulers.io())
   .observeOn(AndroidSchedulers.mainThread())
 }

 override fun getSavedTracks(): Observable<List<Track>> {
  return trackLocalDataSource.getSavedTrack()
   .subscribeOn(Schedulers.io())
   .observeOn(AndroidSchedulers.mainThread())
 }

 override fun deleteSavedTack(track: Track): Completable {
  return Completable.fromAction{
   trackLocalDataSource.deleteSavedTrack(track)
  }.subscribeOn(Schedulers.io())
   .observeOn(AndroidSchedulers.mainThread())
 }

}