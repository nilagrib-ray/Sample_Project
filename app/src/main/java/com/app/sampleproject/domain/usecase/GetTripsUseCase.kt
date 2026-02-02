package com.app.sampleproject.domain.usecase

import android.util.Log
import com.app.sampleproject.core.utils.Resource
import com.app.sampleproject.data.mapper.toDomain
import com.app.sampleproject.domain.model.TripDomain
import com.app.sampleproject.domain.repository.AuthRepository
import com.app.sampleproject.domain.repository.TripRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetTripsUseCase @Inject constructor(
    private val tripRepository: TripRepository,
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Flow<Resource<List<TripDomain>>> = flow {
        try {
            emit(Resource.Loading)

            val userData = authRepository.getUserData()
            if (userData == null) {
                Log.e("GetTripsUseCase", "User not logged in")
                emit(Resource.Error(message = "User not logged in. Please login again."))
                return@flow
            }

            Log.d("GetTripsUseCase", "Fetching trips for userId: ${userData.userId}")

            val response = tripRepository.getTrips(userData.userId)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    val allTrips = mutableListOf<TripDomain>()

                    body.upcomingTrips?.let { trips ->
                        Log.d("GetTripsUseCase", "Upcoming trips: ${trips.size}")
                        allTrips.addAll(trips.toDomain())
                    }

                    body.previousTrips?.let { trips ->
                        Log.d("GetTripsUseCase", "Previous trips: ${trips.size}")
                        allTrips.addAll(trips.toDomain())
                    }

                    Log.d("GetTripsUseCase", "Total trips loaded: ${allTrips.size}")
                    emit(Resource.Success(data = allTrips))
                } else {
                    Log.e("GetTripsUseCase", "Response body is null")
                    emit(Resource.Error(message = "Failed to load trips: Empty response"))
                }
            } else {
                val errorMsg = "Network error: ${response.code()} - ${response.message()}"
                Log.e("GetTripsUseCase", errorMsg)
                emit(Resource.Error(message = errorMsg))
            }
        } catch (e: Exception) {
            Log.e("GetTripsUseCase", "Exception in getTrips: ${e.message}", e)
            emit(Resource.Error(message = e.localizedMessage ?: "Unknown error occurred"))
        }
    }
}