package com.app.sampleproject.domain.repository

import com.app.sampleproject.data.remote.dto.CategoryDto
import com.app.sampleproject.data.remote.dto.TripsResponse
import retrofit2.Response

interface TripRepository {
    suspend fun getTrips(userId: String): Response<TripsResponse>
    suspend fun getUpcomingTrips(): Response<List<CategoryDto>>
}