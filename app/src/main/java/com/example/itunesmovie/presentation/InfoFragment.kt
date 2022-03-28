package com.example.itunesmovie.presentation

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
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
 private lateinit var track: Track
 private val trackViewModel by activityViewModels<TrackViewModel>()

 override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
  super.onViewCreated(view, savedInstanceState)
  val args: InfoFragmentArgs by navArgs()
  track = args.selectedTrack
  setupViews(track)
  setUpFloatingActionButton(track, view)
  trackViewModel.trackId = track.trackId
  trackViewModel.isNavigatedToInfo = true
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

  binding?.saveButton?.apply {
   if (track.isFavorite == true) {
    setButtonToRemove()
   }
   setOnClickListener {
    if (track.isFavorite == true) {
     /**
      * Execute delete selected track
      *
      * Included here is the undo function
      */
     trackViewModel.deleteSavedTrack(track)
      .observe(viewLifecycleOwner) { response ->
       when (response) {
        is Resource.Success -> {
         Snackbar.make(view, "Removed Successfully", Snackbar.LENGTH_LONG)
          .apply {
           setAction("Undo") {
            trackViewModel.saveTrack(track)
            setButtonToRemove()

           }
          }.show()
         setButtonToAdd()
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
         setButtonToRemove()
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
  * Set button properties if selected track is
  * marked as favorite or not
  */
 private fun setButtonToAdd(){
  binding?.saveButton?.apply {
   setBackgroundColor(ContextCompat.getColor(context, R.color.white))
   text = context.getString(R.string.Add)
   setTextColor(ContextCompat.getColor(context, R.color.pink))
  }
 }

 private fun setButtonToRemove(){
  binding?.saveButton?.apply {
   setBackgroundColor(ContextCompat.getColor(context, R.color.pink))
   text = context.getString(R.string.Remove)
   setTextColor(ContextCompat.getColor(context, R.color.white))
  }
 }


}