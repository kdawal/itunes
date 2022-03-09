package com.example.itunesmovie.data.api

import com.example.itunesmovie.data.model.APIResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query
interface TrackAPIService {

 @GET("/search")
 fun getTracks(
  @Query("term")
  term: String = "star",
  @Query("amp:country")
  country: String = "au",
  @Query("amp:media")
  media: String = "movie",
  @Query("amp:all")
  all: String =""
 ):Observable<APIResponse>
}