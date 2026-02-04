package com.app.sampleproject.domain.model

data class TripDetailsDomain(
    val id: Int,
    val packageId: Int?,
    val bookingId: Int?,
    val orderId: String?,
    val tripName: String,
    val description: String?,
    val arrivalDate: String,
    val arrivalTime: String?,
    val departureDate: String,
    val departureTime: String?,
    val hotel: String?,
    val featuredImage: String?,
    val image: String?,
    val squareImage: String?,
    val travellers: List<Traveller>,
    val bookingTotal: String?,
    val bookingBalance: String?,
    val currencySymbol: String?,
    val destinationName: String?,
    val destinationImage: String?,
    val actionsRequired: List<ActionRequired>,
    val daysToGo: Int,
    val destinationLatitude: Double?,
    val destinationLongitude: Double?,
    val meetingPointDetails: String?
)

data class Traveller(
    val id: String,
    val firstName: String,
    val lastName: String,
    val fullName: String,
    val email: String?,
    val isLeadBooker: Boolean
)

data class ActionRequired(
    val id: String,
    val title: String,
    val description: String,
    val actionType: String
)

data class ItineraryDomain(
    val events: List<Event>,
    val eventDate: String
)

data class Event(
    val id: Int,
    val title: String,
    val description: String?,
    val startTime: String?,
    val endTime: String?,
    val location: String?,
    val eventType: String,
    val image: String?
)