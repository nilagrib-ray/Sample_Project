package com.app.sampleproject.domain.usecase

import android.util.Log
import com.app.sampleproject.core.utils.Resource
import com.app.sampleproject.data.mapper.toDomain
import com.app.sampleproject.domain.model.TripDetailsDomain
import com.app.sampleproject.domain.repository.AuthRepository
import com.app.sampleproject.domain.repository.TripRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetTripDetailsUseCase @Inject constructor(
    private val tripRepository: TripRepository,
    private val authRepository: AuthRepository
) {
    operator fun invoke(
        packageId: Int?,
        bookingId: Int?,
        orderId: String?
    ): Flow<Resource<TripDetailsDomain>> = flow {
        try {
            emit(Resource.Loading)

            val userData = authRepository.getUserData()
            if (userData == null) {
                Log.e("GetTripDetailsUseCase", "User not logged in")
                emit(Resource.Error(message = "User not logged in. Please login again."))
                return@flow
            }

            Log.d("GetTripDetailsUseCase", "Fetching trip details for packageId: $packageId, bookingId: $bookingId")

            val response = tripRepository.getTripDetails(
                packageId = packageId,
                bookingId = bookingId,
                orderId = orderId,
                userId = userData.userId
            )

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    val tripDetails = body.toDomain()
                    Log.d("GetTripDetailsUseCase", "Trip details loaded: ${tripDetails.tripName}")
                    emit(Resource.Success(data = tripDetails))
                } else {
                    Log.e("GetTripDetailsUseCase", "Response body is null")
                    emit(Resource.Error(message = "Failed to load trip details: Empty response"))
                }
            } else {
                val errorMsg = "Network error: ${response.code()} - ${response.message()}"
                Log.e("GetTripDetailsUseCase", errorMsg)
                emit(Resource.Error(message = errorMsg))
            }
        } catch (e: Exception) {
            Log.e("GetTripDetailsUseCase", "Exception in getTripDetails: ${e.message}", e)
            emit(Resource.Error(message = e.localizedMessage ?: "Unknown error occurred"))
        }
    }
}