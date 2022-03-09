package com.example.itunesmovie.data

import com.example.itunesmovie.data.api.TrackAPIService
import com.example.itunesmovie.data.model.APIResponse
import com.google.common.truth.Truth.assertThat
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import okio.buffer
import okio.source
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(JUnit4::class)
class TrackAPIServiceTest {
 private lateinit var service: TrackAPIService
 private lateinit var server: MockWebServer

 @Before
 fun setUp() {
  server = MockWebServer()
  service = Retrofit.Builder()
   .baseUrl(server.url(""))
   .addConverterFactory(GsonConverterFactory.create())
   .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
   .build()
   .create(TrackAPIService::class.java)
 }

 @Test
 fun getTracks_sendRequest_receivedExpected() {
  enqueueMockResponse()
  val response = service.getTracks()
  val request = server.takeRequest()
  assertThat(response).isNull()

 }

 private fun enqueueMockResponse(){
  val inputStream = javaClass.classLoader!!.getResourceAsStream("TracksResponse.json")
  val source = inputStream.source().buffer()
  val mockResponse = MockResponse()
  mockResponse.setBody(source.readString(Charsets.UTF_8))
  server.enqueue(mockResponse)
 }
}