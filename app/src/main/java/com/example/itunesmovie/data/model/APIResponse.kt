package com.example.itunesmovie.data.model


import com.google.gson.annotations.SerializedName

/**
 * API response model for fetching data
 */
data class APIResponse(
    @SerializedName("resultCount")
    val resultCount: Int,
    @SerializedName("results")
    val tracks: List<Track>,
)