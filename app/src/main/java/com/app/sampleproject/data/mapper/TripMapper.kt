package com.app.sampleproject.data.mapper

import com.app.sampleproject.data.remote.dto.TripDto
import com.app.sampleproject.domain.model.TripDomain

fun TripDto.toDomain(): TripDomain {
    val locationName = destination?.firstOrNull()?.name ?: ""

    return TripDomain(
        tripId = (packageId ?: bookingId ?: 0).toString(),
        tripName = packageTitle ?: bookingTitle ?: "Unnamed Trip",
        tripImage = featuredImage ?: image ?: squareImage,
        startDate = arrivalDate ?: "",
        endDate = departureDate ?: "",
        location = locationName,
        status = null
    )
}

fun List<TripDto>.toDomain(): List<TripDomain> {
    return this.map { it.toDomain() }
}