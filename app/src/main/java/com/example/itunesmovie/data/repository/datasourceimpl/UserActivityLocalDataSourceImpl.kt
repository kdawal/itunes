package com.example.itunesmovie.data.repository.datasourceimpl

import com.example.itunesmovie.data.db.UserActivityDao
import com.example.itunesmovie.data.model.UserActivity
import com.example.itunesmovie.data.repository.datasource.UserActivityLocalDataSource
import io.reactivex.rxjava3.core.Observable

class UserActivityLocalDataSourceImpl (
 private val userActivityDao: UserActivityDao
): UserActivityLocalDataSource {
 override fun saveUserActivity(userActivity: UserActivity) {
  userActivityDao.saveUserActivity(userActivity)
 }

 override fun getUserActivity(): Observable<List<UserActivity>> {
  return userActivityDao.getUserActivity()
 }

 override fun updateUserActivity(lastAccessDate: String?, fragment: Int?, trackId: Int?) {
  userActivityDao.updateUserActivity(lastAccessDate, fragment, trackId)
 }
}