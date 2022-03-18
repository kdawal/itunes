package com.example.itunesmovie.domain.usecase

import com.example.itunesmovie.data.model.UserActivity
import com.example.itunesmovie.domain.repository.UserActivityRepository

class UpdateUserActivityUseCase(
 private val userActivityRepository: UserActivityRepository
){
 fun execute(lastAccessDate: String?, fragment: Int?, trackId: Int?) = userActivityRepository.updateUserActivity(
  lastAccessDate,
  fragment,
  trackId
 )
}