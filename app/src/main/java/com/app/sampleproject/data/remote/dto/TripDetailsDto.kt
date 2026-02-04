package com.app.sampleproject.data.remote.dto

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName

data class TripDetailsResponse(
    @SerializedName("id") val id: Int?,
    @SerializedName("package_id") val packageId: Int?,
    @SerializedName("booking_id") val bookingId: Int?,
    @SerializedName("order_id") val orderId: String?,
    @SerializedName("package_title") val packageTitle: String?,
    @SerializedName("booking_title") val bookingTitle: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("arrival_date") val arrivalDate: String?,
    @SerializedName("arrival_time") val arrivalTime: String?,
    @SerializedName("departure_date") val departureDate: String?,
    @SerializedName("departure_time") val departureTime: String?,
    @SerializedName("hotel") val hotel: String?,
    @SerializedName("featured_image") val featuredImage: String?,
    @SerializedName("image") val image: String?,
    @SerializedName("square_image") val squareImage: String?,
    @SerializedName("travellers") val travellers: JsonElement?,
    @SerializedName("booking_total") val bookingTotal: String?,
    @SerializedName("booking_balance") val bookingBalance: String?,
    @SerializedName("currency_symbol") val currencySymbol: String?,
    @SerializedName("destination") val destination: List<DestinationDto>?,
    @SerializedName("actions_required") val actionsRequired: List<ActionRequiredDto>?,
    @SerializedName("meeting_point") val meetingPoint: MeetingPointDto?,
    @SerializedName("meeting_point_details") val meetingPointDetails: String?
)

data class TravellerDto(
    @SerializedName("id") val id: String?,
    @SerializedName("first_name") val firstName: String?,
    @SerializedName("last_name") val lastName: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("is_lead_booker") val isLeadBooker: Boolean?
)

data class ActionRequiredDto(
    @SerializedName("id") val id: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("action_type") val actionType: String?
)

data class ItineraryResponse(
    @SerializedName("events") val events: List<EventDto>?,
    @SerializedName("event_date") val eventDate: String?
)

data class EventDto(
    @SerializedName("id") val id: Int?,
    @SerializedName("title") val title: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("start_time") val startTime: String?,
    @SerializedName("end_time") val endTime: String?,
    @SerializedName("location") val location: String?,
    @SerializedName("event_type") val eventType: String?,
    @SerializedName("image") val image: String?
)

data class MeetingPointDto(
    @SerializedName("lat") val lat: Double?,
    @SerializedName("lng") val lng: Double?,
    @SerializedName("address") val address: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("city") val city: String?,
    @SerializedName("country") val country: String?
)