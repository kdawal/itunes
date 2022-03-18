package com.example.itunesmovie.presentation.viewmodel
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.itunesmovie.data.model.Track
import com.example.itunesmovie.data.model.UserActivity
import com.example.itunesmovie.data.util.Resource
import com.example.itunesmovie.domain.usecase.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.functions.BiFunction
import java.text.DateFormat
import java.util.*

class TrackViewModel(
 private val getTracksUseCase: GeTracksUseCase,
 private val getSearchTracksUseCase: GetSearchTracksUseCase,
 private val saveTrackUseCase: SaveTrackUseCase,
 private val getSavedTrackUseCase: GetSavedTrackUseCase,
 private val deleteSavedTrackUSeCaseUseCase: DeleteSavedTrackUSeCase,
 private val saveUserActivityUseCase: SaveUserActivityUseCase,
 private val getUserActivityUseCase: GetUserActivityUseCase,
 private val updateUserActivityUseCase: UpdateUserActivityUseCase
) : ViewModel() {
 private val compositeDisposable = CompositeDisposable()

 val trackListResult = MutableLiveData<Resource<List<Track>>>()
 val saveTrackResult = MutableLiveData<Resource<Boolean>>()
 val deleteSavedTrackResult = MutableLiveData<Resource<Boolean>>()
 val userActivityResult = MutableLiveData<Resource<UserActivity>>()

 var isNavigatedToInfo = false
 var isUserActivityChecked = false

 fun getTracks(hasInternetConnection: Boolean) {
  trackListResult.postValue(Resource.Loading())
  val fromLocal = getSavedTrackUseCase.execute()
  val fromAPI =  getTracksUseCase.execute()
   .map { result -> result.tracks }

  if(hasInternetConnection){
   processData(fromAPI, fromLocal)
  }else{
   trackListResult.postValue(Resource.Error("No Internet Connection"))
   fromLocal.subscribe({
    trackListResult.postValue(Resource.Success(it))
   },{
    trackListResult.postValue(Resource.Error(it.message))
   }
   )
  }

 }
 fun getSearchTracks(hasInternetConnection: Boolean, term: String) {
  trackListResult.postValue(Resource.Loading())
  val fromLocal = getSavedTrackUseCase.execute()
  val fromAPI =  getSearchTracksUseCase.execute(term)
   .map { result -> result.tracks }

  if(hasInternetConnection){
   processData(fromAPI, fromLocal)
  }else{
   trackListResult.postValue(Resource.Error("No Internet Connection"))
   fromLocal.subscribe({
    trackListResult.postValue(Resource.Success(it))
   },{
    trackListResult.postValue(Resource.Error(it.message))
   }
   )
  }
 }


 fun saveTrack(track: Track) {
  track.apply {
   this.isFavorite = true
  }
  val disposable = saveTrackUseCase.execute(track)
   .subscribe({
    saveTrackResult.postValue(Resource.Success(true))
   }, {
    saveTrackResult.postValue(Resource.Error(it.message))
   })
  compositeDisposable.add(disposable)
 }

 fun deleteSavedTrack(track: Track) {
  val disposable = deleteSavedTrackUSeCaseUseCase.execute(track)
   .subscribe({
    deleteSavedTrackResult.postValue(Resource.Success(true))
   }, {
    deleteSavedTrackResult.postValue(Resource.Error(it.message))
   })
  compositeDisposable.add(disposable)
 }

 fun getUserActivity(){
  Log.i("UserActivity","Get User Activity")
  val disposable = getUserActivityUseCase.execute()
   .subscribe({
    if(!it.isNullOrEmpty()){
     userActivityResult.postValue(Resource.Success(it.first()))
    }else{
     userActivityResult.postValue(Resource.Success(null))
    }
   },
    {
     userActivityResult.postValue(Resource.Error(it.message))
     Log.i("UserActivity","Get User Activity Failed")
    })
  compositeDisposable.add(disposable)
 }

 fun saveUserActivity(){
  val data = UserActivity(0,"",null,null)
  val disposable = saveUserActivityUseCase.execute(data)
   .subscribe(
    {
     Log.i("UserActivity","Saved Successfully")
    },{
     Log.e("UserActivity", it.message.toString())
    }
   )
  compositeDisposable.add(disposable)
 }

 fun updateUserActivity(fragment: Int?, trackId: Int?){
  val currentTime = Calendar.getInstance().time
  val formattedDate = DateFormat.getDateInstance(DateFormat.FULL).format(currentTime)
  val disposable = updateUserActivityUseCase.execute(formattedDate, fragment, trackId)
   .subscribe()
  compositeDisposable.add(disposable)

 }

 private fun processData(fromAPI: Observable<List<Track>>, fromLocal: Observable<List<Track>>){
  val disposable = Observable.combineLatest(
   fromLocal,
   fromAPI,
   BiFunction{l1, l2 ->
    return@BiFunction Pair(l1,l2)
   }
  )
   .subscribe({
    Log.i("Emmit", "Emitting")
    val localResult = it.first
    val apiResult = it.second

    if(localResult.isNullOrEmpty()){
     trackListResult.postValue(Resource.Success(apiResult))
    }else{
     for(localResultItem in localResult){
      apiResult.find { result ->
       result.trackId == localResultItem.trackId
      }
       .apply {
        this?.isFavorite = true
       }
     }
     trackListResult.postValue(Resource.Success(apiResult))

    }
   },{
    trackListResult.postValue(Resource.Error(it.message))
   }
   )
  compositeDisposable.add(disposable)
 }


 override fun onCleared() {
  compositeDisposable.dispose()
  compositeDisposable.clear()
  super.onCleared()
 }

}