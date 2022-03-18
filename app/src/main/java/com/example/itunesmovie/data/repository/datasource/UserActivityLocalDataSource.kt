package com.example.itunesmovie.data.repository.datasource

import com.example.itunesmovie.data.model.UserActivity
import io.reactivex.rxjava3.core.Observable

interface UserActivityLocalDataSource {
 fun saveUserActivity(userActivity: UserActivity)
 fun getUserActivity(): Observable<List<UserActivity>>
 fun updateUserActivity(lastAccessDate: String?, fragment: Int?, trackId: Int?)
}