package com.example.itunesmovie.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.itunesmovie.R
import com.example.itunesmovie.data.model.Track
import com.example.itunesmovie.data.util.Resource
import com.example.itunesmovie.databinding.FragmentTrackBinding
import com.example.itunesmovie.presentation.adapter.TrackAdapter
import com.example.itunesmovie.presentation.viewmodel.TrackViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class TrackFragment : Fragment() {
 private lateinit var trackFragmentMovieBinding: FragmentTrackBinding
 private lateinit var trackViewModel: TrackViewModel
 private lateinit var trackAdapter: TrackAdapter

 private var isSearchedStarted = false
 private var cacheSearchText = ""
 private val trackList = ArrayList<Track>()

 override fun onCreateView(
  inflater: LayoutInflater, container: ViewGroup?,
  savedInstanceState: Bundle?,
 ): View? {
  // Inflate the layout for this fragment
  Log.i("onCreateFragment", "Inn")
  return inflater.inflate(R.layout.fragment_track, container, false)
 }

 override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
  super.onViewCreated(view, savedInstanceState)
  trackFragmentMovieBinding = FragmentTrackBinding.bind(view)
  trackViewModel = (activity as MainActivity).trackViewModel
  trackAdapter = (activity as MainActivity).trackAdapter

  initRecyclerView()
  setupSearchView()
  setObservers()
  viewTrackList()

  //Pass selected track as bundle to the next fragment
  trackAdapter.setOnItemClickListener {
   val bundle = Bundle().apply {
    putSerializable("selected_track", it)
   }
   findNavController().navigate(
    R.id.action_trackFragment_to_infoFragment,
    bundle
   )
  }

 }

 private fun initRecyclerView() {

  val linearLayoutManager = object : LinearLayoutManager(activity) {
   override fun supportsPredictiveItemAnimations(): Boolean {
    return false
   }
  }
  trackFragmentMovieBinding.trackRecyclerView.apply {
   adapter = trackAdapter
   layoutManager = linearLayoutManager
  }
 }

 private fun viewTrackList() {
  if(!trackViewModel.isNavigatedToInfo) {
   if(cacheSearchText.isEmpty()){
    trackViewModel.getTracks()
    trackViewModel.getSavedTracks()
   }
  }
  trackViewModel.isNavigatedToInfo = false
 }

 private fun setObservers() {
  trackViewModel.trackListResult.observe(viewLifecycleOwner) { response ->
   when (response) {
    is Resource.Success -> {
     response.data.let {
      if (isSearchedStarted && it.isNullOrEmpty()) {
       displayNoDataFound()
       hideProgressBar()
      } else {
       hideNoDataFound()
       analyzeList()
      }
     }
    }
    is Resource.Error -> {
     hideProgressBar()
     response.message.let {
      Toast.makeText(activity, it.toString(), Toast.LENGTH_LONG).show()
     }
    }
    is Resource.Loading -> {
     displayProgressBar()
    }

   }
  }

  trackViewModel.getSavedTrackResult.observe(viewLifecycleOwner) { response ->
   when (response) {
    is Resource.Success -> {
     Log.i("SavedResult", response.data?.size.toString())
     analyzeList()
    }
    is Resource.Error -> {
     hideProgressBar()
     response.message.let {
      Toast.makeText(activity, it.toString(), Toast.LENGTH_LONG).show()
     }
    }
    is Resource.Loading -> {
     displayProgressBar()
    }
   }
  }

  trackViewModel.deleteSavedTrackResult.observe(viewLifecycleOwner) { response ->
   when (response) {
    is Resource.Success -> {
     trackViewModel.getTracks()
    }
    is Resource.Error -> {
     hideProgressBar()
     response.message.let {
      Toast.makeText(activity, it.toString(), Toast.LENGTH_LONG).show()
     }
    }
    is Resource.Loading -> {
     displayProgressBar()
    }
   }
  }
 }

 @SuppressLint("NotifyDataSetChanged")
 private fun analyzeList() {
  trackList.clear()
//  trackFragmentMovieBinding.trackRecyclerView.smoothScrollToPosition(0)
  Log.i("analyzeData","yes")
  val savedResult = trackViewModel.getSavedTrackResult.value?.data
  val trackResult = trackViewModel.trackListResult.value?.data

  if (savedResult.isNullOrEmpty()) {
   if (!trackResult.isNullOrEmpty()) {
    trackList.addAll(trackResult.toList())
    Log.i("SavedNull", "In")
   }
  } else if (trackResult.isNullOrEmpty()) {
   Log.i("TrackNull", "In")
   trackList.addAll(savedResult)
  } else if (!trackResult.isNullOrEmpty() && !savedResult.isNullOrEmpty()) {
   trackList.addAll(trackResult)
   Log.i("TrackList", trackList.size.toString())
   for (saveResultItem in savedResult) {
    trackList.find { track ->
     track.trackId == saveResultItem.trackId
    }.apply {
     this?.isFavorite = saveResultItem.isFavorite
    }
   }
  } else {
   Log.i("analyzeResult", "No Result")
  }
  trackAdapter.setList(trackList)
  trackAdapter.notifyDataSetChanged()
  hideProgressBar()
 }

 private fun setupSearchView() {
  isSearchedStarted = false
  if(cacheSearchText != ""){
   isSearchedStarted = true
  }

  trackFragmentMovieBinding.searchView.queryHint = getString(R.string.query_hint)
  Observable.create<String> { emitter ->
   trackFragmentMovieBinding.searchView.setOnQueryTextListener(
    object : SearchView.OnQueryTextListener {
     override fun onQueryTextSubmit(query: String?): Boolean {
      isSearchedStarted = true
      emitter.onNext(query!!)
      cacheSearchText = query
      return false
     }
     override fun onQueryTextChange(query: String?): Boolean {
      if (!emitter.isDisposed) {
       if (isSearchedStarted) {
        emitter.onNext(query!!)
       }
       isSearchedStarted = true
       cacheSearchText = query!!
      }
      return true
     }
    })
  }
   .debounce(1000, TimeUnit.MILLISECONDS)
   .subscribeOn(Schedulers.io())
   .distinctUntilChanged()
   .observeOn(AndroidSchedulers.mainThread())
   .subscribe(
    {
     trackViewModel.getSearchTracks(it)
    },
    {
     Log.e("SearchViewError", it.message.toString())
    }
   )
 }

 private fun displayProgressBar() {
  trackFragmentMovieBinding.progressBar.visibility = View.VISIBLE
 }

 private fun hideProgressBar() {
  trackFragmentMovieBinding.progressBar.visibility = View.GONE
 }

 private fun displayNoDataFound() {
  trackFragmentMovieBinding.dataResultTextView.visibility = View.VISIBLE
  trackFragmentMovieBinding.trackRecyclerView.visibility = View.GONE
 }

 private fun hideNoDataFound() {
  trackFragmentMovieBinding.dataResultTextView.visibility = View.GONE
  trackFragmentMovieBinding.trackRecyclerView.visibility = View.VISIBLE
 }

}
