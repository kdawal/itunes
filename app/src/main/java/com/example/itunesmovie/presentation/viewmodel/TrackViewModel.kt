package com.example.itunesmovie.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.itunesmovie.data.model.Track
import com.example.itunesmovie.data.util.Resource
import com.example.itunesmovie.data.util.SearchModel
import com.example.itunesmovie.domain.usecase.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.functions.BiFunction

/**
 * A ViewModel class that will handles data needed
 *
 * @param getTracksUseCase  use to get list of Tracks from the server
 * @param getSearchTracksUseCase use to search list of Tracks from the server
 * @param saveTrackUseCase use to save Track locally
 * @param getSavedTrackUseCase use to get list of Tracks that was stored locally
 * @param deleteSavedTrackUSeCaseUseCase use to delete specific track that was save locally
 */
class TrackViewModel(
 private val getTracksUseCase: GeTracksUseCase,
 private val getSearchTracksUseCase: GetSearchTracksUseCase,
 private val saveTrackUseCase: SaveTrackUseCase,
 private val getSavedTrackUseCase: GetSavedTrackUseCase,
 private val deleteSavedTrackUSeCaseUseCase: DeleteSavedTrackUSeCase,
) : ViewModel() {

 private val compositeDisposable = CompositeDisposable()

 private val tracksLiveData: Map<Boolean, LiveData<Resource<List<Track>>>> =
  lazyMap { isNetworkAvailable ->
   Log.i("DataResponses", "Loading")
   var liveData = MutableLiveData<Resource<List<Track>>>()
   val fromLocal = getSavedTrackUseCase.execute()

   val fromAPI = getTracksUseCase.execute()
     .map { result -> result.tracks }

   liveData.postValue(Resource.Loading())

   if (isNetworkAvailable) {
    liveData = processData(fromAPI, fromLocal)
   } else {
    liveData.postValue(Resource.Error("No Internet Connection"))
    fromLocal.subscribe({
     liveData.postValue(Resource.Success(it))
    }, {
     liveData.postValue(Resource.Error(it.message))
    }
    )
   }

   return@lazyMap liveData
  }
 private val searchTracksLiveData: Map<SearchModel, LiveData<Resource<List<Track>>>> =
  lazyMap { searchModel ->
   Log.i("DataResponses", "Search Loading")
   var liveData = MutableLiveData<Resource<List<Track>>>()
   val fromLocal = getSavedTrackUseCase.execute()

   liveData.postValue(Resource.Loading())

   if (searchModel.isNetworkAvailable) {
    liveData = processData(searchModel.fromAPI, fromLocal)
   } else {
    liveData.postValue(Resource.Error("No Internet Connection"))
    fromLocal.subscribe({
     liveData.postValue(Resource.Success(it))
    }, {
     liveData.postValue(Resource.Error(it.message))
    }
    )
   }
   return@lazyMap liveData
  }
 private val deleteTrackLiveData: Map<Track, LiveData<Resource<Boolean>>> =
  lazyMap { track ->
   val liveData = MutableLiveData<Resource<Boolean>>()
   val disposable = deleteSavedTrackUSeCaseUseCase.execute(track)
    .subscribe({
     liveData.postValue(Resource.Success(true))
    }, {
     liveData.postValue(Resource.Error(it.message))
    })
   compositeDisposable.add(disposable)
   return@lazyMap liveData
  }
 private val saveTrackLiveData: Map<Track, LiveData<Resource<Boolean>>> =
  lazyMap { track ->
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

   return@lazyMap liveData
  }

 private fun <K, V> lazyMap(initializer: (K) -> V): Map<K, V> {
  val map = mutableMapOf<K, V>()
  return map.withDefault { key ->
   val newValue = initializer(key)
   map[key] = newValue
   return@withDefault newValue
  }
 }

 /**
  * Function to process data from API and data that was stored locally
  *
  * This will handles data properly so that duplication of data will be prevented.
  *
  */
 private fun processData(
  fromAPI: Observable<List<Track>>,
  fromLocal: Observable<List<Track>>)
 : MutableLiveData<Resource<List<Track>>>{
  val liveData = MutableLiveData<Resource<List<Track>>>()
  val disposable = Observable.combineLatest(
   fromLocal,
   fromAPI,
   BiFunction { l1, l2 ->
    return@BiFunction Pair(l1, l2)
   }
  )
   .subscribe({
    Log.i("DataResponses", "Emitting")
    val localResult = it.first
    val apiResult = it.second

    /**
     * Any action relating to localResult like saving and deleting stored
     * tracks will be captured here
     */
    if (localResult.isNullOrEmpty()) {
     liveData.postValue(Resource.Success(apiResult))
    } else {
     apiResult.forEach{ track ->
      track.isFavorite = false
     }
     localResult.forEach{ track ->
      apiResult.find { a -> a.trackId == track.trackId }
       .apply {
        this?.isFavorite = true
       }
     }
    }
    liveData.postValue(Resource.Success(apiResult))
   }, {
    liveData.postValue(Resource.Error(it.message))
   }
   )
  compositeDisposable.add(disposable)
  return liveData
 }
 var isNavigatedToInfo = false
 var isFirstTime = true
 val isNetworkAvailable = true


 /**
  * Function to process data from server and data that was stored locally
  *
  * @param isNetworkAvailable to check if network is available
  */

 fun getTracks(isNetworkAvailable: Boolean) = tracksLiveData.getValue(isNetworkAvailable)

 /**
  * Function that will search data from server
  * and also get data that was stored locally
  *
  * @param isNetworkAvailable to check if network is available
  */
 fun getSearchTracks(isNetworkAvailable: Boolean, term: String) : LiveData<Resource<List<Track>>>{
  val fromAPI = if(term.isEmpty())
   getTracksUseCase.execute()
   .map { a -> a.tracks }
  else
   getSearchTracksUseCase.execute(term )
    .map { a -> a.tracks }

  return searchTracksLiveData.getValue(SearchModel(isNetworkAvailable, fromAPI))
 }
 /**
  * Function to save selected tracks
  */
 fun saveTrack(track: Track) = saveTrackLiveData.getValue(track)

 /**
  * Function to delete specific track that was stored locally
  */
 fun deleteSavedTrack(track: Track) = deleteTrackLiveData.getValue(track)

 override fun onCleared() {
  compositeDisposable.dispose()
  compositeDisposable.clear()
  super.onCleared()
 }

}