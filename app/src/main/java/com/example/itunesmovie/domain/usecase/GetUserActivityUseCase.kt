package com.example.itunesmovie.domain.usecase

import com.example.itunesmovie.data.model.UserActivity
import com.example.itunesmovie.domain.repository.UserActivityRepository
import io.reactivex.rxjava3.core.Observable

class GetUserActivityUseCase(
 private val userActivityRepository: UserActivityRepository
) {
 fun execute(): Observable<List<UserActivity>> = userActivityRepository.getUserActivity()
}