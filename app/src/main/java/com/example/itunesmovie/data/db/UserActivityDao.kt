package com.example.itunesmovie.data.db

import androidx.room.*
import com.example.itunesmovie.data.model.UserActivity
import io.reactivex.rxjava3.core.Observable

@Dao
interface UserActivityDao {

 @Insert(onConflict = OnConflictStrategy.REPLACE)
 fun saveUserActivity(userActivity: UserActivity)

 @Query("SELECT * FROM user_activity")
 fun getUserActivity(): Observable<List<UserActivity>>

 @Query("UPDATE user_activity SET lastAccessDate = :lastAccessDate, lastAccessScreen = :fragment, trackId = :trackId")
 fun updateUserActivity(lastAccessDate: String?, fragment: Int?, trackId: Int?)
}