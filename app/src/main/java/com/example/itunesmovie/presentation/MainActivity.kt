package com.example.itunesmovie.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.itunesmovie.R
import com.example.itunesmovie.databinding.ActivityMainBinding
import com.example.itunesmovie.presentation.adapter.TrackAdapter
import com.example.itunesmovie.presentation.viewmodel.TrackViewModel
import com.example.itunesmovie.presentation.viewmodel.TrackViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
 @Inject
 lateinit var trackViewModelFactory: TrackViewModelFactory
 @Inject
 lateinit var trackAdapter: TrackAdapter
 lateinit var trackViewModel: TrackViewModel
 private lateinit var mainAcitvityBinding: ActivityMainBinding

 override fun onCreate(savedInstanceState: Bundle?) {
  super.onCreate(savedInstanceState)
  mainAcitvityBinding = ActivityMainBinding.inflate(layoutInflater)
  setContentView(mainAcitvityBinding.root)

  trackViewModel = ViewModelProvider(this, trackViewModelFactory)[TrackViewModel::class.java]

 }

}