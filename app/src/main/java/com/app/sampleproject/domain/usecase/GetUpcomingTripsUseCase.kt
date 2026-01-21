package com.app.sampleproject.domain.usecase

import com.app.sampleproject.domain.model.TripDomain
import com.app.sampleproject.domain.repository.TripRepository
import com.app.sampleproject.core.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUpcomingTripsUseCase @Inject constructor(
    private val tripRepository: TripRepository
) {
    suspend operator fun invoke(): Flow<Resource<List<TripDomain>>> {
        return tripRepository.getUpcomingTrips()
    }
}
