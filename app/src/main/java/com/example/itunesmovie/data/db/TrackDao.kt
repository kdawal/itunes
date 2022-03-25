package com.example.itunesmovie.data.db

import androidx.room.*
import com.example.itunesmovie.data.model.Track
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.DELETE
/**
 * Data Access Object where you can write your SQLite query
 */
@Dao
interface TrackDao {

 @Insert(onConflict = OnConflictStrategy.REPLACE)
 fun saveTrack(track: Track)

 @Query("SELECT * FROM track")
 fun getAllTracks(): Observable<List<Track>>

 @Delete
 fun deleteTrack(track: Track)
}