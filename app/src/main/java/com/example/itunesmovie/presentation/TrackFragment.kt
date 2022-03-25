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

 override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
  super.onViewCreated(view, savedInstanceState)
  trackViewModel = (activity as MainActivity).trackViewModel
  trackAdapter = (activity as MainActivity).trackAdapter
  headerAdapter = (activity as MainActivity).headerAdapter
  sharedPreferences = (activity as MainActivity).sharedPreferences
  Log.i("OnCreateList", "Creating....")
  checkLastScreen()
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
 }

 private fun checkLastScreen() {

  val trackId = sharedPreferences.getInt("trackId", 0)
  val date = sharedPreferences.getString("date",null)
  Log.i("TrackFragmentCheck",trackId.toString())
  val fragment = sharedPreferences.getInt("fragment", 0)
  if (trackId != 0) {
   Log.i("ViewTrackList","Inn")
   trackViewModel.getTracks(isNetworkConnected)
    .observe(viewLifecycleOwner) { trackResult ->
    when (trackResult) {
     is Resource.Success -> {
      if (trackResult.data != null) {
       val track = trackResult.data.find { track -> track.trackId == trackId }
       val bundle = Bundle().apply {
        putSerializable(
         "selected_track",
         track
        )
       }
       findNavController().navigate(
        fragment,
        bundle
       )
      }
     }
     is Resource.Error -> {
      Toast.makeText(view?.context, "Something went wrong!", Toast.LENGTH_LONG).show()
      displayNoDataFound()
     }
     is Resource.Loading -> {
      Log.i("LoadingTrackList","Loading....")
      displayProgressBar()
     }
    }
   }
  }else{
   initRecyclerView(date)
   trackViewModel.isFirstTime.let {
    Log.i("FirstTime",it.toString())
    if(it) {
     viewTrackList()
     trackViewModel.isFirstTime = false
    }
   }
   viewTrackList()
   setupSearchView()

  }
 }



 /**
  * Initialized RecyclerView
  */
 private fun initRecyclerView(date: String?) {
  val linearLayoutManager = object : LinearLayoutManager(activity) {
   override fun supportsPredictiveItemAnimations(): Boolean {
    return false
   }
  }
  if(date != null){
   val concatAdapter = ConcatAdapter(headerAdapter, trackAdapter)
   binding?.trackRecyclerView?.apply {
    adapter = concatAdapter
    layoutManager = linearLayoutManager
   }
  }else{
   binding?.trackRecyclerView?.apply {
    adapter = trackAdapter
    layoutManager = linearLayoutManager
   }
  }
 }
 /**
  * Load data
  */
 @SuppressLint("NotifyDataSetChanged")
 private fun viewTrackList() {
  trackViewModel.getTracks(isNetworkConnected)
   .observe(viewLifecycleOwner){ result ->
    when(result){
     is Resource.Success ->{
      hideProgressBar()
      if(result.data.isNullOrEmpty()){
       displayNoDataFound()
      }else{
       hideNoDataFound()
       trackAdapter.setList(result.data)
       trackAdapter.notifyDataSetChanged()
      }
     }
     is Resource.Loading ->{
      displayProgressBar()
     }
     is Resource.Error ->{
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
    trackViewModel.getSearchTracks(isNetworkConnected, term)
     .observe(viewLifecycleOwner){ result ->
      when(result){
       is Resource.Success -> {
        hideProgressBar()
        if( result.data.isNullOrEmpty()){
         displayNoDataFound()
        }else{
         hideNoDataFound()
         trackAdapter.setList(result.data)
         trackAdapter.notifyDataSetChanged()
        }
       }
       is Resource.Loading ->{
        displayProgressBar()
       }
       is Resource.Error ->{
        hideProgressBar()
        Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
       }
      }
     }
   }, {
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
  binding?.dataResultTextView?.visibility = View.VISIBLE
  binding?.trackRecyclerView?.visibility = View.GONE
 }

 private fun hideNoDataFound() {
  binding?.dataResultTextView?.visibility = View.GONE
  binding?.trackRecyclerView?.visibility = View.VISIBLE
 }

 override fun onStop() {
  super.onStop()
  val currentTime = Calendar.getInstance().time
  val formattedDate = DateFormat.getDateInstance(DateFormat.FULL).format(currentTime)

  val editor = (activity as MainActivity).sharedPreferences.edit()
  editor.remove("fragment")
  editor.remove("trackId")
  editor.putString("date",formattedDate)
  editor.apply()

 }

}
