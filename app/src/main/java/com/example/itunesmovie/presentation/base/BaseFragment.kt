package com.example.itunesmovie.presentation.base

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding


/**
 * A base class for fragment that uses ViewBinding
 */
abstract class BaseFragment<VB : ViewBinding>(
  private val bindingInflater: (inflater: LayoutInflater) -> VB,
) : Fragment() {

  private var _binding: VB? = null

  val binding
    get() = _binding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View? {
    _binding = bindingInflater.invoke(inflater)
    if (_binding == null)
      throw IllegalArgumentException("Binding cannot be null")
    return binding?.root
  }

  abstract fun displayProgressBar()
  abstract fun hideProgressBar()

}