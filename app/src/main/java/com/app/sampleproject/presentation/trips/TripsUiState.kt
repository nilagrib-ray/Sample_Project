package com.app.sampleproject.presentation.trips

import com.app.sampleproject.domain.model.DestinationCategory
import com.app.sampleproject.domain.model.TripDomain

data class TripsUiState(
    val isLoading: Boolean = false,
    val upcomingTrips: List<TripDomain> = emptyList(),
    val destinations: List<DestinationCategory> = emptyList(),
    val previousTrips: List<TripDomain> = emptyList(),
    val errorMessage: String? = null
)