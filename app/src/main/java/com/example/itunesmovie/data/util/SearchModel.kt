package com.example.itunesmovie.data.util

import com.example.itunesmovie.data.model.Track
import io.reactivex.rxjava3.core.Observable

class SearchModel(
 val isNetworkAvailable: Boolean,
 val fromAPI: Observable<List<Track>>
)