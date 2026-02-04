package com.app.sampleproject.domain.repository

import com.app.sampleproject.data.remote.dto.ItineraryResponse
import com.app.sampleproject.data.remote.dto.TripDetailsResponse
import retrofit2.Response

interface TripDetailsRepository {
    suspend fun getTripDetails(
        packageId: Int,
        bookingId: Int,
        orderId: String,
        userId: String
    ): Response<TripDetailsResponse>

    suspend fun getItinerary(
        bookingId: Int,
        eventDate: String,
        userId: String
    ): Response<ItineraryResponse>
}