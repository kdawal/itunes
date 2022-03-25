package com.example.itunesmovie.presentation

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.itunesmovie.databinding.ActivityMainBinding
import com.example.itunesmovie.presentation.adapter.HeaderAdapter
import com.example.itunesmovie.presentation.adapter.TrackAdapter
import com.example.itunesmovie.presentation.viewmodel.TrackViewModel
import com.example.itunesmovie.presentation.viewmodel.TrackViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
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
 lateinit var trackAdapter: TrackAdapter
 @Inject
 lateinit var headerAdapter: HeaderAdapter
 @Inject
 lateinit var sharedPreferences: SharedPreferences
 lateinit var trackViewModel: TrackViewModel
 private lateinit var mainActivityBinding: ActivityMainBinding

 override fun onCreate(savedInstanceState: Bundle?) {
  super.onCreate(savedInstanceState)
  mainActivityBinding = ActivityMainBinding.inflate(layoutInflater)
  setContentView(mainActivityBinding.root)
  trackViewModel = ViewModelProvider(this, trackViewModelFactory)[TrackViewModel::class.java]

 }

}