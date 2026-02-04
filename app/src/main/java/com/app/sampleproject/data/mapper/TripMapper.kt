package com.app.sampleproject.data.mapper

import com.app.sampleproject.data.remote.dto.CategoryDto
import com.app.sampleproject.data.remote.dto.TripDto
import com.app.sampleproject.domain.model.DestinationCategory
import com.app.sampleproject.domain.model.TripDomain

fun TripDto.toDomain(): TripDomain {
    val locationName = destination?.firstOrNull()?.name ?: ""
    val destinationImageUrl = destination?.firstOrNull()?.descriptionFeaturedImageUrl

    return TripDomain(
        tripId = (packageId ?: bookingId ?: 0).toString(),
        packageId = packageId,
        bookingId = bookingId,
        orderId = orderId,
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

fun CategoryDto.toDomain(): DestinationCategory {
    return DestinationCategory(
        categoryId = categoryId ?: 0,
        categoryName = categoryName ?: "",
        destUrl = destUrl ?: "",
        imageUrl = descriptionFeaturedImageUrl ?: posts?.firstOrNull()?.image ?: "",
        squareImageUrl = posts?.firstOrNull()?.squareImage ?: ""
    )
}

fun List<CategoryDto>.toDestinationCategories(): List<DestinationCategory> {
    return this.map { it.toDomain() }
}