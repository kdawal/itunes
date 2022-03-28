package com.example.itunesmovie.data.util

/**
 * A class that handles the response data
 * Serves as a flagging during execution of data process
 *
 * Success: for success data
 *
 * Loading: use for displaying progress bar.
 *
 * Error: use for displaying error message.
 * Let the user know that there is an error
 *
 */
sealed class Resource<T>(
 val data: T? = null,
 val message: String? = null,
) {
  class Success<T>(data: T?) : Resource<T>(data)
  class Loading<T>(data: T? = null) : Resource<T>(data)
  class Error<T>(message: String?, data: T? = null) : Resource<T>(data, message)
}