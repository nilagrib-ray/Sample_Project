package com.app.sampleproject.data.repository

import com.app.sampleproject.data.remote.ApiService
import com.app.sampleproject.data.remote.dto.ItineraryResponse
import com.app.sampleproject.data.remote.dto.TripDetailsResponse
import com.app.sampleproject.domain.repository.TripDetailsRepository
import retrofit2.Response
import javax.inject.Inject

class TripDetailsRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : TripDetailsRepository {

    override suspend fun getTripDetails(
        packageId: Int,
        bookingId: Int,
        orderId: String,
        userId: String
    ): Response<TripDetailsResponse> {
        return apiService.getTripDetails(packageId, bookingId, orderId, userId)
    }

    override suspend fun getItinerary(
        bookingId: Int,
        eventDate: String,
        userId: String
    ): Response<ItineraryResponse> {
        return apiService.getItinerary(bookingId, eventDate, userId)
    }
}