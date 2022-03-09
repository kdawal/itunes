package com.example.itunesmovie.presentation

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.itunesmovie.R
import com.example.itunesmovie.data.model.Track
import com.example.itunesmovie.data.util.Resource
import com.example.itunesmovie.databinding.FragmentInfoBinding
import com.example.itunesmovie.presentation.viewmodel.TrackViewModel
import com.google.android.material.snackbar.Snackbar

class InfoFragment : Fragment() {
 private lateinit var infoFragmentBinding: FragmentInfoBinding
 private lateinit var trackViewModel: TrackViewModel
 override fun onCreateView(
  inflater: LayoutInflater, container: ViewGroup?,
  savedInstanceState: Bundle?,
 ): View? {

  // Inflate the layout for this fragment
  return inflater.inflate(R.layout.fragment_info, container, false)
 }


 override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
  super.onViewCreated(view, savedInstanceState)
  trackViewModel = (activity as MainActivity).trackViewModel
  infoFragmentBinding = FragmentInfoBinding.bind(view)
  val args: InfoFragmentArgs by navArgs()
  val track = args.selectedTrack
  setupViews(track)
  setUpFloatingActionButton(track, view)
  trackViewModel.isNavigatedToInfo = true
 }

 private fun setupViews(track: Track){
  Log.i("InfoFragmentData", track.isFavorite.toString())
  infoFragmentBinding.apply {
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

  infoFragmentBinding.saveFloatingActionButton.apply {

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
       trackViewModel.getSavedTracks()
       Snackbar.make(view, "Saved Successfully", Snackbar.LENGTH_LONG).show()
       setImageResource(R.drawable.ic_favorite_2)
       hideProgressBar()
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
       trackViewModel.getSavedTracks()
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
 private fun displayProgressBar() {
  infoFragmentBinding.progressBar.visibility = View.VISIBLE
 }

 private fun hideProgressBar() {
  infoFragmentBinding.progressBar.visibility = View.GONE
 }
}