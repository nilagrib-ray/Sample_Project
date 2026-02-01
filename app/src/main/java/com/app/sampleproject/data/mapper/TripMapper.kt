package com.app.sampleproject.data.mapper

import com.app.sampleproject.data.remote.dto.TripDto
import com.app.sampleproject.domain.model.TripDomain

fun TripDto.toDomain(): TripDomain {
    val locationName = destination?.firstOrNull()?.name ?: ""
    val destinationImageUrl = destination?.firstOrNull()?.descriptionFeaturedImageUrl

    return TripDomain(
        tripId = (packageId ?: bookingId ?: 0).toString(),
        tripName = packageTitle ?: bookingTitle ?: "Unnamed Trip",
        featuredImage = featuredImage,
        image = image,
        squareImage = squareImage,
        destinationImage = destinationImageUrl,
        startDate = arrivalDate ?: "",
        endDate = departureDate ?: "",
        location = locationName,
        status = null,
        bookingTotal = bookingTotal,
        bookingBalance = bookingBalance,
        currencySymbol = currencySymbol,
        hotel = hotel,
        type = type
    )
}

fun List<TripDto>.toDomain(): List<TripDomain> {
    return this.map { it.toDomain() }
}