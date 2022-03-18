package com.example.itunesmovie.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.itunesmovie.data.model.Track
import com.example.itunesmovie.data.model.UserActivity


@Database(
 entities = [Track::class, UserActivity::class],
 version = 1,
 exportSchema = false)
abstract class TrackDB: RoomDatabase() {
abstract fun getTrackDao(): TrackDao
abstract fun getUserActivityDao(): UserActivityDao
}