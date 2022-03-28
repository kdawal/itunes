package com.example.itunesmovie.presentation

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.itunesmovie.R
import com.example.itunesmovie.data.util.Resource
import com.example.itunesmovie.databinding.ActivityMainBinding
import com.example.itunesmovie.presentation.adapter.HeaderAdapter
import com.example.itunesmovie.presentation.adapter.TrackAdapter
import com.example.itunesmovie.presentation.viewmodel.TrackViewModel
import com.example.itunesmovie.presentation.viewmodel.TrackViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import java.text.DateFormat
import java.util.*
import javax.inject.Inject

/**
 * This is where we inject our dependency
 *
 * ViewModel is also set here
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
 @Inject
 lateinit var trackViewModelFactory: TrackViewModelFactory
 @Inject
 lateinit var headerAdapter: HeaderAdapter
 @Inject
 lateinit var sharedPreferences: SharedPreferences
 lateinit var trackViewModel: TrackViewModel
 lateinit var trackAdapter: TrackAdapter
 private lateinit var mainActivityBinding: ActivityMainBinding
 private val navHostFragment by lazy {
  supportFragmentManager
   .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
 }

 override fun onCreate(savedInstanceState: Bundle?) {
  super.onCreate(savedInstanceState)
  trackViewModel = ViewModelProvider(this, trackViewModelFactory)[TrackViewModel::class.java]
  trackAdapter = TrackAdapter(trackViewModel)
  mainActivityBinding = ActivityMainBinding.inflate(layoutInflater)
  setContentView(mainActivityBinding.root)

  checkLastSavedScreen()
 }


 /**
  * Saved last screen when the app stop
  */
 override fun onPause() {
  super.onPause()
  val currentTime = Calendar.getInstance().time
  val formattedDate = DateFormat.getDateInstance(DateFormat.FULL).format(currentTime)

  val editor = sharedPreferences.edit()
  val fragment = navHostFragment.navController.currentDestination?.id
  val trackId = trackViewModel.trackId?:0
  editor.putInt("fragment", fragment!!)
  if(fragment.toString().contains("TrackFragment")){
   editor.putInt("trackId", 0)
  }else{
   editor.putInt("trackId", trackId)
  }
  editor.putString("date", formattedDate)
  editor.apply()
 }

 @SuppressLint("NotifyDataSetChanged")
 private fun checkLastSavedScreen(){
  val trackId = sharedPreferences.getInt("trackId", 0)
  val date = sharedPreferences.getString("date",null)
  Log.i("TrackFragmentCheck",trackId.toString())
  val fragment = sharedPreferences.getInt("fragment", 0)

  /**
   *Check last opened screen
   *
   * If trackId is not equal to 0, it means that user navigated into InfoFragment
   * when the app stop the last time it was opened
   */

  if(trackId != 0){
   trackViewModel.getTracks()
    .observe(this) { result ->
     when (result) {
      is Resource.Success -> {
       if (result.data != null) {
        val track = result.data.find { track -> track.trackId == trackId }
        val bundle = Bundle().apply {
         putSerializable(
          "selected_track",
          track
         )
        }
        navHostFragment.navController.navigate(
         fragment,
         bundle
        )
       }
      }
      is Resource.Error -> {
       Toast.makeText(this, "Something went wrong!", Toast.LENGTH_LONG).show()
      }
      is Resource.Loading -> {
       Log.i("LoadingTrackList","Loading....")
      }
     }
    }
  }
  if(!date.isNullOrEmpty()){
   headerAdapter.setData(date.toString())
   headerAdapter.notifyDataSetChanged()
  }
 }

}