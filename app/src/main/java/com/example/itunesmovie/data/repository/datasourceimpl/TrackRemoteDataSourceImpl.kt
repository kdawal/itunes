package com.example.itunesmovie.data.repository.datasourceimpl

import com.example.itunesmovie.data.api.TrackAPIService
import com.example.itunesmovie.data.model.APIResponse
import com.example.itunesmovie.data.repository.datasource.TrackRemoteDataSource
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.schedulers.Schedulers

class TrackRemoteDataSourceImpl(
 private val trackAPIService: TrackAPIService
): TrackRemoteDataSource {
 override fun getTracks(): Observable<APIResponse> {
  return trackAPIService.getTracks()
 }

 override fun searchTracks(term: String): Observable<APIResponse> {
  return trackAPIService.getTracks(term)
 }
}