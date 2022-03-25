package com.example.itunesmovie.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.itunesmovie.R
import com.example.itunesmovie.data.model.Track
import com.example.itunesmovie.databinding.ListItemBinding

/**
 * Handles data that will be displayed on the recylcerview.
*/

class TrackAdapter: RecyclerView.Adapter <TrackAdapter.MovieViewHolder>() {
 private val trackList = ArrayList<Track>()

 fun setList(tracks: List<Track>){
  trackList.clear()
  trackList.addAll(tracks)
 }

 /**
  * Use for setting onClickListener for each item
  */
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

  /**
   * Function that gives data to to be displayed in recyclerview
   *
   * @param Track data needed for displaying item in recyclerview
   */
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

 /**
  * A public function for setting onClickListener for each item
  *
  * @param listener with Track as reference data
  */
 fun setOnItemClickListener(listener: ((Track) -> Unit)){
  onItemClickListener = listener
 }

}