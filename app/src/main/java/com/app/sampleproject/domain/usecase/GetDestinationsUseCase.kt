package com.app.sampleproject.domain.usecase

import android.util.Log
import com.app.sampleproject.core.utils.Resource
import com.app.sampleproject.data.mapper.toDestinationCategories
import com.app.sampleproject.domain.model.DestinationCategory
import com.app.sampleproject.domain.repository.TripRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetDestinationsUseCase @Inject constructor(
    private val tripRepository: TripRepository
) {
    operator fun invoke(): Flow<Resource<List<DestinationCategory>>> = flow {
        try {
            emit(Resource.Loading)

            Log.d("GetDestinationsUseCase", "Fetching destination categories...")
            val response = tripRepository.getUpcomingTrips()

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    val destinations = body.toDestinationCategories()
                    Log.d("GetDestinationsUseCase", "Destinations loaded: ${destinations.size}")
                    emit(Resource.Success(data = destinations))
                } else {
                    Log.e("GetDestinationsUseCase", "Response body is null")
                    emit(Resource.Error(message = "Failed to load destinations: Empty response"))
                }
            } else {
                val errorMsg = "Network error: ${response.code()} - ${response.message()}"
                Log.e("GetDestinationsUseCase", errorMsg)
                emit(Resource.Error(message = errorMsg))
            }
        } catch (e: Exception) {
            Log.e("GetDestinationsUseCase", "Exception in getDestinations: ${e.message}", e)
            emit(Resource.Error(message = e.localizedMessage ?: "Unknown error occurred"))
        }
    }
}