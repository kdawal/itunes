package com.example.itunesmovie.domain.repository

import com.example.itunesmovie.data.model.UserActivity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

interface UserActivityRepository {
 fun saveUserActivity(userActivity: UserActivity): Completable
 fun getUserActivity(): Observable<List<UserActivity>>
 fun updateUserActivity(lastAccessDate: String?, fragment: Int?, trackId: Int? ): Completable
}