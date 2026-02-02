package com.app.sampleproject.domain.model

data class TripDomain(
    val tripId: String,
    val tripName: String,
    val featuredImage: String?,
    val image: String?,
    val squareImage: String?,
    val destinationImage: String?,
    val startDate: String,
    val endDate: String,
    val location: String?,
    val status: String?,
    val bookingTotal: String? = null,
    val bookingBalance: String? = null,
    val currencySymbol: String? = null,
    val hotel: String? = null,
    val type: String? = null
)

data class DestinationCategory(
    val categoryId: Int,
    val categoryName: String,
    val destUrl: String,
    val imageUrl: String,
    val squareImageUrl: String
)