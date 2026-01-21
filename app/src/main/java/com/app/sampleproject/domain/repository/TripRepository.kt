package com.app.sampleproject.domain.repository

import com.app.sampleproject.domain.model.TripDomain
import com.app.sampleproject.core.utils.Resource
import kotlinx.coroutines.flow.Flow

interface TripRepository {
    suspend fun getTrips(userId: String, userType: String): Flow<Resource<List<TripDomain>>>
    suspend fun getUpcomingTrips(): Flow<Resource<List<TripDomain>>>
}