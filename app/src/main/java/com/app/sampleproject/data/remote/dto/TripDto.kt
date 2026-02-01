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
    @SerializedName("owner_booking") val ownerBooking: Boolean?,
    @SerializedName("type") val type: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("arrival_date") val arrivalDate: String?,
    @SerializedName("arrival_time") val arrivalTime: String?,
    @SerializedName("departure_date") val departureDate: String?,
    @SerializedName("departure_time") val departureTime: String?,
    @SerializedName("hotel") val hotel: String?,
    @SerializedName("product_id") val productId: Int?,
    @SerializedName("order_id") val orderId: String?,
    @SerializedName("featured_image") val featuredImage: String?,
    @SerializedName("image") val image: String?,
    @SerializedName("square_image") val squareImage: String?,
    @SerializedName("travellers") val travellers: String?,
    @SerializedName("discount_total") val discountTotal: String?,
    @SerializedName("booking_total") val bookingTotal: String?,
    @SerializedName("booking_balance") val bookingBalance: String?,
    @SerializedName("currency_symbol") val currencySymbol: String?,
    @SerializedName("destination") val destination: List<DestinationDto>?
)

data class DestinationDto(
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("slug") val slug: String?,
    @SerializedName("description_featured_image_url") val descriptionFeaturedImageUrl: String?
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