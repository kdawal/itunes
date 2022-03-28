package com.example.itunesmovie.data.model


import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Database table track.
 * Also a model for result data
 */
@Entity(
  tableName = "track"
)
data class Track(
    @SerializedName("artworkUrl100")
    val artworkUrl100: String?,
    @SerializedName("artworkUrl30")
    val artworkUrl30: String?,
    @SerializedName("artworkUrl60")
    val artworkUrl60: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("longDescription")
    val longDescription: String?,
    @SerializedName("primaryGenreName")
    val primaryGenreName: String?,
    @PrimaryKey
    @SerializedName("trackId")
    val trackId: Int?,
    @SerializedName("trackName")
    val trackName: String?,
    @SerializedName("trackPrice")
    val trackPrice: Double?,
    var isFavorite: Boolean?,
) : Serializable