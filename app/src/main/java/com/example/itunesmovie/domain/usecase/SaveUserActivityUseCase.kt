package com.example.itunesmovie.domain.usecase

import com.example.itunesmovie.data.model.UserActivity
import com.example.itunesmovie.domain.repository.UserActivityRepository

class SaveUserActivityUseCase(
 private val userActivityRepository: UserActivityRepository
) {
 fun execute(userActivity: UserActivity) = userActivityRepository.saveUserActivity(userActivity)
}