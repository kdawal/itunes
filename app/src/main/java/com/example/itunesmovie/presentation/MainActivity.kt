package com.example.itunesmovie.presentation

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.itunesmovie.R
import com.example.itunesmovie.data.util.Resource
import com.example.itunesmovie.databinding.ActivityMainBinding
import com.example.itunesmovie.presentation.adapter.TrackAdapter
import com.example.itunesmovie.presentation.viewmodel.TrackViewModel
import com.example.itunesmovie.presentation.viewmodel.TrackViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
 @Inject
 lateinit var trackViewModelFactory: TrackViewModelFactory
 @Inject
 lateinit var trackAdapter: TrackAdapter
 @Inject
 lateinit var sharedPreferences: SharedPreferences
 lateinit var trackViewModel: TrackViewModel
 private lateinit var mainActivityBinding: ActivityMainBinding
 private val navHostFragment by lazy{
  supportFragmentManager
  .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
 }

 override fun onCreate(savedInstanceState: Bundle?) {
  super.onCreate(savedInstanceState)
  mainActivityBinding = ActivityMainBinding.inflate(layoutInflater)
  setContentView(mainActivityBinding.root)
  trackViewModel = ViewModelProvider(this, trackViewModelFactory)[TrackViewModel::class.java]
  Log.i("UserActivity", "Creating Activity")

 }

}