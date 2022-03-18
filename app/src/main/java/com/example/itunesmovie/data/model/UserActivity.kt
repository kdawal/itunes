package com.example.itunesmovie.data.model

import android.os.Bundle
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
 tableName = "user_activity"
)
data class UserActivity(
 @PrimaryKey(autoGenerate = true)
 val id: Int,
 val lastAccessDate: String?,
 val lastAccessScreen: Int?,
 val trackId: Int?
): Serializable