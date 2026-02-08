package com.app.sampleproject.presentation.tripdetails

import com.app.sampleproject.domain.model.ActionRequired
import com.app.sampleproject.domain.model.Event
import com.app.sampleproject.domain.model.Traveller

data class TripDetailsUiState(
    val isLoading: Boolean = false,
    val tripName: String = "",
    val description: String? = null,
    val featuredImage: String? = null,
    val destinationImage: String? = null,
    val arrivalDate: String = "",
    val arrivalTime: String? = null,
    val departureDate: String = "",
    val departureTime: String? = null,
    val hotel: String? = null,
    val bookingNumber: String = "",
    val daysToGo: Int = 0,
    val hoursToGo: Int = 0,
    val minutesToGo: Int = 0,
    val destinationName: String? = null,
    val travellers: List<Traveller> = emptyList(),
    val actionsRequired: List<ActionRequired> = emptyList(),
    val todayEvents: List<Event> = emptyList(),
    val bookingTotal: String? = null,
    val bookingBalance: String? = null,
    val currencySymbol: String = "£",
    val errorMessage: String? = null,
    val isLoadingItinerary: Boolean = false,
    val destinationLatitude: Double? = null,
    val destinationLongitude: Double? = null,
    val meetingPointDetails: String? = null
)