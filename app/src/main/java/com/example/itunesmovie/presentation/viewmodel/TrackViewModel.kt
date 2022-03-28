package com.example.itunesmovie.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.itunesmovie.data.model.Track
import com.example.itunesmovie.data.util.Resource
import com.example.itunesmovie.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.BiFunction
import javax.inject.Inject

/**
 * A ViewModel class that will handles data needed
 *
 * @param getTracksUseCase  use to get list of Tracks from the server
 * @param getSearchTracksUseCase use to search list of Tracks from the server
 * @param saveTrackUseCase use to save Track locally
 * @param getSavedTrackUseCase use to get list of Tracks that was stored locally
 * @param deleteSavedTrackUSeCaseUseCase use to delete specific track that was save locally
 */
@HiltViewModel
class TrackViewModel @Inject constructor(
 private val getTracksUseCase: GeTracksUseCase,
 private val getSearchTracksUseCase: GetSearchTracksUseCase,
 private val saveTrackUseCase: SaveTrackUseCase,
 private val getSavedTrackUseCase: GetSavedTrackUseCase,
 private val deleteSavedTrackUSeCaseUseCase: DeleteSavedTrackUSeCase,
) : ViewModel() {

 private val compositeDisposable = CompositeDisposable()
 private val searchDisposableList = ArrayList<Disposable>()

 private val tracksLiveData by lazy {
  val fromLocal = getSavedTrackUseCase.execute()
  val fromAPI = getTracksUseCase.execute()
   .map { result -> result.tracks }
  return@lazy processData(fromAPI, fromLocal)
 }

 private fun searchTracksLiveData(fromAPI: Observable<List<Track>>): LiveData<Resource<List<Track>>> {
  val fromLocal = getSavedTrackUseCase.execute()
  return processData(fromAPI, fromLocal)
 }

 private fun deleteTrackLiveData(track: Track): LiveData<Resource<Boolean>> {
  val liveData = MutableLiveData<Resource<Boolean>>()
  val disposable = deleteSavedTrackUSeCaseUseCase.execute(track)
   .subscribe({
    liveData.postValue(Resource.Success(true))
   }, {
    liveData.postValue(Resource.Error(it.message))
   })
  compositeDisposable.add(disposable)
  return liveData
 }

 private fun saveTrackLiveData(track: Track): LiveData<Resource<Boolean>> {
  val liveData = MutableLiveData<Resource<Boolean>>()
  track.apply {
   this.isFavorite = true
  }
  val disposable = saveTrackUseCase.execute(track)
   .subscribe({
    liveData.postValue(Resource.Success(true))
   }, {
    liveData.postValue(Resource.Error(it.message))
   })
  compositeDisposable.add(disposable)

  return liveData
 }

 /**
  * Function to process data from API and data that was stored locally
  *
  * This will handles data properly so that duplication of data will be prevented.
  *
  */
 private fun processData(
  fromAPI: Observable<List<Track>>,
  fromLocal: Observable<List<Track>>,
 )
     : MutableLiveData<Resource<List<Track>>> {
  val liveData = MutableLiveData<Resource<List<Track>>>()
  Observable.combineLatest(
   fromLocal,
   fromAPI,
   BiFunction { l1, l2 ->
    return@BiFunction Pair(l1, l2)
   }
  )
   .subscribe(object : Observer<Pair<List<Track>, List<Track>>> {
    override fun onSubscribe(d: Disposable) {
     liveData.postValue(Resource.Loading())
     if (isSearchStarted) {
      searchDisposableList.add(d)
     }
     compositeDisposable.add(d)
    }

    override fun onNext(t: Pair<List<Track>, List<Track>>) {
     Log.i("DataResponses", "Emitting")
     val localResult = t.first
     val apiResult = t.second
     /**
      * Any action relating to localResult like saving and deleting stored
      * tracks will be captured here
      */
     apiResult.forEach { track ->
      track.isFavorite = false
     }
     if (localResult.isNullOrEmpty()) {
      liveData.postValue(Resource.Success(apiResult))
     } else {
      localResult.forEach { track ->
       apiResult.find { a -> a.trackId == track.trackId }
        .apply {
         this?.isFavorite = true
        }
      }
     }
     liveData.postValue(Resource.Success(apiResult))
    }

    override fun onError(e: Throwable) {
     liveData.postValue(Resource.Error("No Internet Connection"))
     fromLocal.subscribe({
      liveData.postValue(Resource.Success(it))
     }, {
      liveData.postValue(Resource.Error(it.message))
     })
    }

    override fun onComplete() {
     Log.i("Subscriber", "Subscriber")
    }

   }
   )
  return liveData
 }

 var trackId: Int? = null
 var isNavigatedToInfo = false
 var isSearchStarted = false


 /**
  * Function to process data from server and data that was stored locally
  *
  */

 fun getTracks() = tracksLiveData

 /**
  * Function that will search data from server
  * and also get data that was stored locally
  */
 fun getSearchTracks(term: String): LiveData<Resource<List<Track>>> {

  val fromAPI = if (term.isEmpty())
   getTracksUseCase.execute()
    .map { a -> a.tracks }
  else
   getSearchTracksUseCase.execute(term)
    .map { a -> a.tracks }

  return searchTracksLiveData(fromAPI)
 }

 /**
  * Function to save selected tracks
  */
 fun saveTrack(track: Track) = saveTrackLiveData(track)

 /**
  * Function to delete specific track that was stored locally
  */
 fun deleteSavedTrack(track: Track) = deleteTrackLiveData(track)

 /**
  * Function to dispose previous search
  */
 fun disposePreviousSearch() {
  if (searchDisposableList.size > 0) {
   searchDisposableList[0].dispose()
   searchDisposableList.removeAt(0)
  }
 }

 override fun onCleared() {
  compositeDisposable.dispose()
  compositeDisposable.clear()
  super.onCleared()
 }

}