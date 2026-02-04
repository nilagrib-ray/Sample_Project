package com.app.sampleproject.domain.usecase

import android.util.Log
import com.app.sampleproject.core.utils.Resource
import com.app.sampleproject.data.mapper.toDomain
import com.app.sampleproject.domain.model.ItineraryDomain
import com.app.sampleproject.domain.repository.AuthRepository
import com.app.sampleproject.domain.repository.TripRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetItineraryUseCase @Inject constructor(
    private val tripRepository: TripRepository,
    private val authRepository: AuthRepository
) {
    operator fun invoke(
        bookingId: Int,
        eventDate: String
    ): Flow<Resource<ItineraryDomain>> = flow {
        try {
            emit(Resource.Loading)

            val userData = authRepository.getUserData()
            if (userData == null) {
                Log.e("GetItineraryUseCase", "User not logged in")
                emit(Resource.Error(message = "User not logged in. Please login again."))
                return@flow
            }

            Log.d("GetItineraryUseCase", "Fetching itinerary for bookingId: $bookingId, date: $eventDate")

            val response = tripRepository.getItinerary(
                bookingId = bookingId,
                eventDate = eventDate,
                userId = userData.userId
            )

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    val itinerary = body.toDomain()
                    Log.d("GetItineraryUseCase", "Itinerary loaded: ${itinerary.events.size} events")
                    emit(Resource.Success(data = itinerary))
                } else {
                    Log.e("GetItineraryUseCase", "Response body is null")
                    emit(Resource.Error(message = "Failed to load itinerary: Empty response"))
                }
            } else {
                val errorMsg = "Network error: ${response.code()} - ${response.message()}"
                Log.e("GetItineraryUseCase", errorMsg)
                emit(Resource.Error(message = errorMsg))
            }
        } catch (e: Exception) {
            Log.e("GetItineraryUseCase", "Exception in getItinerary: ${e.message}", e)
            emit(Resource.Error(message = e.localizedMessage ?: "Unknown error occurred"))
        }
    }
}