package com.example.itunesmovie.data.model


import com.google.gson.annotations.SerializedName

data class APIResponse(
    @SerializedName("resultCount")
    val resultCount: Int,
    @SerializedName("results")
    val tracks: List<Track>
)