package com.example.itunesmovie.data.repository

import com.example.itunesmovie.data.model.UserActivity
import com.example.itunesmovie.data.repository.datasource.UserActivityLocalDataSource
import com.example.itunesmovie.domain.repository.UserActivityRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

class UserActivityRepositoryImpl(
 private val userActivityLocalDataSource: UserActivityLocalDataSource
): UserActivityRepository {
 override fun saveUserActivity(userActivity: UserActivity): Completable {
  return Completable.fromAction {
   userActivityLocalDataSource.saveUserActivity(userActivity)
  }
   .subscribeOn(Schedulers.io())
   .observeOn(AndroidSchedulers.mainThread())

 }

 override fun getUserActivity(): Observable<List<UserActivity>> {
  return userActivityLocalDataSource.getUserActivity()
   .subscribeOn(Schedulers.io())
   .observeOn(AndroidSchedulers.mainThread())
 }

 override fun updateUserActivity(lastAccessDate: String?, fragment: Int?, trackId: Int?): Completable {
  return Completable.fromAction {
   userActivityLocalDataSource.updateUserActivity(lastAccessDate, fragment, trackId)
  }
   .subscribeOn(Schedulers.io())
   .observeOn(AndroidSchedulers.mainThread())
 }
}