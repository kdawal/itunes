package com.example.itunesmovie.presentation

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.itunesmovie.R
import com.example.itunesmovie.data.util.Resource
import com.example.itunesmovie.databinding.FragmentTrackBinding
import com.example.itunesmovie.presentation.adapter.HeaderAdapter
import com.example.itunesmovie.presentation.adapter.TrackAdapter
import com.example.itunesmovie.presentation.base.BaseFragment
import com.example.itunesmovie.presentation.viewmodel.TrackViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.text.DateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class TrackFragment : BaseFragment<FragmentTrackBinding>(
 FragmentTrackBinding::inflate
) {
 private lateinit var trackViewModel: TrackViewModel
 private lateinit var trackAdapter: TrackAdapter
 private lateinit var headerAdapter: HeaderAdapter
 private lateinit var sharedPreferences: SharedPreferences
 private var searchTerm: String = ""

 override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
  super.onViewCreated(view, savedInstanceState)
  trackViewModel = (activity as MainActivity).trackViewModel
  trackAdapter = (activity as MainActivity).trackAdapter
  headerAdapter = (activity as MainActivity).headerAdapter
  sharedPreferences = (activity as MainActivity).sharedPreferences

  /**
   * Pass selected track as bundle to the next fragment
   */
  trackAdapter.setOnItemClickListener {
   val bundle = Bundle().apply {
    putSerializable("selected_track", it)
   }
   findNavController().navigate(
    R.id.action_trackFragment_to_infoFragment,
    bundle
   )
  }
  initRecyclerView()
  viewTrackList()
  setupSearchView()
 }


 /**
  * Initialized RecyclerView
  */
 @SuppressLint("NotifyDataSetChanged")
 private fun initRecyclerView() {
  binding?.trackRecyclerView?.apply {
   adapter = ConcatAdapter(headerAdapter, trackAdapter)
   layoutManager = LinearLayoutManager(activity)
  }
 }

 /**
  * Load data
  */
 @SuppressLint("NotifyDataSetChanged")
 private fun viewTrackList() {
  trackViewModel.getTracks()
   .observe(viewLifecycleOwner) { result ->
    when (result) {
     is Resource.Success -> {
      if (result.data.isNullOrEmpty()) {
       displayNoDataFound()
      } else {
       hideNoDataFound()
       if (!trackViewModel.isSearchStarted) {
        Log.i("DataReflected", "List")
        trackAdapter.setList(result.data)
        trackAdapter.notifyDataSetChanged()
       }

      }
      hideProgressBar()
     }
     is Resource.Loading -> {
      Log.i("DataReflected", "List Loading")
      displayProgressBar()
     }
     is Resource.Error -> {
      hideProgressBar()
      Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
     }
    }
   }
 }


 /**
  * This function will setup the SearchView
  *
  * Handles the SearchView Query Listener
  * Uses Observable for better searching experience
  */
 @SuppressLint("NotifyDataSetChanged")
 private fun setupSearchView() {
  binding?.searchView?.queryHint = getString(R.string.query_hint)
  Observable.create<String> { emitter ->
   binding?.searchView?.setOnQueryTextListener(
    object : SearchView.OnQueryTextListener {
     override fun onQueryTextSubmit(query: String?): Boolean {
      emitter.onNext(query!!)
      return true
     }

     override fun onQueryTextChange(query: String?): Boolean {
      if (!emitter.isDisposed) {
       emitter.onNext(query!!)
      }
      return true
     }
    })
  }
   .debounce(1000, TimeUnit.MILLISECONDS)
   .subscribeOn(Schedulers.io())
   .distinctUntilChanged()
   .observeOn(AndroidSchedulers.mainThread())
   .subscribe({ term ->
    trackViewModel.isSearchStarted = true
    /**
     * Since when this fragments re-creates when the app navigates to the InfoFragment
     * it will trigger this search function.
     *
     * We need to know if search query changes,
     * If it change execute search and then dispose previous observable,
     * IF not, observed the same observable.
     *
     * Any action like save (add to favorite) or delete (remove to favorite) will be observed.
     */
    if (searchTerm != term || trackViewModel.isNavigatedToInfo) {
     trackViewModel.disposePreviousSearch()
     trackViewModel.getSearchTracks(term)
      .observe(viewLifecycleOwner) { result ->
       when (result) {
        is Resource.Success -> {
         if (result.data.isNullOrEmpty()) {
          displayNoDataFound()
         } else {
          hideNoDataFound()
          Log.i("DataReflected", "Search Result")
          trackAdapter.setList(result.data)
          trackAdapter.notifyDataSetChanged()
         }
         hideProgressBar()
        }
        is Resource.Loading -> {
         Log.i("DataReflected", "Search Loading")
         displayProgressBar()
        }
        is Resource.Error -> {
         hideProgressBar()
         Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
        }
       }
      }
     if (!trackViewModel.isNavigatedToInfo) {
      binding?.trackRecyclerView?.smoothScrollToPosition(0)
     }
    }
    trackViewModel.isNavigatedToInfo = false
    searchTerm = term
   },
    {
     Log.e("SearchViewError", it.message.toString())
    }
   )

 }

 override fun displayProgressBar() {
  binding?.progressBar?.visibility = View.VISIBLE
 }

 override fun hideProgressBar() {
  binding?.progressBar?.visibility = View.GONE
 }

 private fun displayNoDataFound() {
  binding?.noResultImageView?.visibility = View.VISIBLE
  binding?.trackRecyclerView?.visibility = View.GONE
 }

 private fun hideNoDataFound() {
  binding?.noResultImageView?.visibility = View.GONE
  binding?.trackRecyclerView?.visibility = View.VISIBLE
 }

 override fun onStop() {
  super.onStop()
  val currentTime = Calendar.getInstance().time
  val formattedDate = DateFormat.getDateInstance(DateFormat.FULL).format(currentTime)

  val editor = (activity as MainActivity).sharedPreferences.edit()
  editor.remove("fragment")
  editor.remove("trackId")
  editor.putString("date", formattedDate)
  editor.apply()

 }

}
