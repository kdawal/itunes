package com.example.itunesmovie.presentation

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

class InfoFragment : BaseFragment<FragmentInfoBinding>(
 FragmentInfoBinding::inflate
) {
 private lateinit var trackViewModel: TrackViewModel
 private lateinit var track: Track


 override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
  super.onViewCreated(view, savedInstanceState)
  trackViewModel = (activity as MainActivity).trackViewModel
  val args: InfoFragmentArgs by navArgs()
  track = args.selectedTrack
  setupViews(track)
  setUpFloatingActionButton(track, view)
  trackViewModel.isNavigatedToInfo = true
 }

 private fun setupViews(track: Track){
  Log.i("InfoFragmentData", track.isFavorite.toString())
  binding?.apply {
   trackNameTextView.text = track.trackName ?: getString(R.string.null_data)
   genreTextView.text = track.primaryGenreName ?: getString(R.string.null_data)
   priceTextVIew.text = getString(R.string.price, track.trackPrice ?:
   getString(R.string.null_data))
   contentDescriptionTextView.text = track.longDescription ?:
   getString(R.string.no_description)

   Glide.with(root)
    .load(track.artworkUrl100)
    .placeholder(R.drawable.ic_movie)
    .into(imageView)

  }
 }

 private fun setUpFloatingActionButton(track: Track, view: View) {

  binding?.saveFloatingActionButton?.apply {

   if (track.isFavorite == true) {
    setImageResource(R.drawable.ic_favorite_2)
   }

   setOnClickListener {
    if (track.isFavorite == true) {
     Log.i("FavoriteTrack", track.trackName.toString())
     trackViewModel.deleteSavedTrack(track)
    } else {
     trackViewModel.saveTrack(track)
    }

    trackViewModel.saveTrackResult.observe(viewLifecycleOwner) { response ->
     when (response) {
      is Resource.Success -> {
       Snackbar.make(view, "Saved Successfully", Snackbar.LENGTH_LONG).show()
//       trackViewModel.getTracks(true)
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

    trackViewModel.deleteSavedTrackResult.observe(viewLifecycleOwner) { response ->
     when (response) {
      is Resource.Success -> {
//       trackViewModel.getTracks(true)
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
   }
  }
 }

 override fun displayProgressBar() {
  binding?.progressBar?.visibility = View.VISIBLE
 }

 override fun hideProgressBar() {
  binding?.progressBar?.visibility = View.GONE
 }

 override fun onStop() {
  super.onStop()
  trackViewModel.updateUserActivity(findNavController().currentDestination?.id, track.trackId)
 }

 override fun onDestroyView() {
  super.onDestroyView()
  Log.i("OnDestroyView", "Destroying View")

 }

}