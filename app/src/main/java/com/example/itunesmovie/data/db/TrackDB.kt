package com.example.itunesmovie.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.itunesmovie.data.model.Track


@Database(
 entities = [Track::class],
 version = 1,
 exportSchema = false)
abstract class TrackDB: RoomDatabase() {
abstract fun getDTrackDao(): TrackDao
}