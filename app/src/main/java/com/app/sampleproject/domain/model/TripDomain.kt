package com.app.sampleproject.domain.model

data class TripDomain(
    val tripId: String,
    val tripName: String,
    val tripImage: String?,
    val startDate: String,
    val endDate: String,
    val location: String?,
    val status: String?
)