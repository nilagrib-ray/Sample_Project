package com.app.sampleproject.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TripsResponse(
    @SerializedName("upcoming_trips") val upcomingTrips: List<TripDto>?,
    @SerializedName("previous_trips") val previousTrips: List<TripDto>?
)

data class TripDto(
    @SerializedName("package_id") val packageId: Int?,
    @SerializedName("package_title") val packageTitle: String?,
    @SerializedName("booking_id") val bookingId: Int?,
    @SerializedName("booking_title") val bookingTitle: String?,
    @SerializedName("arrival_date") val arrivalDate: String?,
    @SerializedName("departure_date") val departureDate: String?,
    @SerializedName("featured_image") val featuredImage: String?,
    @SerializedName("image") val image: String?,
    @SerializedName("square_image") val squareImage: String?,
    @SerializedName("hotel") val hotel: String?,
    @SerializedName("destination") val destination: List<DestinationDto>?
)

data class DestinationDto(
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("slug") val slug: String?
)

data class CategoryDto(
    @SerializedName("category_id") val categoryId: Int?,
    @SerializedName("category_name") val categoryName: String?,
    @SerializedName("posts") val posts: List<PostDto>?
)

data class PostDto(
    @SerializedName("id") val id: Int?,
    @SerializedName("title") val title: String?,
    @SerializedName("start_date") val startDate: String?,
    @SerializedName("end_date") val endDate: String?,
    @SerializedName("image") val image: String?,
    @SerializedName("square_image") val squareImage: String?
)