package com.app.sampleproject.data.repository

import android.util.Log
import com.app.sampleproject.core.utils.Resource
import com.app.sampleproject.data.mapper.toDomain
import com.app.sampleproject.data.remote.ApiService
import com.app.sampleproject.domain.model.TripDomain
import com.app.sampleproject.domain.repository.TripRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TripRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : TripRepository {

    override suspend fun getTrips(userId: String, userType: String): Flow<Resource<List<TripDomain>>> = flow {
        try {
            emit(Resource.Loading)
            Log.d("TripRepository", "Fetching trips for userId: $userId, userType: $userType")

            val response = apiService.getTrips(userId, userType)

            Log.d("TripRepository", "Response code: ${response.code()}")
            Log.d("TripRepository", "Response body: ${response.body()}")

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    val allTrips = mutableListOf<TripDomain>()

                    body.upcomingTrips?.let { trips ->
                        Log.d("TripRepository", "Upcoming trips: ${trips.size}")
                        allTrips.addAll(trips.toDomain())
                    }

                    body.previousTrips?.let { trips ->
                        Log.d("TripRepository", "Previous trips: ${trips.size}")
                        allTrips.addAll(trips.toDomain())
                    }

                    Log.d("TripRepository", "Total trips loaded: ${allTrips.size}")
                    emit(Resource.Success(allTrips))
                } else {
                    Log.e("TripRepository", "Response body is null")
                    emit(Resource.Error("Failed to load trips: Empty response"))
                }
            } else {
                val errorMsg = "Network error: ${response.code()} - ${response.message()}"
                Log.e("TripRepository", errorMsg)
                emit(Resource.Error(errorMsg))
            }
        } catch (e: Exception) {
            Log.e("TripRepository", "Exception in getTrips: ${e.message}", e)
            emit(Resource.Error(e.message ?: "Unknown error occurred"))
        }
    }

    override suspend fun getUpcomingTrips(): Flow<Resource<List<TripDomain>>> = flow {
        try {
            emit(Resource.Loading)
            Log.d("TripRepository", "Fetching upcoming trips from separate endpoint")

            val response = apiService.getUpcomingTrips()

            Log.d("TripRepository", "Upcoming trips response code: ${response.code()}")

            if (response.isSuccessful) {
                val categories = response.body()
                if (categories != null) {
                    val allTrips = categories.flatMap { category ->
                        category.posts?.map { post ->
                            TripDomain(
                                tripId = post.id?.toString() ?: "0",
                                tripName = post.title ?: "Unnamed Trip",
                                tripImage = post.image ?: post.squareImage,
                                startDate = post.startDate ?: "",
                                endDate = post.endDate ?: "",
                                location = category.categoryName,
                                status = null
                            )
                        } ?: emptyList()
                    }

                    Log.d("TripRepository", "Upcoming trips from endpoint: ${allTrips.size}")
                    emit(Resource.Success(allTrips))
                } else {
                    Log.e("TripRepository", "Upcoming trips response body is null")
                    emit(Resource.Error("Failed to load upcoming trips"))
                }
            } else {
                val errorMsg = "Network error: ${response.code()} - ${response.message()}"
                Log.e("TripRepository", errorMsg)
                emit(Resource.Error(errorMsg))
            }
        } catch (e: Exception) {
            Log.e("TripRepository", "Exception in getUpcomingTrips: ${e.message}", e)
            emit(Resource.Error(e.message ?: "Unknown error occurred"))
        }
    }
}