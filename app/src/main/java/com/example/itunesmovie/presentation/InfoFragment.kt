package com.example.itunesmovie.presentation

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.itunesmovie.R
import com.example.itunesmovie.data.model.Track
import com.example.itunesmovie.data.util.Resource
import com.example.itunesmovie.databinding.FragmentInfoBinding
import com.example.itunesmovie.presentation.base.BaseFragment
import com.example.itunesmovie.presentation.viewmodel.TrackViewModel
import com.google.android.material.snackbar.Snackbar
import java.text.DateFormat
import java.util.*

class InfoFragment : BaseFragment<FragmentInfoBinding>(
 FragmentInfoBinding::inflate
) {
 private lateinit var trackViewModel: TrackViewModel
 private lateinit var track: Track
 private lateinit var sharedPreferences: SharedPreferences

 override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
  super.onViewCreated(view, savedInstanceState)
  trackViewModel = (activity as MainActivity).trackViewModel
  val args: InfoFragmentArgs by navArgs()
  track = args.selectedTrack
  setupViews(track)
  setUpFloatingActionButton(track, view)
  trackViewModel.isNavigatedToInfo = true
  sharedPreferences = (activity as MainActivity).sharedPreferences
 }

 /**
  * Function to load data into designated views
  *
  * @param track Track data
  */
 private fun setupViews(track: Track) {
  Log.i("InfoFragmentData", track.isFavorite.toString())
  binding?.apply {
   trackNameTextView.text = track.trackName ?: getString(R.string.null_data)
   genreTextView.text = track.primaryGenreName ?: getString(R.string.null_data)
   priceTextVIew.text = getString(R.string.price, track.trackPrice ?: getString(R.string.null_data))
   contentDescriptionTextView.text = track.longDescription ?: getString(R.string.no_description)

   Glide.with(root)
    .load(track.artworkUrl100)
    .placeholder(R.drawable.ic_movie)
    .into(imageView)

  }
 }

 /**
  * Function to setup FloatingActionButton
  *
  * @param track Track data
  */
 private fun setUpFloatingActionButton(track: Track, view: View) {

  binding?.saveFloatingActionButton?.apply {

   if (track.isFavorite == true) {
    setImageResource(R.drawable.ic_favorite_2)
   }
   setOnClickListener {
    if (track.isFavorite == true) {
     Log.i("FavoriteTrack", track.trackName.toString())

     /**
      * Execute delete selected track
      *
      * Included here is the undo function
      */
     trackViewModel.deleteSavedTrack(track)
      .observe(viewLifecycleOwner) { response ->
       when (response) {
        is Resource.Success -> {
         Snackbar.make(view, "Deleted Successfully", Snackbar.LENGTH_LONG)
          .apply {
           setAction("Undo") {
            trackViewModel.saveTrack(track)
            setImageResource(R.drawable.ic_favorite_2)
           }
          }.show()
         setImageResource(R.drawable.ic_favorite_1)
         hideProgressBar()
        }
        is Resource.Error -> {
         hideProgressBar()
         Snackbar.make(view, "Unable to Delete Track", Snackbar.LENGTH_LONG).show()
        }
        is Resource.Loading -> {
         displayProgressBar()
        }
       }
      }
    } else {

     /**
      * Execute save selected track
      */
     trackViewModel.saveTrack(track)
      .observe(viewLifecycleOwner) { response ->
       when (response) {
        is Resource.Success -> {
         Snackbar.make(view, "Saved Successfully", Snackbar.LENGTH_LONG).show()
         setImageResource(R.drawable.ic_favorite_2)
        }
        is Resource.Error -> {
         hideProgressBar()
         Snackbar.make(view, "Unable to Save Track", Snackbar.LENGTH_LONG).show()
        }
        is Resource.Loading -> {
         displayProgressBar()
        }
       }
      }
    }
   }
  }
 }


 override fun displayProgressBar() {
  binding?.progressBar?.visibility = View.VISIBLE
 }

 override fun hideProgressBar() {
  binding?.progressBar?.visibility = View.GONE
 }

 /**
  * Saved last screen when the app stop
  */
 override fun onStop() {
  super.onStop()
  val currentTime = Calendar.getInstance().time
  val formattedDate = DateFormat.getDateInstance(DateFormat.FULL).format(currentTime)

  val editor = sharedPreferences.edit()
  val fragment = findNavController().currentDestination?.id
  val trackId = track.trackId ?: 0
  editor.putInt("fragment", fragment!!)
  editor.putInt("trackId", trackId)
  editor.putString("date", formattedDate)
  editor.apply()

 }

 override fun onDestroyView() {
  super.onDestroyView()
  Log.i("OnDestroyView", "Destroying View")

 }

}