package com.app.sampleproject.data.repository

import com.app.sampleproject.data.remote.ApiService
import com.app.sampleproject.data.remote.dto.CategoryDto
import com.app.sampleproject.data.remote.dto.TripsResponse
import com.app.sampleproject.domain.repository.TripRepository
import retrofit2.Response
import javax.inject.Inject

class TripRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : TripRepository {

    override suspend fun getTrips(userId: String): Response<TripsResponse> {
        return apiService.getTrips(userId)
    }

    override suspend fun getUpcomingTrips(): Response<List<CategoryDto>> {
        return apiService.getUpcomingTrips()
    }
}