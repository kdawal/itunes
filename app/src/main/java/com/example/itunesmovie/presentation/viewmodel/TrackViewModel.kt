package com.example.itunesmovie.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.itunesmovie.data.model.APIResponse
import com.example.itunesmovie.data.model.Track
import com.example.itunesmovie.data.util.Resource
import com.example.itunesmovie.domain.usecase.*
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

class TrackViewModel(
 private val getTracksUseCase: GeTracksUseCase,
 private val getSearchTracksUseCase: GetSearchTracksUseCase,
 private val saveTrackUseCase: SaveTrackUseCase,
 private val getSavedTrackUseCase: GetSavedTrackUseCase,
 private val deleteSavedTrackUSeCaseUseCase: DeleteSavedTrackUSeCase
): ViewModel() {
 private val compositeDisposable = CompositeDisposable()

 val trackListResult = MutableLiveData<Resource<List<Track>>>()
 val getSavedTrackResult = MutableLiveData<Resource<List<Track>>>()
 val saveTrackResult = MutableLiveData<Resource<Boolean>>()
 val deleteSavedTrackResult = MutableLiveData<Resource<Boolean>>()

 var isNavigatedToInfo = false

 fun getTracks(){
  getTracksUseCase.execute()
   .subscribe(getTracksObserver)
 }

 fun getSearchTracks(term: String){
  getSearchTracksUseCase.execute(term)
   .subscribe(getSearchTracksObserver)
 }

 fun getSavedTracks(){
  getSavedTrackUseCase.execute()
   .subscribe(getSavedTracksObserver)
 }

 fun saveTrack(track: Track){
  track.apply {
   this.isFavorite = true
  }

  val disposable = saveTrackUseCase.execute(track)
   .subscribe({
    saveTrackResult.postValue(Resource.Success(true))
   },{
    saveTrackResult.postValue(Resource.Error(it.message))
   })
  compositeDisposable.add(disposable)
 }

 fun deleteSavedTrack(track: Track){
  val disposable = deleteSavedTrackUSeCaseUseCase.execute(track)
   .subscribe({
    deleteSavedTrackResult.postValue(Resource.Success(true))
   },{
    deleteSavedTrackResult.postValue(Resource.Error(it.message))
   })
  compositeDisposable.add(disposable)
 }

 private val getTracksObserver = object: Observer<APIResponse>{
  override fun onSubscribe(d: Disposable) {
   compositeDisposable.add(d)
   trackListResult.postValue(Resource.Loading())
  }

  override fun onNext(t: APIResponse) {
   trackListResult.postValue(Resource.Success(t.tracks))
  }

  override fun onError(e: Throwable) {
   trackListResult.postValue(Resource.Error(e.toString()))
  }

  override fun onComplete() {
  }
 }
 private val getSearchTracksObserver = object: Observer<APIResponse>{
  override fun onSubscribe(d: Disposable) {
   compositeDisposable.add(d)
   trackListResult.postValue(Resource.Loading())
  }

  override fun onNext(t: APIResponse) {
   trackListResult.postValue(Resource.Success(t.tracks))
  }

  override fun onError(e: Throwable) {
   trackListResult.postValue((Resource.Error(e.toString())))
  }

  override fun onComplete() {
  }

 }
 private val getSavedTracksObserver = object: Observer<List<Track>>{
  override fun onSubscribe(d: Disposable) {
   compositeDisposable.add(d)
   getSavedTrackResult.postValue(Resource.Loading())
  }

  override fun onNext(t: List<Track>) {
   getSavedTrackResult.postValue(Resource.Success(t))
  }

  override fun onError(e: Throwable) {
   getSavedTrackResult.postValue(Resource.Error(e.message))
  }

  override fun onComplete() {
  }

 }

 override fun onCleared() {
  compositeDisposable.dispose()
  compositeDisposable.clear()
  super.onCleared()
 }

}