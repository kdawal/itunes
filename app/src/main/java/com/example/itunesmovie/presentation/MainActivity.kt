package com.example.itunesmovie.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.NavHostFragment
import com.example.itunesmovie.R
import com.example.itunesmovie.data.util.Resource
import com.example.itunesmovie.databinding.ActivityMainBinding
import com.example.itunesmovie.presentation.adapter.HeaderAdapter
import com.example.itunesmovie.presentation.adapter.TrackAdapter
import com.example.itunesmovie.presentation.viewmodel.TrackViewModel
import com.google.android.material.snackbar.Snackbar
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
  private val trackViewModel by viewModels<TrackViewModel>()

  @Inject
  lateinit var headerAdapter: HeaderAdapter

  @Inject
  lateinit var sharedPreferences: SharedPreferences
  lateinit var trackAdapter: TrackAdapter
  private lateinit var mainActivityBinding: ActivityMainBinding
  private val navHostFragment by lazy {
    supportFragmentManager
      .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    trackAdapter = TrackAdapter(trackViewModel)
    mainActivityBinding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(mainActivityBinding.root)
    checkNetworkConnection()
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
    val trackId = trackViewModel.trackId ?: 0
    editor.putInt("fragment", fragment!!)
    if (fragment.toString().contains("TrackFragment")) {
      editor.putInt("trackId", 0)
    } else {
      editor.putInt("trackId", trackId)
    }
    editor.putString("date", formattedDate)
    editor.apply()
  }

  @SuppressLint("NotifyDataSetChanged")
  private fun checkLastSavedScreen() {
    val trackId = sharedPreferences.getInt("trackId", 0)
    val date = sharedPreferences.getString("date", null)
    Log.i("TrackFragmentCheck", trackId.toString())
    val fragment = sharedPreferences.getInt("fragment", 0)

    /**
     *Check last opened screen
     *
     * If trackId is not equal to 0, it means that user navigated into InfoFragment
     * when the app stop the last time it was opened
     */

    if (trackId != 0) {
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
              Log.i("LoadingTrackList", "Loading....")
            }
          }
        }
    }
    if (!date.isNullOrEmpty()) {
      headerAdapter.setData(date.toString())
      headerAdapter.notifyDataSetChanged()
    }
  }

  private fun checkNetworkConnection() {
    try {
      val connectivityManager =
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
      NetworkRequest.Builder()
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        connectivityManager.registerDefaultNetworkCallback(object :
          ConnectivityManager.NetworkCallback() {

          override fun onLost(network: Network) {
            Snackbar.make(mainActivityBinding.root, "No Network Connection", Snackbar.LENGTH_LONG)
              .show()
          }

          override fun onUnavailable() {
            Snackbar.make(mainActivityBinding.root, "No Network Connection", Snackbar.LENGTH_LONG)
              .show()
          }

        }
        )
      }

    } catch (e: Exception) {
      Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
    }


  }

}