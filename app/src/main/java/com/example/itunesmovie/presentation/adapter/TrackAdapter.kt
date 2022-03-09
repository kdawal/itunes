package com.example.itunesmovie.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.itunesmovie.R
import com.example.itunesmovie.data.model.Track
import com.example.itunesmovie.databinding.ListItemBinding

class TrackAdapter: RecyclerView.Adapter <TrackAdapter.MovieViewHolder>() {
 private var trackList = ArrayList<Track>()

 fun setList(tracks: List<Track>){
  trackList.clear()
  trackList.addAll(tracks)
 }

 private var onItemClickListener: ((Track) -> Unit)? = null

 override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
  val listItemBinding = ListItemBinding
   .inflate(LayoutInflater.from(parent.context), parent, false)
  return MovieViewHolder(listItemBinding)
 }

 override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
  val movie = trackList[position]
  holder.bind(movie)
 }

 override fun getItemCount(): Int {
  return trackList.size
 }

 inner class MovieViewHolder(
  private val listItemBinding: ListItemBinding)
  : RecyclerView.ViewHolder(listItemBinding.root){

  fun bind(track: Track){
   listItemBinding.apply {
    trackNameTextView.text = track.trackName ?: root.context
     .getString(R.string.null_data)
    genreTextView.text = track.primaryGenreName ?: root.context
     .getString(R.string.null_data)
    priceTextView.text = root.context
     .getString(R.string.price, track.trackPrice
      ?: R.string.null_data)

    if(track.isFavorite == true){
    Log.i("favoriteTrack",track.trackName.toString())
     Glide.with(root)
      .load(R.drawable.ic_favorite_2)
      .into(favoriteIcon)
    }else{
     Glide.with(root)
      .load(R.drawable.ic_favorite_1)
      .into(favoriteIcon)
   }

    Glide.with(root)
     .load(track.artworkUrl100)
     .placeholder(R.drawable.ic_movie)
     .into(artworkImageView)
    root.setOnClickListener {

     onItemClickListener?.let {
      it(track)
     }
    }
   }
  }
 }

 fun setOnItemClickListener(listener: ((Track) -> Unit)){
  onItemClickListener = listener
 }

}