package com.example.itunesmovie.presentation

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.itunesmovie.R
import com.example.itunesmovie.data.util.Resource
import com.example.itunesmovie.databinding.FragmentTrackBinding
import com.example.itunesmovie.presentation.adapter.TrackAdapter
import com.example.itunesmovie.presentation.base.BaseFragment
import com.example.itunesmovie.presentation.viewmodel.TrackViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class TrackFragment : BaseFragment<FragmentTrackBinding>(
 FragmentTrackBinding::inflate
) {
 private lateinit var trackViewModel: TrackViewModel
 private lateinit var trackAdapter: TrackAdapter

 private var cacheSearchText = ""

 override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
  super.onViewCreated(view, savedInstanceState)
  trackViewModel = (activity as MainActivity).trackViewModel
  trackAdapter = (activity as MainActivity).trackAdapter
  Log.i("OnCreateList", "Creating....")
//  checkLastScreen()
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

 private fun checkLastScreen() {
  trackViewModel.getUserActivity()
  trackViewModel.userActivityResult.observe(viewLifecycleOwner){ userActivityResult ->
   Log.i("UserActivity", "Observer Result")
   when(userActivityResult){
    is Resource.Success ->{
     if(userActivityResult.data == null){
      trackViewModel.saveUserActivity()
     }else{
      trackViewModel.getTracks(true)
      trackViewModel.trackListResult.observe(viewLifecycleOwner){ trackResult ->
       if(trackResult.data != null){
        if(userActivityResult.data.trackId != null){
         val bundle = Bundle().apply {
          putSerializable(
           "selected_track",
           trackResult.data.find { track -> track.trackId == userActivityResult.data.trackId }
          )
         }
          findNavController().navigate(
          userActivityResult.data.lastAccessScreen!!.toInt(),
          bundle
         )
        }
       }
      }
     }

    }
    is Resource.Error ->{
     Log.i("UserActivity",userActivityResult.message.toString())
    }
    is Resource.Loading ->{

    }
   }
  }
 }


 private fun initRecyclerView() {

  val linearLayoutManager = object : LinearLayoutManager(activity) {
   override fun supportsPredictiveItemAnimations(): Boolean {
    return false
   }
  }
  binding?.trackRecyclerView?.apply {
   adapter = trackAdapter
   layoutManager = linearLayoutManager
  }
 }

 private fun viewTrackList() {

  if (cacheSearchText.isEmpty()) {
   trackViewModel.getTracks(true)
  } else {
   if (!trackViewModel.isNavigatedToInfo) {
    trackViewModel.getSearchTracks(true, cacheSearchText)
   }
  }
  trackViewModel.isNavigatedToInfo = false

 }

 private fun setObservers() {
  trackViewModel.trackListResult.observe(viewLifecycleOwner) { response ->
   when (response) {
    is Resource.Success -> {
     response.data?.let {
      if (it.isNullOrEmpty()) {
       Log.i("SearchCache", cacheSearchText)
       displayNoDataFound()
       hideProgressBar()
      } else {
       hideNoDataFound()
//       trackAdapter.setList(it)
//       trackAdapter.notifyDataSetChanged()
       trackAdapter.submitList(it)
       hideProgressBar()
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
 }

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
   .subscribe(
    {
     if(it.isNullOrEmpty()){
      trackViewModel.getTracks(true)
     }else{
      trackViewModel.getSearchTracks(true, it)
     }
     if (cacheSearchText != it) {
      binding?.trackRecyclerView?.scrollToPosition(0)
     }
     cacheSearchText = it

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
  binding?.dataResultTextView?.visibility = View.VISIBLE
  binding?.trackRecyclerView?.visibility = View.GONE
 }

 private fun hideNoDataFound() {
  binding?.dataResultTextView?.visibility = View.GONE
  binding?.trackRecyclerView?.visibility = View.VISIBLE
 }


}
